package dev.anshshukla.splitty.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
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
import dev.anshshukla.splitty.viewmodel.PageViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PageHolderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PageHolderFragment : Fragment() {
    private val pageViewModel: PageViewModel by activityViewModels()

    private lateinit var homePageTag: String
    private lateinit var otherPageTag: String

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
                        fab, pageViewModel.selectedPageId.value.toString(),
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
                        setPage(GroupsPageFragment(), R.id.page_groups)
                        true
                    }
                    R.id.page_splits -> {
                        setPage(SplitsPageFragment(), R.id.page_splits)
                        true
                    }
                    R.id.page_activity -> {
                        setPage(ActivityPageFragment(), R.id.page_activity)
                        true
                    }
                    R.id.page_settings -> {
                        setPage(SettingsPageFragment(), R.id.page_settings)
                        true
                    }
                    else -> false
                }
            }
        setPage(GroupsPageFragment(), R.id.page_groups)
    }

    private fun setPage(page: Fragment, pageId: Int) {
        if (pageViewModel.selectedPageId.value == pageId) return
        pageViewModel.setPageId(pageId)
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
                        pageViewModel.setPageId(R.id.page_groups)
                        expenseFab.show()
                    }
                }
            })
        }
    }
}