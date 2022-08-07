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
 * Use the [SplitsPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplitsPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splits_page, container, false)
        view.findViewById<FloatingActionButton>(R.id.fab_splits).setOnClickListener { fab ->
            run {
                Snackbar.make(
                    fab, R.string.label_groups,
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(R.id.fab_splits)
                    .setAction("Action", null).show()
            }
        }
        return view;
    }
}