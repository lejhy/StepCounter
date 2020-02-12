package strathclyde.emb15144.stepcounter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import strathclyde.emb15144.stepcounter.database.Day

@BindingAdapter("steps")
fun TextView.setStepsFormatted(item: Int) {
    text = String.format("%d steps", item)
}

@BindingAdapter("int")
fun TextView.setIntFormatted(item: Int) {
    text = item.toString()
}

@BindingAdapter("dateReadable")
fun TextView.setDateReadableFormatted(item: String) {
    val date = DateFormat.standardParse(item)
    text = DateFormat.readableFormat(date)
}

@BindingAdapter("stepsProgress")
fun TextView.setStepsProgressFormatted(item: Day) {
    text = String.format("%s out of %s steps (%d%%)", item.steps, item.goal_steps, 100 * item.steps / item.goal_steps)
}

@BindingAdapter("stepsPercentage")
fun TextView.setStepsPercentageFormatted(item: Day?) {
    item?.let {
        text = String.format("(%d%%)", 100 * item.steps / item.goal_steps)
    }
}
