package strathclyde.emb15144.stepcounter.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import strathclyde.emb15144.stepcounter.database.Day

class HistoryListAdapter(val clickListener: HistoryListListener) : ListAdapter<Day, HistoryViewHolder>(HistoryListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}
