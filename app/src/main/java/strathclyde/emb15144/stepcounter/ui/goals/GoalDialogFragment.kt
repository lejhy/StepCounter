package strathclyde.emb15144.stepcounter.ui.goals

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.DialogAddGoalBinding

class GoalDialogFragment(
    private val title: String,
    private val goalName: String,
    private val goalSteps: Int,
    private val accept: (name: String, steps: Int) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogAddGoalBinding>(inflater, R.layout.dialog_add_goal, container, false)

            binding.nameInput.setText(goalName)
            binding.stepsInput.setText(goalSteps.toString())

            builder.setView(binding.root)
                .setTitle(title)
                .setPositiveButton("Accept") { _, _ ->
                    val name = binding.nameInput.text.toString()
                    val steps = binding.stepsInput.text.toString()
                    if (name.isNotEmpty() && steps.isNotEmpty()) {
                        accept(
                            name,
                            Integer.parseInt(steps)
                        )
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
