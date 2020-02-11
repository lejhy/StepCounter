package strathclyde.emb15144.stepcounter.ui.goals

import androidx.recyclerview.widget.DiffUtil

class GoalListDiffCallback : DiffUtil.ItemCallback<GoalListItem>() {
    override fun areItemsTheSame(oldItem: GoalListItem, newItem: GoalListItem): Boolean {
        return oldItem.goal.id == newItem.goal.id
    }

    override fun areContentsTheSame(oldItem: GoalListItem, newItem: GoalListItem): Boolean {
        return oldItem == newItem
    }

}
