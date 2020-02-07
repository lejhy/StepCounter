package strathclyde.emb15144.stepcounter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.database.GoalDao

class SharedViewModel(
        private val datasource: GoalDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _text = MutableLiveData<String>().apply {
        value = "This is goals Fragment"
    }
    val text: LiveData<String> = _text

    val array = datasource.getAll()

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
    }

    fun addGoal(goal: String) {
        uiScope.launch {
            saveGoal(Goal(0, goal, 0))
        }
    }

    private suspend fun saveGoal(goal: Goal) {
        withContext(Dispatchers.IO) {
            datasource.insert(goal)
        }
    }
}
