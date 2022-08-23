package dev.anshshukla.splitty.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.anshshukla.splitty.R
import dev.anshshukla.splitty.databinding.FragmentPageHolderBinding
import dev.anshshukla.splitty.pages.activity.ActivityPageFragment
import dev.anshshukla.splitty.pages.groups.GroupsPageFragment
import dev.anshshukla.splitty.pages.settings.SettingsPageFragment
import dev.anshshukla.splitty.pages.splits.SplitsPageFragment

/**
 * A simple [Fragment] subclass.
 * Use the [PageHolderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageHolderFragment : Fragment() {
    private lateinit var homePageTag: String
    private lateinit var otherPageTag: String

    private var currPageId = 0
    private var currBackStackCount = 0

    private lateinit var expenseFab: ExtendedFloatingActionButton
    private lateinit var bottomNavBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePageTag = getString(R.string.label_groups)
        otherPageTag = "OTHER_PAGE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_holder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseFab = view.findViewById(R.id.fab_add_expense)
        expenseFab.setOnClickListener { fab ->
                run {
                    Snackbar.make(
                        fab, currPageId.toString(),
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(R.id.fab_add_expense)
                        .setAction("Action", null).show()
                }
            }

        bottomNavBar = view.findViewById(R.id.bottom_navigation)
        bottomNavBar.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.page_groups -> {
                        setPage(GroupsPageFragment(), item.itemId)
                        true
                    }
                    R.id.page_splits -> {
                        setPage(SplitsPageFragment(), item.itemId)
                        true
                    }
                    R.id.page_activity -> {
                        setPage(ActivityPageFragment(), item.itemId)
                        true
                    }
                    R.id.page_settings -> {
                        setPage(SettingsPageFragment(), item.itemId)
                        true
                    }
                    else -> false
                }
            }
        setPage(GroupsPageFragment(), R.id.page_groups)
    }

    private fun setPage(page: Fragment, pageId: Int) {
        if (currPageId == pageId) return
        currPageId = pageId
        if (pageId == R.id.page_settings) {
            expenseFab.hide()
        } else {
            expenseFab.show()
        }

        val transaction = parentFragmentManager
            .beginTransaction()
            .replace(R.id.page_container, page, pageId.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .setReorderingAllowed(true)
        currBackStackCount = parentFragmentManager.backStackEntryCount
        if (pageId != R.id.page_groups) {
            transaction.addToBackStack(otherPageTag)
        }

        transaction.commit()
        if (currBackStackCount == 0) {
            parentFragmentManager.addOnBackStackChangedListener(object :
                FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    if (parentFragmentManager.backStackEntryCount <= currBackStackCount) {
                        // backstack was popped, empty back stack and navigate to home page
                        parentFragmentManager.popBackStack(
                            otherPageTag,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )
                        parentFragmentManager.removeOnBackStackChangedListener(this)
                        bottomNavBar.menu.getItem(0).isChecked = true
                        currPageId = R.id.page_groups
                        expenseFab.show()
                    }
                }
            })
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment PageHolderFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            PageHolderFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}