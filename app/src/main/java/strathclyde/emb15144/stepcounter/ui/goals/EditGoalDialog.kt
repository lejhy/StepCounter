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

class EditGoalDialog(
    private val goalDialogViewModel: GoalDialogViewModel
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val binding = DataBindingUtil.inflate<DialogGoalBinding>(inflater, R.layout.dialog_goal, container, false)

        binding.editGoalDialogViewModel = goalDialogViewModel
        binding.lifecycleOwner = requireActivity()

        builder.setView(binding.root)
            .setTitle(goalDialogViewModel.title)
            .setPositiveButton(getString(R.string.Accept)) { _, _ ->
                goalDialogViewModel.accept()
            }
            .setNegativeButton(getString(R.string.Cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        goalDialogViewModel.isValid.observe(activity!!, isValidObserver)
    }

    override fun onPause() {
        super.onPause()
        goalDialogViewModel.isValid.removeObserver(isValidObserver)
    }

    private val isValidObserver = Observer<Boolean>{ setPositiveButtonEnabled(it) }

    private fun setPositiveButtonEnabled(enabled: Boolean) {
        dialog?.let {
            val dialog = it as AlertDialog
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
        }
    }
}
