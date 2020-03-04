package strathclyde.emb15144.stepcounter.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.databinding.ListItemHistoryBinding
import strathclyde.emb15144.stepcounter.model.Day

class HistoryViewHolder private constructor(val binding: ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        day: Day,
        clickListener: HistoryListListener
    ) {
        binding.day = day
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): HistoryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemHistoryBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return HistoryViewHolder(binding)
        }
    }
}
