package strathclyde.emb15144.stepcounter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import strathclyde.emb15144.stepcounter.database.Goal

@BindingAdapter("goalSteps")
fun TextView.setGoalStepsFormatted(item: Goal) {
    text = String.format("%d steps", item.steps)
}
