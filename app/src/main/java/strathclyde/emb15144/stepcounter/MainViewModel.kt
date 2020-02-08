package strathclyde.emb15144.stepcounter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.DayDao
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.database.GoalDao

class MainViewModel(
    private val goalDao: GoalDao,
    private val dayDao: DayDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val goals = goalDao.getAll()
    val days = dayDao.getAll()

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
    }

    fun addGoal(goal: String, steps: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                goalDao.insert(Goal(0, goal, steps))
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
