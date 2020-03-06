package strathclyde.emb15144.stepcounter.ui.history

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.FragmentHistoryBinding
import strathclyde.emb15144.stepcounter.model.Day
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModel
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModelFactory
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: HistoryListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHistoryBinding>(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(requireActivity().application)
        ).get(SharedViewModel::class.java)

        viewAdapter = HistoryListAdapter(HistoryListListener(
            {
                AddStepsDialog(
                    getString(R.string.AddSteps),
                    addStepsCallback(it)
                ).show(requireActivity().supportFragmentManager, "addStepsDialog")
            },
            {
                ChangeGoalDialog(
                    getString(R.string.ChangeGoal),
                    viewModel.goals.value!!,
                    Goal(it.id, it.goal_name, it.goal_steps),
                    changeGoalCallback(it)
                ).show(requireActivity().supportFragmentManager, "changeGoalDialog")
            }
        ))
        binding.daysRecyclerView.adapter = viewAdapter

        binding.addHistoryButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireActivity())
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (viewModel.addHistory(calendar.time)) {
                    Toast.makeText(requireActivity(), getString(R.string.NewDateAdded), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), getString(R.string.DateAlreadyExists), Toast.LENGTH_LONG).show()
                }
            }
            datePickerDialog.show()
        }

        viewModel.days.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun addStepsCallback(day: Day) = { steps: Int ->
        viewModel.addSteps(day, steps)
    }

    private fun changeGoalCallback(day: Day) = { goal: Goal ->
        viewModel.changeGoal(day, goal)
    }
}
