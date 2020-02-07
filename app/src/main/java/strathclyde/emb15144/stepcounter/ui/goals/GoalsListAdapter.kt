package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.database.Goal

class GoalsListAdapter() : RecyclerView.Adapter<GoalsListAdapter.GoalViewHolder>() {

    class GoalViewHolder private constructor(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout) {
        val textView: TextView = linearLayout.findViewById(R.id.list_goal_text)

        fun bind(item: Goal) {
            textView.text = item.name
        }

        companion object {
            fun from(parent: ViewGroup): GoalViewHolder {
                val linearLayout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_goal_item, parent, false) as LinearLayout

                return GoalViewHolder(linearLayout)
            }
        }
    }

    var goals = listOf<Goal>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        return GoalViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val item = goals[position]
        holder.bind(item)
    }

    override fun getItemCount() = goals.size
}
