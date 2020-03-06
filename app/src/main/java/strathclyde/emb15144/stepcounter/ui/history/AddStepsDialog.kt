package strathclyde.emb15144.stepcounter.ui.history

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.DialogAddStepsBinding

class AddStepsDialog (
    private val title: String,
    private val accept: (steps: Int) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogAddStepsBinding>(inflater, R.layout.dialog_add_steps, container, false)

            builder.setView(binding.root)
                .setTitle(title)
                .setPositiveButton(getString(R.string.Accept)) { _, _ ->
                    val steps = binding.stepsInput.text.toString()
                    if (steps.isNotEmpty()) {
                        accept(
                            Integer.parseInt(steps)
                        )
                    }
                }
                .setNegativeButton(getString(R.string.Cancel)) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
