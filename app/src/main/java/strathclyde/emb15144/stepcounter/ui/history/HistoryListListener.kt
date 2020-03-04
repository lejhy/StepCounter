package strathclyde.emb15144.stepcounter.ui.history

import strathclyde.emb15144.stepcounter.model.Day

class HistoryListListener (
    val onAddStepsCallback: (day: Day) -> Unit,
    val onEditGoalCallback: (day: Day) -> Unit
) {
    fun onAddSteps(day: Day) : Unit = onAddStepsCallback(day)
    fun onEditGoal(day: Day) : Unit = onEditGoalCallback(day)
}
