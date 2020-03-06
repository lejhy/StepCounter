package strathclyde.emb15144.stepcounter.ui.history

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.DialogChangeGoalBinding
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.ui.steps.GoalsSpinnerAdapter

class ChangeGoalDialog(
    private val title: String,
    private val goals: List<Goal>,
    private var goal: Goal,
    private val accept: (goal: Goal) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val binding = DataBindingUtil.inflate<DialogChangeGoalBinding>(inflater, R.layout.dialog_change_goal, container, false)

        val spinnerAdapter =
            GoalsSpinnerAdapter(requireActivity())
        spinnerAdapter.addAll(goals)
        binding.goalSpinner.setSelection(goals.indexOf(goal))
        binding.goalSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                goal = spinnerAdapter.getItem(position)!!
            }
        }
        binding.goalSpinner.adapter = spinnerAdapter

        builder.setView(binding.root)
            .setTitle(title)
            .setPositiveButton(getString(R.string.Accept)) { _, _ ->
                accept(
                    goal
                )
            }
            .setNegativeButton(getString(R.string.Cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }
}
