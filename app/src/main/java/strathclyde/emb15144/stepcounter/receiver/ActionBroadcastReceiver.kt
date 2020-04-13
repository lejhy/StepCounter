package strathclyde.emb15144.stepcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ActionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "view" -> Log.i("ActionBroadcastReceiver", "RECEIVED VIEW")
            "change" -> Log.i("ActionBroadcastReceiver", "RECEIVED CHANGE")
        }
    }
}
