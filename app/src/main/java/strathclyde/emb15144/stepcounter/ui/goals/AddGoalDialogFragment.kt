package strathclyde.emb15144.stepcounter.ui.goals

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.DialogAddGoalBinding

class AddGoalDialogFragment(val accept: (name: String, steps: Int) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogAddGoalBinding>(inflater, R.layout.dialog_add_goal, container, false)

            builder.setView(binding.root)
                .setTitle("Add Goal")
                .setPositiveButton("Accept") { dialog, id ->
                    val name = binding.addGoalName.text.toString()
                    val steps = binding.addGoalSteps.text.toString()
                    if (name.isNotEmpty() && steps.isNotEmpty()) {
                        accept(
                            name,
                            Integer.parseInt(steps)
                        )
                    }
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
