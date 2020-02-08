package strathclyde.emb15144.stepcounter.ui.goals

import androidx.recyclerview.widget.DiffUtil
import strathclyde.emb15144.stepcounter.database.Goal

class GoalListDiffCallback : DiffUtil.ItemCallback<Goal>() {
    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem
    }

}
