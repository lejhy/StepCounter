package strathclyde.emb15144.stepcounter.ui.goals

import strathclyde.emb15144.stepcounter.database.Goal

data class GoalListItem (
    val goal: Goal,
    val deletable: Boolean,
    val editable: Boolean
)
