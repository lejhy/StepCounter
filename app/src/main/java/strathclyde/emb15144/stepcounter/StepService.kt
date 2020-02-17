package strathclyde.emb15144.stepcounter

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.DayDao
import strathclyde.emb15144.stepcounter.database.MainDatabase

class StepService : Service(), SensorEventListener {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var steps = 0

    private lateinit var dayDao: DayDao
    private lateinit var today: LiveData<Day>

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("StepService", "OnStart called")
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.i("StepService", "OnBind called")
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        Log.i("StepService", "OnCreate called")
        super.onCreate()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification = Notification.Builder(this, getString(R.string.channel_id))
            .setContentTitle("Automatic Step Counting")
            .setContentText("StepCounter now automatically records your steps")
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(R.integer.automaticStepCounting_notification_id, notification)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        dayDao = MainDatabase.getInstance(this).dayDao
        today = dayDao.getLatestObservable()
        today.observeForever {
            Log.i("Service", ""+it)
        }
    }

    override fun onDestroy() {
        Log.i("StepService", "OnDestroy called")
        super.onDestroy()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing...
    }

    override fun onSensorChanged(event: SensorEvent) {
        val eventSteps = event.values[0].toInt()
        Log.i("Steps", ""+ eventSteps)
        if (steps == 0) {
            steps = eventSteps
            Log.i("Steps", ""+ steps)
        } else {
            today.value?.let {
                Log.i("Steps", "today")
                val deltaSteps = eventSteps - steps
                steps = eventSteps

                val updated = it.copy()
                updated.steps += deltaSteps
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        dayDao.update(updated)
                    }
                }
            }
        }
    }
}
