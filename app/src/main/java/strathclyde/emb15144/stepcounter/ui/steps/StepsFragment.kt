package strathclyde.emb15144.stepcounter.ui.steps

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.FragmentStepsBinding
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.utils.observeOnce
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModel
import strathclyde.emb15144.stepcounter.viewmodel.SharedViewModelFactory


class StepsFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentStepsBinding>(inflater, R.layout.fragment_steps, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory(requireActivity().application)
        ).get(SharedViewModel::class.java)
        binding.viewModel = viewModel

        val spinnerAdapter = GoalsSpinnerAdapter(requireActivity())
        binding.goalSpinner.adapter = spinnerAdapter

        binding.goalSpinner.onItemSelectedListener = onItemSelectedListener

        viewModel.goals.observe(viewLifecycleOwner, Observer {
            spinnerAdapter.clear()
            spinnerAdapter.addAll(it)
        })

        viewModel.today.observe(viewLifecycleOwner, Observer { today ->
            viewModel.goals.observeOnce(Observer {
                it.forEachIndexed { index, goal ->
                    if (goal.id == today.goal_id) {
                        binding.goalSpinner.setSelection(index)
                    }
                }
            })
        })

        binding.addStepsButton.setOnClickListener {
            val steps = binding.stepsInput.text.toString()
            if (steps.isNotEmpty()) {
                viewModel.addSteps(Integer.parseInt(steps))
                binding.stepsInput.setText("")
                hideKeyboard()
            }
        }

        viewModel.today.observe(viewLifecycleOwner, Observer {
            binding.progress.max = it.goal_steps
            binding.progress.progress = it.steps
        })

        return binding.root
    }

    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing...
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.newGoalSelected(parent!!.adapter.getItem(position)!! as Goal)
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
