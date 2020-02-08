package strathclyde.emb15144.stepcounter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.list_item_goal.view.*
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.Goal

@BindingAdapter("goalSteps")
fun TextView.setGoalStepsFormatted(item: Goal) {
    text = String.format("%d steps", item.steps)
}

@BindingAdapter("intToText")
fun TextView.setIntToTextFormatted(item: Int) {
    text = item.toString()
}
