package strathclyde.emb15144.stepcounter.ui.goals

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.ListItemGoalBinding

class GoalViewHolder private constructor(val binding: ListItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        goalListItem: GoalListItem,
        clickListener: GoalListListener,
        editable: Boolean
    ) {
        binding.goal = goalListItem.goal
        binding.clickListener = clickListener
        binding.editable = editable
        binding.active = goalListItem.active
        binding.executePendingBindings()
        Log.i("GoalViewHolder", ""+editable)
    }

    companion object {
        fun from(parent: ViewGroup): GoalViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemGoalBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return GoalViewHolder(binding)
        }
    }
}
