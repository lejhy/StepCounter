package strathclyde.emb15144.stepcounter.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.viewmodel.MainViewModel
import strathclyde.emb15144.stepcounter.viewmodel.MainViewModelFactory
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.model.Day
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.databinding.FragmentHistoryBinding
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: HistoryListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHistoryBinding>(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModelFactory =
            MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        viewAdapter =
            HistoryListAdapter(HistoryListListener(
                {
                    val dialog = AddStepsDialog(
                        "Add Steps",
                        addStepsCallback(it)
                    )
                    dialog.show(requireActivity().supportFragmentManager, "addStepsDialog")
                },
                {
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
        recyclerView = binding.daysRecyclerView.apply {
            adapter = viewAdapter
        }

        binding.addHistoryButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireActivity())
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (mainViewModel.addHistory(calendar.time)) {
                    Toast.makeText(requireActivity(), "New date added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Date already exists", Toast.LENGTH_LONG).show()
                }
            }
            datePickerDialog.show()
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
