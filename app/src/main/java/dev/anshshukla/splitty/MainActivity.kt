package dev.anshshukla.splitty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.anshshukla.splitty.databinding.ActivityMainBinding
import dev.anshshukla.splitty.pages.groups.GroupsPageFragment
import dev.anshshukla.splitty.pages.splits.SplitsPageFragment
import dev.anshshukla.splitty.pages.activity.ActivityPageFragment
import dev.anshshukla.splitty.pages.settings.SettingsPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var homePageTag: String
    private lateinit var otherPageTag: String

    private var currPageId = R.id.page_groups
    private var currBackStackCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        homePageTag = getString(R.string.label_groups)
        otherPageTag = "OTHER_PAGE"

        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fabAddExpense.setOnClickListener { fab ->
            run {
                Snackbar.make(
                    fab, currPageId.toString(),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(R.id.fab_add_expense)
                    .setAction("Action", null).show()
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPage(page: Fragment, pageId: Int) {
        currPageId = pageId
        if(pageId == R.id.page_settings) {
            binding.fabAddExpense.hide()
        } else {
            binding.fabAddExpense.show()
        }

        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.page_container, page, pageId.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .setReorderingAllowed(true)
        currBackStackCount = supportFragmentManager.backStackEntryCount
        if(pageId != R.id.page_groups) {
            transaction.addToBackStack(otherPageTag)
        }

        transaction.commit()
        if(currBackStackCount == 0) {
            supportFragmentManager.addOnBackStackChangedListener(object :
                FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    if(supportFragmentManager.backStackEntryCount <= currBackStackCount) {
                        // backstack was popped, empty back stack and navigate to home page
                        supportFragmentManager.popBackStack(otherPageTag, POP_BACK_STACK_INCLUSIVE)
                        supportFragmentManager.removeOnBackStackChangedListener(this)
                        binding.bottomNavigation.menu.getItem(0).isChecked = true
                        currPageId = R.id.page_groups
                        binding.fabAddExpense.show()
                    }
                }
            })
        }
    }

}