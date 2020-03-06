package strathclyde.emb15144.stepcounter.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.Day
import strathclyde.emb15144.stepcounter.model.DayDao
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.ui.MainActivity
import strathclyde.emb15144.stepcounter.utils.MAX_STEPS

class NotificationService : Service() {

    private var lastStepRatio: Float = Float.MAX_VALUE

    private lateinit var dayDao: DayDao
    private lateinit var today: LiveData<Day>

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        dayDao = MainDatabase.getInstance(this).dayDao
        today = dayDao.getLatestObservable()
        today.observeForever(notificationObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        today.removeObserver(notificationObserver)
    }

    private val notificationObserver = Observer{ day: Day ->
        val newStepRatio = day.steps.toFloat() / day.goal_steps.toFloat()
        if (lastStepRatio < 1.0 && newStepRatio >= 1.0) {
            createAllTheWayThereNotification(day)
        } else if (lastStepRatio < 0.5 && newStepRatio >= 0.5) {
            createHalfWayThereNotification(day)
        }
        lastStepRatio = newStepRatio
    }

    private fun createHalfWayThereNotification(day: Day) {
        createProgressNotification(day, getString(R.string.halfWayThere))
    }

    private fun createAllTheWayThereNotification(day: Day) {
        createProgressNotification(day, getString(R.string.AllTheWayThere))
    }

    private fun createProgressNotification(day: Day, title: String) {
        val pendingIntent: PendingIntent =
            Intent(application, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(application, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(application, application.getString(R.string.channel_id))
            .setContentTitle(title)
            .setContentText(String.format(getString(R.string.ProgressNotificationFormat), 100 * day.steps / day.goal_steps))
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(R.integer.progress_notification_id, notification)
    }

}
