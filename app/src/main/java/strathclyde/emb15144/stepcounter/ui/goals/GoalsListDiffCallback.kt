package strathclyde.emb15144.stepcounter.ui.goals

import androidx.recyclerview.widget.DiffUtil

class GoalsListDiffCallback : DiffUtil.ItemCallback<GoalsListItem>() {
    override fun areItemsTheSame(oldItem: GoalsListItem, newItem: GoalsListItem): Boolean {
        return oldItem.goal.id == newItem.goal.id
    }

    override fun areContentsTheSame(oldItem: GoalsListItem, newItem: GoalsListItem): Boolean {
        return oldItem == newItem
    }

}
