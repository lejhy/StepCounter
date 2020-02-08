package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.ListItemGoalBinding

class GoalListDiffCallback : DiffUtil.ItemCallback<Goal>() {
    override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
        return oldItem == newItem
    }

}

class GoalsListAdapter(val clickListener: GoalListListener) : ListAdapter<Goal, GoalsListAdapter.GoalViewHolder> (GoalListDiffCallback()) {

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
                val binding = ListItemGoalBinding.inflate(layoutInflater, parent, false)

                return GoalViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}

class GoalListListener(
    val onDeleteCallback: (goal: Goal) -> Unit,
    val onEditCallback: (goal: Goal) -> Unit
) {
    fun onDelete(goal: Goal) : Unit = onDeleteCallback(goal)
    fun onEdit(goal: Goal) : Unit = onEditCallback(goal)
}
