package strathclyde.emb15144.stepcounter

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import strathclyde.emb15144.stepcounter.database.MainDatabase

class MainViewModelFactory(
        private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val datasource = MainDatabase.getInstance(application)
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(datasource.goalDao, datasource.dayDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
