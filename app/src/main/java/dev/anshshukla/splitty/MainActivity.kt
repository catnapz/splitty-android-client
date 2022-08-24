package dev.anshshukla.splitty

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.google.android.material.color.DynamicColors
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import dev.anshshukla.splitty.databinding.ActivityMainBinding
import dev.anshshukla.splitty.viewmodel.PageViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val pageViewModel: PageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        when(pageViewModel.selectedPageId.value){
            R.id.page_groups -> binding.toolbar.title = getString(R.string.label_groups)
            R.id.page_splits -> binding.toolbar.title = getString(R.string.label_splits)
            R.id.page_activity -> binding.toolbar.title = getString(R.string.label_activity)
            R.id.page_settings -> binding.toolbar.title = getString(R.string.label_settings)
            else -> binding.toolbar.title = getString(R.string.app_name)
        }
        if (pageViewModel.selectedPageId.value == R.id.page_settings) {
            menu.findItem(R.id.action_settings).isVisible = false
        } else {
            // testing
            menu.getItem(0).title = pageViewModel.selectedPageId.value.toString()
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        invalidateOptionsMenu()
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}