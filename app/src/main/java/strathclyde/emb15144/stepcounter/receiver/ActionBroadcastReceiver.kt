package strathclyde.emb15144.stepcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import strathclyde.emb15144.stepcounter.ui.MainActivity

class ActionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "view" -> {
                context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            }
        }
    }
}
