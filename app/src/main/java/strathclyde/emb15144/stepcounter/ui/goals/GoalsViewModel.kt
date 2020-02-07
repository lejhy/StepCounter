package strathclyde.emb15144.stepcounter.ui.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GoalsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is goals Fragment"
    }
    val text: LiveData<String> = _text
}
