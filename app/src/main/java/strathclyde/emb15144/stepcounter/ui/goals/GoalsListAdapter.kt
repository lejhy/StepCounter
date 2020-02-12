package strathclyde.emb15144.stepcounter.ui.goals

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class GoalsListAdapter(val clickListener: GoalListListener, val editable: Boolean) : ListAdapter<GoalListItem, GoalViewHolder> (GoalListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, editable)
    }

}
