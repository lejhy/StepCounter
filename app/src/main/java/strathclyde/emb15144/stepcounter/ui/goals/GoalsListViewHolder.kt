package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.databinding.ListItemGoalBinding

class GoalsListViewHolder private constructor(val binding: ListItemGoalBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        goalsListItem: GoalsListItem,
        clickListener: GoalsListListener
    ) {
        binding.goalsListItem = goalsListItem
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): GoalsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemGoalBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return GoalsListViewHolder(binding)
        }
    }
}
