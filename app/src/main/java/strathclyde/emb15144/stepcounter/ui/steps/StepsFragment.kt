package strathclyde.emb15144.stepcounter.ui.steps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_steps.view.*
import strathclyde.emb15144.stepcounter.MainViewModel
import strathclyde.emb15144.stepcounter.MainViewModelFactory
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.database.Goal
import strathclyde.emb15144.stepcounter.databinding.FragmentStepsBinding
import strathclyde.emb15144.stepcounter.observeOnce

class StepsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("StepsFragment", "onCreate Called")
    }

    override fun onStart() {
        super.onStart()
        Log.i("StepsFragment", "onStart Called")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.i("StepsFragment", "onCreateView Called")
        val binding = DataBindingUtil.inflate<FragmentStepsBinding>(inflater, R.layout.fragment_steps, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel

        val spinnerAdapter = GoalSpinnerAdapter(requireActivity())
        binding.spinner3.adapter = spinnerAdapter
        binding.spinner3.onItemSelectedListener = onItemSelectedListener

        mainViewModel.goals.observe(viewLifecycleOwner, Observer {
            spinnerAdapter.clear()
            spinnerAdapter.addAll(it)
        })

        mainViewModel.today.observe(viewLifecycleOwner, Observer { today ->
            Log.i("StepsFragment", "today changed")
            mainViewModel.goals.observeOnce(Observer {
                Log.i("StepsFragment", "goal changed")
                it.forEachIndexed { index, goal ->
                    if (goal.id == today.goal_id) {
                        binding.spinner3.setSelection(index)
                    }
                }
            })
        })

        binding.button.setOnClickListener {
            mainViewModel.addSteps(Integer.parseInt(binding.textInputLayout.steps_input.text.toString()))
            binding.textInputLayout.steps_input.setText("")
        }

        mainViewModel.today.observe(viewLifecycleOwner, Observer {
            binding.progressBar.max = it.goal_steps
            binding.progressBar.progress = it.steps
            Log.i("StepFragment", "Steps updated")
            Log.i("StepFragment", "steps: "+it.steps)
            Log.i("StepFragment", "goal: "+it.goal_steps)
        })

        return binding.root
    }

    val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selection = parent!!.adapter.getItem(position)!! as Goal
            mainViewModel.newGoalSelected(selection)
            Log.i("StepFragment", "Item selected")
        }
    }
}
