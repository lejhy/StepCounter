package strathclyde.emb15144.stepcounter.viewmodel

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.receiver.DateChangedBroadcastReceiver
import strathclyde.emb15144.stepcounter.utils.ObservablePreferences
import strathclyde.emb15144.stepcounter.service.NotificationService
import strathclyde.emb15144.stepcounter.service.StepsSensorService

class MainActivityViewModel(
    application: Application
): AndroidViewModel(application) {

    private val preferences: ObservablePreferences
    private var dateChangedBroadcastReceiver = DateChangedBroadcastReceiver()

    private val automaticStepCountingObserver = Observer { isEnabled: Boolean ->
        when(isEnabled) {
            true -> application.startService(Intent(application, StepsSensorService::class.java))
            false -> application.stopService(Intent(application, StepsSensorService::class.java))
        }
    }

    private val notificationObserver = Observer { isEnabled: Boolean ->
        when(isEnabled) {
            true -> application.startService(Intent(application, NotificationService::class.java))
            false -> application.stopService(Intent(application, NotificationService::class.java))
        }
    }

    init {
        createNotificationChannel(application)
        PreferenceManager.setDefaultValues(application, R.xml.root_preferences, false)
        preferences = ObservablePreferences(application)
        preferences.automaticStepCounting.observeForever(automaticStepCountingObserver)
        preferences.notifications.observeForever(notificationObserver)
        application.registerReceiver(dateChangedBroadcastReceiver, IntentFilter(Intent.ACTION_DATE_CHANGED))
    }

    override fun onCleared() {
        getApplication<Application>().unregisterReceiver(dateChangedBroadcastReceiver)
        preferences.automaticStepCounting.removeObserver(automaticStepCountingObserver)
        preferences.notifications.removeObserver(notificationObserver)
        preferences.destroy()
    }

    private fun createNotificationChannel(application: Application) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            application.getString(R.string.channel_id),
            application.getString(R.string.channel_name),
            importance
        )
        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
