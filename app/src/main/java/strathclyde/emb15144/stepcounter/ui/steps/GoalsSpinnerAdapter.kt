package strathclyde.emb15144.stepcounter.ui.steps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import strathclyde.emb15144.stepcounter.model.Goal
import strathclyde.emb15144.stepcounter.databinding.SpinnerItemGoalBinding


class GoalsSpinnerAdapter(
    context: Context
) : ArrayAdapter<Goal>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SpinnerItemGoalBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        binding.goal = getItem(position)!!
        return binding.root
    }
}
