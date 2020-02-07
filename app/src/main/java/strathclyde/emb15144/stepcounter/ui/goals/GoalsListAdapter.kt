package strathclyde.emb15144.stepcounter.ui.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.R

class GoalsListAdapter() : RecyclerView.Adapter<GoalsListAdapter.GoalViewHolder>() {

    private var goals = mutableListOf<String>()

    class GoalViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout) {
        val textView: TextView = linearLayout.findViewById(R.id.list_goal_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val linearLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_goal_item, parent, false) as LinearLayout

        return GoalViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {

        holder.textView.text = goals[position]
    }

    override fun getItemCount() = goals.size

    fun setData(data: MutableList<String>) {
        goals = data
        notifyDataSetChanged()
    }
}
