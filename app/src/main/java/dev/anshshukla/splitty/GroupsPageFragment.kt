package dev.anshshukla.splitty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class GroupsPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_groups_page, container, false)
        view.findViewById<FloatingActionButton>(R.id.fab_groups).setOnClickListener { fab ->
            run {
                Snackbar.make(
                    fab, R.string.label_groups,
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(R.id.fab_groups)
                    .setAction("Action", null).show()
            }
        }
        return view;
    }
}