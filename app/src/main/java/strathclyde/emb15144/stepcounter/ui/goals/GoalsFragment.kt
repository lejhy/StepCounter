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
import strathclyde.emb15144.stepcounter.viewmodel.EditGoalDialogViewModel
import strathclyde.emb15144.stepcounter.viewmodel.MainViewModel
import strathclyde.emb15144.stepcounter.viewmodel.MainViewModelFactory


class GoalsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewAdapter: GoalsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGoalsBinding>(inflater, R.layout.fragment_goals, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModelFactory =
            MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        viewAdapter = GoalsListAdapter(
            GoalsListListener(
                {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Are you sure?")
                        .setMessage("This goal will be deleted!")
                        .setPositiveButton("Delete") { _, _ ->
                            mainViewModel.deleteGoal(it.id)
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .create()
                        .show()
                },
                {
                    val goalDialog = EditGoalDialog(
                        EditGoalDialogViewModel(
                            "Edit Goal",
                            it.name,
                            it.steps,
                            getEditGoalCallback(it.id),
                            requireActivity().application
                        )
                    )
                    goalDialog.show(requireActivity().supportFragmentManager, "editGoalDialog")
                }
            )
        )
        mainViewModel.goals.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }
        mainViewModel.todayGoal.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }
        mainViewModel.editableGoals.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }

        binding.goalsRecyclerView.apply {
            adapter = viewAdapter
        }

        binding.addGoalButton.setOnClickListener {
            val goalDialog = EditGoalDialog(
                EditGoalDialogViewModel(
                    "Add Goal",
                    "",
                    0,
                    getAddGoalCallback(),
                    requireActivity().application
                )
            )
            goalDialog.show(requireActivity().supportFragmentManager, "addGoalDialog")
        }

        return binding.root
    }

    private fun updateGoalList(goals: List<Goal>?) {
        goals?.let {
            viewAdapter.submitList(goals.map { goal ->
                GoalsListItem(
                    goal,
                    goal.id != mainViewModel.todayGoal.value!!.id,
                    mainViewModel.editableGoals.value!! && (goal.id != mainViewModel.todayGoal.value!!.id)
                )
            })
        }
    }

    private fun getAddGoalCallback() = { name: String, steps: Int ->
        mainViewModel.addGoal(name, steps)
    }

    private fun getEditGoalCallback(id: Long) = { name: String, steps: Int ->
        mainViewModel.editGoal(Goal(id, name, steps))
    }
}
