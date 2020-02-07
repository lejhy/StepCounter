package strathclyde.emb15144.stepcounter.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import strathclyde.emb15144.stepcounter.R

class GoalsFragment : Fragment() {

    private lateinit var goalsViewModel: GoalsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        goalsViewModel =
                ViewModelProviders.of(this).get(GoalsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_goals, container, false)
//        val textView: TextView = root.findViewById(R.id.text_goals)
//        goalsViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        val array = arrayOf("Item1", "Item2", "Item3")
        viewAdapter = GoalsListAdapter(array)

        recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view_goals).apply {
            adapter = viewAdapter
        }

//        val adapter = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, array)
//        listView = root.findViewById(R.id.recycler_view_goals)
//        listView.adapter = adapter;

        return root
    }
}
