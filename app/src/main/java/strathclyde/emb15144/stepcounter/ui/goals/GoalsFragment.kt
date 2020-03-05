package strathclyde.emb15144.stepcounter.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.FragmentGoalsBinding
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.model.MainDatabase
import strathclyde.emb15144.stepcounter.viewmodel.EditGoalDialogViewModel
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModel
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModelFactory

class GoalsFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var viewAdapter: GoalsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGoalsBinding>(inflater, R.layout.fragment_goals, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(requireActivity().application)
        ).get(SharedViewModel::class.java)

        viewAdapter = GoalsListAdapter(
            GoalsListListener(
                {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Are you sure?")
                        .setMessage("This goal will be deleted!")
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteGoal(it.id)
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create()
                        .show()
                },
                {
                    EditGoalDialog(
                        EditGoalDialogViewModel(
                            "Edit Goal",
                            it.name,
                            it.steps,
                            getEditGoalCallback(it.id),
                            MainDatabase.getInstance(requireActivity().application).goalDao
                        )
                    ).show(requireActivity().supportFragmentManager, "editGoalDialog")
                }
            )
        )
        binding.goalsRecyclerView.adapter = viewAdapter

        binding.addGoalButton.setOnClickListener {
            EditGoalDialog(
                EditGoalDialogViewModel(
                    "Add Goal",
                    "",
                    0,
                    getAddGoalCallback(),
                    MainDatabase.getInstance(requireActivity().application).goalDao
                )
            ).show(requireActivity().supportFragmentManager, "addGoalDialog")
        }

        viewModel.goals.observe(viewLifecycleOwner) { updateGoalList(viewModel.goals.value) }
        viewModel.todayGoal.observe(viewLifecycleOwner) { updateGoalList(viewModel.goals.value) }
        viewModel.preferences.editableGoals.observe(viewLifecycleOwner) { updateGoalList(viewModel.goals.value) }

        return binding.root
    }

    private fun updateGoalList(goals: List<Goal>?) {
        goals?.let {
            viewAdapter.submitList(goals.map { goal ->
                GoalsListItem(
                    goal,
                    goal.id != viewModel.todayGoal.value!!.id,
                    viewModel.preferences.editableGoals.value!! && (goal.id != viewModel.todayGoal.value!!.id)
                )
            })
        }
    }

    private fun getAddGoalCallback() = { name: String, steps: Int ->
        viewModel.addGoal(name, steps)
    }

    private fun getEditGoalCallback(id: Long) = { name: String, steps: Int ->
        viewModel.editGoal(Goal(id, name, steps))
    }
}
