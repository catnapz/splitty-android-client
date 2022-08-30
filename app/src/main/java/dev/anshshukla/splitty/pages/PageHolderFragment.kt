package dev.anshshukla.splitty.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.anshshukla.splitty.R
import dev.anshshukla.splitty.databinding.FragmentPageHolderBinding
import dev.anshshukla.splitty.pages.activity.ActivityPageFragment
import dev.anshshukla.splitty.pages.groups.GroupsPageFragment
import dev.anshshukla.splitty.pages.settings.SettingsPageFragment
import dev.anshshukla.splitty.pages.splits.SplitsPageFragment
import dev.anshshukla.splitty.viewmodel.PageViewModel

/**
 * Page holder contains the views below the action bar
 */
class PageHolderFragment : Fragment() {
    private var _binding: FragmentPageHolderBinding? = null
    private val pageViewModel: PageViewModel by activityViewModels()

    private lateinit var homePageTag: String
    private lateinit var otherPageTag: String

    private var currBackStackCount = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homePageTag = getString(R.string.label_groups)
        otherPageTag = "OTHER_PAGE"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPageHolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.action_add_expense)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
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
        setPage(GroupsPageFragment(), pageViewModel.selectedPageId.value ?: R.id.page_groups)
    }

    private fun setPage(page: Fragment, pageId: Int) {
        if (pageViewModel.selectedPageId.value == pageId) return
        pageViewModel.setPageId(pageId)
        if (pageId == R.id.page_settings) {
            binding.fabAddExpense.hide()
        } else {
            binding.fabAddExpense.show()
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
                        binding.bottomNavigation.menu.getItem(0).isChecked = true
                        pageViewModel.setPageId(R.id.page_groups)
                        binding.fabAddExpense.show()
                    }
                }
            })
        }
    }
}