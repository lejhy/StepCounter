package strathclyde.emb15144.stepcounter.ui.goals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is goals Fragment"
    }
    val text: LiveData<String> = _text

    private val _array = MutableLiveData<MutableList<String>>()
    val array: LiveData<MutableList<String>>
        get() = _array

    init {
        Log.i("GoalsViewModel", "GoalsViewModel created!")
        _array.value = mutableListOf("Item1", "Item2", "Item3")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GoalsViewModel", "GoalsViewModel destroyed!")
    }

    fun addGoal(goal: String) {
        _array.value?.add(goal)
        _array.value = _array.value
    }
}
