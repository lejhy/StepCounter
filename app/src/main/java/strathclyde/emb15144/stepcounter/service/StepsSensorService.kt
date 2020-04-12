package strathclyde.emb15144.stepcounter.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.DayDao
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.ui.MainActivity
import strathclyde.emb15144.stepcounter.utils.launchIO

class StepsSensorService : Service(), SensorEventListener {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var steps = 0

    private lateinit var dayDao: DayDao
    private lateinit var sensorManager: SensorManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, getString(R.string.channel_id))
            .setContentTitle(getString(R.string.AutomaticStepCounting))
            .setContentText(getString(R.string.AutomaticStepCountingText))
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(R.integer.automaticStepCounting_notification_id, notification)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        dayDao = MainDatabase.getInstance(this).dayDao
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        viewModelJob.cancel()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing...
    }

    override fun onSensorChanged(event: SensorEvent) {
        val eventSteps = event.values[0].toInt()
        if (steps == 0) {
            steps = eventSteps
        } else {
            val deltaSteps = eventSteps - steps
            steps = eventSteps
            launchIO(uiScope) {
                dayDao.addLatestSteps(deltaSteps)
            }
        }
    }
}
