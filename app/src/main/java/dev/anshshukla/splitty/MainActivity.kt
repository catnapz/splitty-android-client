package dev.anshshukla.splitty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.material.color.DynamicColors
import com.google.firebase.auth.FirebaseAuth
import dev.anshshukla.splitty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_groups -> {
                    Log.println(Log.DEBUG, "main", "groups")
                    Toast.makeText(this, R.string.label_groups, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.page_splits -> {
                    Log.println(Log.DEBUG, "main", "splits")
                    Toast.makeText(this, R.string.label_splits, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.page_history -> {
                    Log.println(Log.DEBUG, "main", "history")
                    Toast.makeText(this, R.string.label_history, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.fab.setOnClickListener { view ->
            run {
                Snackbar.make(
                    view,
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "uhm",
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorView(R.id.fab)
                    .setAction("Action", null).show()
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener {
                        run {
                            // user is now signed out
                            startActivity(Intent(this, FirebaseUIActivity::class.java))
                            finish()
                        }
                    }
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}