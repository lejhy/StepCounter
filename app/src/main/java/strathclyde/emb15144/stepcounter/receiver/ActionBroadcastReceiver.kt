package strathclyde.emb15144.stepcounter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.ui.MainActivity

class ActionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "view" -> {
                context.startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            }
            "Change Goal" -> {
                val dayDao = MainDatabase.getInstance(context).dayDao
                val goalDao = MainDatabase.getInstance(context).goalDao

                val goalName = intent.extras?.getString("goalName")
                if (goalName == null || goalName.isEmpty()) {
                    Log.d("ChangeGoal", "Invalid Goal Name: $goalName")
                    return
                }

                val today = dayDao.getLatest()
                val newGoal = goalDao.get(goalName)

                if (newGoal == null) {
                    Log.d("ChangeGoal", "Could not find goal with name $goalName")
                    return
                }
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
            else -> {
                Log.e(
                    "ActionBroadcastReceiver",
                    "Received an unrecognized action: ${intent.action}"
                )
            }
        }
    }
}
