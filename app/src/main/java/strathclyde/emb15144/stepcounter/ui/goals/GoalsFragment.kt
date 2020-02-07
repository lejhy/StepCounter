package strathclyde.emb15144.stepcounter.ui.goals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.SharedViewModel
import strathclyde.emb15144.stepcounter.databinding.FragmentGoalsBinding

class GoalsFragment : Fragment() {

    private lateinit var goalsViewModel: SharedViewModel
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
        goalsViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

        binding.goalsViewModel = goalsViewModel
        viewAdapter = GoalsListAdapter()

        goalsViewModel.array.observe(viewLifecycleOwner, Observer { array ->
            viewAdapter.setData(array)
        })

        recyclerView = binding.recyclerViewGoals.apply {
            adapter = viewAdapter
        }

        binding.addGoal.setOnClickListener {
            goalsViewModel.addGoal("asdf")
        }

        return binding.root
    }
}
