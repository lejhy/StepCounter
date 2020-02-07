package strathclyde.emb15144.stepcounter.ui.steps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is steps Fragment"
    }
    val text: LiveData<String> = _text
}
