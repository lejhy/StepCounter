package strathclyde.emb15144.stepcounter.ui.goals

import strathclyde.emb15144.stepcounter.model.Goal

data class GoalsListItem (
    val goal: Goal,
    val deletable: Boolean,
    val editable: Boolean
)
