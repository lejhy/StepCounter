package strathclyde.emb15144.stepcounter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.model.MainDatabase

class GoalDialogViewModel(
    val title: String,
    goalName: String,
    goalSteps: Int,
    accept: (name: String, steps: Int) -> Unit,
    application: Application
): AndroidViewModel(application) {
    private val goalDao = MainDatabase.getInstance(application).goalDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val valueObserver = Observer<String> {
        uiScope.launch {
            isValid.value = when {
                name.value!!.isEmpty() -> false
                steps.value!!.isEmpty() -> false
                Integer.parseInt(steps.value!!) <= 0 -> false
                name.value!! == goalName -> true
                else -> {
                    val goal = withContext(Dispatchers.IO) {
                        goalDao.get(name.value!!)
                    }
                    goal == null
                }
            }
        }
    }

    val name = MutableLiveData<String>(goalName)
    val steps = MutableLiveData<String>(goalSteps.toString())
    val accept = { accept(name.value!!, Integer.parseInt(steps.value!!)) }
    val isValid = MutableLiveData<Boolean>(false)

    init {
        name.observeForever(valueObserver)
        steps.observeForever(valueObserver)
    }

    override fun onCleared() {
        super.onCleared()
        name.removeObserver(valueObserver)
        steps.removeObserver(valueObserver)
    }
}
