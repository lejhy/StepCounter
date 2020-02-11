package strathclyde.emb15144.stepcounter.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.MainViewModel
import strathclyde.emb15144.stepcounter.MainViewModelFactory
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.database.Day
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.FragmentGoalsBinding
import strathclyde.emb15144.stepcounter.databinding.FragmentHistoryBinding
import strathclyde.emb15144.stepcounter.databinding.FragmentStepsBinding
import strathclyde.emb15144.stepcounter.ui.goals.GoalDialogFragment
import strathclyde.emb15144.stepcounter.ui.goals.GoalListListener
import strathclyde.emb15144.stepcounter.ui.goals.GoalsListAdapter

class HistoryFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: HistoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("HistoryFragment", "onCreate Called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("HistoryFragment", "onStart Called")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("HistoryFragment", "onCreateView Called")
        val binding = DataBindingUtil.inflate<FragmentHistoryBinding>(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel

        viewAdapter = HistoryListAdapter(HistoryListListener(
            {
                Log.i("HistoryFragment", "AddSteps")
                val dialog = AddStepsDialog("Add Steps", addStepsCallback(it))
                dialog.show(requireActivity().supportFragmentManager, "addStepsDialog")
            },
            {
                Log.i("HistoryFragment", "EditGoals")
                val dialog = ChangeGoalDialog(
                    "Change Goal",
                    mainViewModel.goals.value!!,
                    Goal(it.id, it.goal_name, it.goal_steps),
                    changeGoalCallback(it)
                )
                dialog.show(requireActivity().supportFragmentManager, "changeGoalDialog")
            }
        ))
        mainViewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.submitList(it)
            }
        })
        recyclerView = binding.recyclerViewHistory.apply {
            adapter = viewAdapter
        }

        return binding.root
    }

    private fun addStepsCallback(day: Day) = { steps: Int ->
        mainViewModel.addSteps(day, steps)
    }

    private fun changeGoalCallback(day: Day) = { goal: Goal ->
        mainViewModel.changeGoal(day, goal)
    }
}
