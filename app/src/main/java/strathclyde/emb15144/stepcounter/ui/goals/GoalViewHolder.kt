package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.ListItemGoalBinding

class GoalViewHolder private constructor(val binding: ListItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        goal: Goal,
        clickListener: GoalListListener
    ) {
        binding.goal = goal
        binding.clickListener = clickListener
        binding.executePendingBindings()
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
