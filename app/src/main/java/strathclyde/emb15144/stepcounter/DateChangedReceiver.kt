package strathclyde.emb15144.stepcounter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.MainDatabase
import java.text.SimpleDateFormat
import java.util.*

class DateChangedReceiver : BroadcastReceiver() {
    private var receiverJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("DateChangedReceiver", "onReceive")
        val action = intent.action
        if (Intent.ACTION_DATE_CHANGED == action) {
            Log.i("DateChangedReceiver", "ACTION_DATE_CHANGED")
            uiScope.launch {
                val datasource = MainDatabase.getInstance(context)
                @SuppressLint("SimpleDateFormat")
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val newDate = dateFormat.format(Calendar.getInstance().time)
                withContext(Dispatchers.IO) {
                    val lastDate = datasource.dayDao.getLast()
                    datasource.dayDao.insert(
                        Day(
                            0,
                            newDate,
                            0,
                            lastDate.goal_id,
                            lastDate.goal_name,
                            lastDate.goal_steps
                        )
                    )
                }
            }
        }
    }
}
