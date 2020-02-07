package strathclyde.emb15144.stepcounter.ui.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import strathclyde.emb15144.stepcounter.R
import strathclyde.emb15144.stepcounter.databinding.FragmentHistoryBinding
import strathclyde.emb15144.stepcounter.databinding.FragmentStepsBinding

class StepsFragment : Fragment() {

    private lateinit var stepsViewModel: StepsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        stepsViewModel =
                ViewModelProviders.of(this).get(StepsViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentStepsBinding>(inflater, R.layout.fragment_steps, container, false)

        return binding.root
    }
}
