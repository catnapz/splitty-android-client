package dev.anshshukla.splitty.pages.splits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.anshshukla.splitty.R

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
        return inflater.inflate(R.layout.page_splits, container, false)
    }
}