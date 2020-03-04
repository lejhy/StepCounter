package strathclyde.emb15144.stepcounter.ui.goals

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class GoalsListAdapter(val clickListener: GoalsListListener) : ListAdapter<GoalsListItem, GoalsListViewHolder> (
    GoalsListDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsListViewHolder {
        return GoalsListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GoalsListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}
