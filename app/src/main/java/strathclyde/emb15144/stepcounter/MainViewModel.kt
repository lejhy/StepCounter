package strathclyde.emb15144.stepcounter

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.preference.PreferenceManager
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.DayDao
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.database.GoalDao
import java.util.*

class MainViewModel(
    private val goalDao: GoalDao,
    private val dayDao: DayDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var dateChangedReceiver: DateChangedReceiver
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    var editableGoals = preferences.getBoolean("editable_goals", false)


    val goals: LiveData<List<Goal>> = goalDao.getAll()
    val days: LiveData<List<Day>> = dayDao.getAll()
    val today: LiveData<Day> = Transformations.map(days) {
         it.last()
    }
    val todayGoal: LiveData<Goal> = Transformations.map(today) {
        Goal(it.goal_id, it.goal_name, it.goal_steps)
    }

    private val activeGoalChangeObserver: Observer<List<Goal>> = Observer { list ->
        Log.i("Goals", "Goals Changed: " + list.size)
        todayGoal.observeOnce(Observer<Goal> {todayGoal ->
            Log.i("Goals", "Today goal: " + list.size)
            val goal = list.find { it.id == todayGoal.id }!!
            if (todayGoal != goal) {
                updateActiveGoal(goal)
            }
        })
    }

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
        today.observeOnce(Observer {
            val now = DateFormat.standardFormat(Calendar.getInstance().time)
            Log.i("StepFragment", "now: " + now)
            Log.i("StepFragment", "today: " + it.id)
            if (it.date != now) {
                Log.i("StepFragment", "new day")
                insertDay(now, Goal(it.goal_id, it.goal_name, it.goal_steps))
            }
        })
        goals.observeForever(activeGoalChangeObserver)
        dateChangedReceiver = DateChangedReceiver()
        getApplication<Application>().registerReceiver(dateChangedReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
        viewModelJob.cancel()
        goals.removeObserver(activeGoalChangeObserver)
        getApplication<Application>().unregisterReceiver(dateChangedReceiver)
    }

    private fun insertDay(date: String, goal: Goal) {
        val day = Day(
            0,
            date,
            0,
            goal.id,
            goal.name,
            goal.steps
        )
        uiScope.launch {
            withContext(Dispatchers.IO) {
                dayDao.insert(day)
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

    fun updateActiveGoal(goal: Goal) {
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
}
