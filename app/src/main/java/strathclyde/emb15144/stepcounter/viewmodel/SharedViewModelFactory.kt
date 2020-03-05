package strathclyde.emb15144.stepcounter.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.utils.ObservablePreferences

class SharedViewModelFactory(
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val datasource = MainDatabase.getInstance(application)
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(
                datasource.goalDao,
                datasource.dayDao,
                ObservablePreferences(application)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
