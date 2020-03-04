package strathclyde.emb15144.stepcounter.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.*
import strathclyde.emb15144.stepcounter.model.Day
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.utils.DateFormat
import java.util.*

class DateChangedBroadcastReceiver : BroadcastReceiver() {
    private var receiverJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + receiverJob)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_DATE_CHANGED == action) {
            uiScope.launch {
                val datasource = MainDatabase.getInstance(context)
                @SuppressLint("SimpleDateFormat")
                val newDate = DateFormat.standardFormat(Calendar.getInstance().time)
                withContext(Dispatchers.IO) {
                    val lastDate = datasource.dayDao.getLatest()
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
