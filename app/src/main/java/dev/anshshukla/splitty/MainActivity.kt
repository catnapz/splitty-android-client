package dev.anshshukla.splitty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.color.DynamicColors
import dev.anshshukla.splitty.databinding.ActivityMainBinding
import dev.anshshukla.splitty.pages.groups.GroupsPageFragment
import dev.anshshukla.splitty.pages.splits.SplitsPageFragment
import dev.anshshukla.splitty.pages.activity.ActivityPageFragment
import dev.anshshukla.splitty.pages.settings.SettingsPageFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_groups -> {
                    replaceFragment(GroupsPageFragment(), getString(R.string.label_groups))
                    true
                }
                R.id.page_splits -> {
                    replaceFragment(SplitsPageFragment(), getString(R.string.label_splits))
                    true
                }
                R.id.page_activity -> {
                    replaceFragment(ActivityPageFragment(), getString(R.string.label_activity))
                    true
                }
                R.id.page_settings -> {
                    replaceFragment(SettingsPageFragment(), getString(R.string.label_settings))
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

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.page_container, fragment, tag)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(tag)
            .commit()
    }
}