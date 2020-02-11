package strathclyde.emb15144.stepcounter

import android.util.Log
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

@BindingAdapter("dateAndGoal")
fun TextView.setDateAndGoalFormatted(item: Day) {
    text = String.format("%s - %s", item.date, item.goal_name)
}

@BindingAdapter("daySteps")
fun TextView.setDayStepsFormatted(item: Day) {
    text = String.format("%s out of %s steps", item.steps, item.goal_steps)
}

@BindingAdapter("stepsPercentage")
fun TextView.setStepsPercentageFormatted(item: Day?) {
    item?.let {
        text = String.format("(%d%%)", 100 * item.steps / item.goal_steps)
    }
}
