package strathclyde.emb15144.stepcounter.viewmodel

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.Day
import strathclyde.emb15144.stepcounter.model.DayDao
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.model.GoalDao
import strathclyde.emb15144.stepcounter.receiver.DateChangedBroadcastReceiver
import strathclyde.emb15144.stepcounter.service.StepsSensorService
import strathclyde.emb15144.stepcounter.ui.MainActivity
import strathclyde.emb15144.stepcounter.utils.DateFormat
import strathclyde.emb15144.stepcounter.utils.observeOnce
import java.util.*

class MainViewModel(
    private val goalDao: GoalDao,
    private val dayDao: DayDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var dateChangedBroadcastReceiver = DateChangedBroadcastReceiver()

    val preferences = ObservablePreferences(application)
    val goals = goalDao.getAllObservable()
    val days = dayDao.getAllObservable()
    val today = Transformations.map(days) {
         it.first()
    }
    val todayGoal = Transformations.map(today) {
        Goal(it.goal_id, it.goal_name, it.goal_steps)
    }

    private val activeGoalChangeObserver = Observer { list: List<Goal> ->
        todayGoal.observeOnce(Observer<Goal> {todayGoal ->
            val goal = list.find { it.id == todayGoal.id }!!
            if (todayGoal != goal) {
                updateActiveGoal(goal)
            }
        })
    }

    private val newDayObserver = Observer { day: Day ->
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        val lastDate = Calendar.getInstance()
        lastDate.time = DateFormat.standardParse(day.date)

        while (lastDate.time < currentDate.time) {

            lastDate.add(Calendar.DATE, 1)
            val newDay = Day(
                0,
                DateFormat.standardFormat(lastDate.time),
                0,
                day.goal_id,
                day.goal_name,
                day.goal_steps
            )
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    dayDao.insert(newDay)
                }
            }
        }
    }

    private val automaticStepCountingObserver = Observer { isEnabled: Boolean ->
        when(isEnabled) {
            true -> application.startService(Intent(application, StepsSensorService::class.java))
            false -> application.stopService(Intent(application, StepsSensorService::class.java))
        }
    }

    private var lastTodayStepsValue: Int = Int.MAX_VALUE
    private val notificationObserver = Observer{ day: Day ->
        if (preferences.notifications.value!!) {
            val lastStepRatio = lastTodayStepsValue.toFloat() / day.goal_steps.toFloat()
            val newStepRatio = day.steps.toFloat() / day.goal_steps.toFloat()
            if (lastStepRatio < 1.0 && newStepRatio > 1.0) {
                createAllTheWayThereNotification(day)
            } else if (lastStepRatio < 0.5 && newStepRatio >= 0.5) {
                createHalfWayThereNotification(day)
            }
        }
        lastTodayStepsValue = day.steps
    }

    init {
        today.observeOnce(newDayObserver)

        today.observeForever(notificationObserver)
        goals.observeForever(activeGoalChangeObserver)
        preferences.automaticStepCounting.observeForever(automaticStepCountingObserver)

        getApplication<Application>().registerReceiver(dateChangedBroadcastReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

        today.removeObserver(notificationObserver)
        goals.removeObserver(activeGoalChangeObserver)
        preferences.automaticStepCounting.removeObserver(automaticStepCountingObserver)
        preferences.destroy()

        getApplication<Application>().unregisterReceiver(dateChangedBroadcastReceiver)
    }

    private fun createHalfWayThereNotification(day: Day) {
        createProgressNotification(day, "Half Way There!")
    }

    private fun createAllTheWayThereNotification(day: Day) {
        createProgressNotification(day, "All The Way There!")
    }

    private fun createProgressNotification(day: Day, title: String) {
        val application = getApplication<Application>()
        val pendingIntent: PendingIntent =
            Intent(application, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(application, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(application, application.getString(R.string.channel_id))
            .setContentTitle(title)
            .setContentText(String.format("You have completed %d%% today's goal!", 100 * day.steps / day.goal_steps))
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(R.integer.progress_notification_id, notification)
    }

    private fun updateActiveGoal(goal: Goal) {
        val updated = today.value!!
        updated.goal_id = goal.id
        updated.goal_name = goal.name
        updated.goal_steps = goal.steps
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dayDao.update(updated)
            }
        }
    }

    fun addGoal(goal: String, steps: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                goalDao.insert(Goal(0, goal, steps))
            }
        }
    }

    fun editGoal(goal: Goal) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                goalDao.update(goal)
            }
        }
    }

    fun deleteGoal(id: Long) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                goalDao.delete(Goal(id))
            }
        }
    }

    fun newGoalSelected(selection: Goal) {
        if (today.value!!.goal_id != selection.id) {
            updateActiveGoal(selection)
        }
    }

    fun addSteps(steps: Int) {
        addSteps(today.value!!, steps)
    }

    fun addSteps(day: Day, steps: Int) {
        val updated = day.copy()
        updated.steps += steps
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dayDao.update(updated)
            }
        }
    }

    fun changeGoal(day: Day, goal: Goal) {
        val updated = day.copy()
        updated.apply {
            goal_id = goal.id
            goal_name = goal.name
            goal_steps = goal.steps
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dayDao.update(updated)
            }
        }
    }

    fun addHistory(date: Date): Boolean {
        val dateString = DateFormat.standardFormat(date)
        for(day in days.value!!) {
            if (day.date == dateString) {
                return false
            }
        }
        val newDay = Day(
            0,
            dateString,
            0,
            todayGoal.value!!.id,
            todayGoal.value!!.name,
            todayGoal.value!!.steps
        )
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dayDao.insert(newDay)
            }
        }
        return true
    }
}
