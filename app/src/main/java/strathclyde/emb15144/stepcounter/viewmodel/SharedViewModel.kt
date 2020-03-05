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

class SharedViewModel(
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

    init {
        today.observeOnce(newDayObserver)

        goals.observeForever(activeGoalChangeObserver)

        getApplication<Application>().registerReceiver(dateChangedBroadcastReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

        goals.removeObserver(activeGoalChangeObserver)
        preferences.destroy()

        getApplication<Application>().unregisterReceiver(dateChangedBroadcastReceiver)
    }

    private fun updateActiveGoal(goal: Goal) {
        val updated = today.value!!
        updated.goal_id = goal.id
        updated.goal_name = goal.name
        updated.goal_steps = goal.steps
        launchIO { dayDao.update(updated) }
    }

    private fun launchIO(code: () -> Unit) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                code()
            }
        }
    }

    fun addGoal(goal: String, steps: Int) {
        launchIO { goalDao.insert(Goal(0, goal, steps)) }
    }

    fun editGoal(goal: Goal) {
        launchIO { goalDao.update(goal) }
    }

    fun deleteGoal(id: Long) {
        launchIO { goalDao.delete(Goal(id)) }
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
        launchIO { dayDao.update(updated) }
    }

    fun changeGoal(day: Day, goal: Goal) {
        val updated = day.copy()
        updated.apply {
            goal_id = goal.id
            goal_name = goal.name
            goal_steps = goal.steps
        }
        launchIO { dayDao.update(updated) }
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
        launchIO { dayDao.insert(newDay) }
        return true
    }
}
