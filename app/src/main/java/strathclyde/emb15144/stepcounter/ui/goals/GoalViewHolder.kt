package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.databinding.ListItemGoalBinding

class GoalViewHolder private constructor(val binding: ListItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        goalListItem: GoalListItem,
        clickListener: GoalListListener
    ) {
        binding.goalItem = goalListItem
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
