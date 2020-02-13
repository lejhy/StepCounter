package strathclyde.emb15144.stepcounter

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.list_item_history.view.*
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
    private val _editableGoals = MutableLiveData<Boolean>(false)

    val goals: LiveData<List<Goal>> = goalDao.getAll()
    val days: LiveData<List<Day>> = dayDao.getAll()
    val today: LiveData<Day> = Transformations.map(days) {
         it.last()
    }
    val todayGoal: LiveData<Goal> = Transformations.map(today) {
        Goal(it.goal_id, it.goal_name, it.goal_steps)
    }
    val editableGoals: LiveData<Boolean> = _editableGoals

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

    private val prefListener: SharedPreferences.OnSharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
        when(key) {
            "editableGoals" -> {
                _editableGoals.value = sp.getBoolean(key, false)
            }
        }
    }

    private val newDayObserver: Observer<Day> = Observer {
        val currentDate = Calendar.getInstance()
        currentDate.set(Calendar.HOUR_OF_DAY, 0)
        currentDate.set(Calendar.MINUTE, 0)
        currentDate.set(Calendar.SECOND, 0)
        currentDate.set(Calendar.MILLISECOND, 0)

        val lastDate = Calendar.getInstance()
        lastDate.time = DateFormat.standardParse(it.date)

        while (lastDate.time < currentDate.time) {

            lastDate.add(Calendar.DATE, 1)
            val day = Day(
                0,
                DateFormat.standardFormat(lastDate.time),
                0,
                it.goal_id,
                it.goal_name,
                it.goal_steps
            )
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    dayDao.insert(day)
                }
            }
        }
    }

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
        today.observeOnce(newDayObserver)
        goals.observeForever(activeGoalChangeObserver)
        dateChangedReceiver = DateChangedReceiver()
        getApplication<Application>().registerReceiver(dateChangedReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
        preferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
        viewModelJob.cancel()
        goals.removeObserver(activeGoalChangeObserver)
        getApplication<Application>().unregisterReceiver(dateChangedReceiver)
        preferences.unregisterOnSharedPreferenceChangeListener(prefListener)
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
