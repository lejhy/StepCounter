package strathclyde.emb15144.stepcounter.ui.goals

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.DialogGoalBinding
import strathclyde.emb15144.stepcounter.viewmodel.GoalDialogViewModel

class GoalDialogFragment(
    private val viewModel: GoalDialogViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val binding = DataBindingUtil.inflate<DialogGoalBinding>(inflater, R.layout.dialog_goal, container, false)

            binding.viewModel = viewModel
            binding.lifecycleOwner = it

            builder.setView(binding.root)
                .setTitle(viewModel.title)
                .setPositiveButton("Accept") { _, _ ->
                    viewModel.accept()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        viewModel.isValid.observe(activity!!, isValidObserver)
    }

    override fun onPause() {
        super.onPause()
        viewModel.isValid.removeObserver(isValidObserver)
    }

    private val isValidObserver = Observer<Boolean>{ setPositiveButtonEnabled(it) }

    private fun setPositiveButtonEnabled(enabled: Boolean) {
        dialog?.let {
            val dialog = it as AlertDialog
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
        }
    }
}
