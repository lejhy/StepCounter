package strathclyde.emb15144.stepcounter.ui.goals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.MainViewModel
import strathclyde.emb15144.stepcounter.MainViewModelFactory
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.FragmentGoalsBinding


class GoalsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: GoalsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("GoalsFragment", "onCreate Called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("GoalsFragment", "onStart Called")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("GoalsFragment", "onCreateView Called")
        val binding = DataBindingUtil.inflate<FragmentGoalsBinding>(inflater, R.layout.fragment_goals, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        viewAdapter = GoalsListAdapter(
            GoalListListener(
                {
                    mainViewModel.deleteGoal(it.id)
                },
                {
                    val goalDialog = GoalDialogFragment("Edit Goal", it.name, it.steps, getEditGoalCallback(it.id))
                    goalDialog.show(requireActivity().supportFragmentManager, "editGoalDialog")
                }
            )
        )
        mainViewModel.goals.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }
        mainViewModel.todayGoal.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }
        mainViewModel.editableGoals.observe(viewLifecycleOwner) { updateGoalList(mainViewModel.goals.value) }

        recyclerView = binding.goalsRecyclerView.apply {
            adapter = viewAdapter
        }

        binding.addGoalButton.setOnClickListener {
            val goalDialog = GoalDialogFragment("Add Goal", "", 0, getAddGoalCallback())
            goalDialog.show(requireActivity().supportFragmentManager, "addGoalDialog")
        }

        return binding.root
    }

    private fun updateGoalList(goals: List<Goal>?) {
        goals?.let {
            viewAdapter.submitList(goals.map { goal ->
                GoalListItem(
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
