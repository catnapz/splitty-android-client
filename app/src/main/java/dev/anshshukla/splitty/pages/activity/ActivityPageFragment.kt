package dev.anshshukla.splitty.pages.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.anshshukla.splitty.R

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ActivityPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.page_activity, container, false)
    }
}