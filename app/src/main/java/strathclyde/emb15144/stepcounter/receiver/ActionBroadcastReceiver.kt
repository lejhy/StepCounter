package strathclyde.emb15144.stepcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.provider.MainContentProvider
import strathclyde.emb15144.stepcounter.ui.MainActivity

class ActionBroadcastReceiver : BroadcastReceiver() {

    private fun view(context: Context) {
        context.startActivity(
            Intent(
                context,
                MainActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun changeGoal(context: Context){
        val db = MainDatabase.getInstance(context)
        val dayDao = db.dayDao

        val newGoal = MainContentProvider.getClosestGoal(db)
        val today = dayDao.getLatest()

        val oldGoal = today.goal_name
        today.goal_id = newGoal.id
        today.goal_name = newGoal.name
        today.goal_steps = newGoal.steps
        dayDao.update(today)

        Toast.makeText(
            context,
            "Changed the active goal from $oldGoal to ${today.goal_name}",
            Toast.LENGTH_LONG
        ).show()
    }
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "view" -> {
                view(context)
            }
            "Change Goal" -> {
                changeGoal(context)
                view(context)
            }
            else -> {
                Log.e(
                    "ActionBroadcastReceiver",
                    "Received an unrecognized action: ${intent.action}"
                )
            }
        }
    }
}