package strathclyde.emb15144.stepcounter.ui.history

import androidx.recyclerview.widget.DiffUtil
import strathclyde.emb15144.stepcounter.database.Day

class HistoryListDiffCallback : DiffUtil.ItemCallback<Day>() {
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}
