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
                        accept(
                            binding.addGoalName.text.toString(),
                            Integer.parseInt(binding.addGoalSteps.text.toString())
                        )
                    }
                .setNegativeButton("Cancel") { dialog, id ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
