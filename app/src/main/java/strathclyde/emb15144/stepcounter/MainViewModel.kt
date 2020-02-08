package strathclyde.emb15144.stepcounter

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.DayDao
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.database.GoalDao
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val goalDao: GoalDao,
    private val dayDao: DayDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    val goals: LiveData<List<Goal>> = goalDao.getAll()
    val days: LiveData<List<Day>> = dayDao.getAll()
    val today: LiveData<Day> = Transformations.map(days) {
         it[0]
    }

    private val newDayObserver: Observer<List<Day>> = Observer{ list ->
        Log.i("Days", "Days Changed: " + list.size)
        today.observeOnce(Observer<Day> {
            val now = dateFormat.format(Calendar.getInstance().time)
            if (it.date != now) {
                insertDay(now, Goal(it.goal_id, it.goal_name, it.goal_steps))
            }
        })
    }

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
        days.observeForever(newDayObserver)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
        viewModelJob.cancel()
        days.removeObserver(newDayObserver)
    }

    fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
        observeForever(object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
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
}
