package strathclyde.emb15144.stepcounter.ui.history

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.databinding.DialogChangeGoalBinding
import strathclyde.emb15144.stepcounter.ui.steps.GoalSpinnerAdapter

class ChangeGoalDialog(
    private val title: String,
    private val goals: List<Goal>,
    private var goal: Goal,
    private val accept: (goal: Goal) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogChangeGoalBinding>(inflater, R.layout.dialog_change_goal, container, false)

            val spinnerAdapter =
                GoalSpinnerAdapter(requireActivity())
            spinnerAdapter.addAll(goals)
            binding.goalSpinner.setSelection(goals.indexOf(goal))
            binding.goalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Log.i("StepFragment", "Item selected")
                    goal = spinnerAdapter.getItem(position)!!
                }
            }
            binding.goalSpinner.adapter = spinnerAdapter

            builder.setView(binding.root)
                .setTitle(title)
                .setPositiveButton("Accept") { _, _ ->
                    accept(
                        goal
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
