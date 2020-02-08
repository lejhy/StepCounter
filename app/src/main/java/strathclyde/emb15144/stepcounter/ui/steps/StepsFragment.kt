package strathclyde.emb15144.stepcounter.ui.steps

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
import strathclyde.emb15144.stepcounter.MainViewModel
import strathclyde.emb15144.stepcounter.MainViewModelFactory
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.FragmentGoalsBinding
import strathclyde.emb15144.stepcounter.databinding.FragmentHistoryBinding
import strathclyde.emb15144.stepcounter.databinding.FragmentStepsBinding

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
        binding.setLifecycleOwner(viewLifecycleOwner)
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel

        return binding.root
    }
}
