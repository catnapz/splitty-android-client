package dev.anshshukla.splitty.pages.settings.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.firebase.ui.auth.AuthUI
import dev.anshshukla.splitty.FirebaseUIActivity
import dev.anshshukla.splitty.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (findPreference("logout") as Preference?)?.setOnPreferenceClickListener {
            context?.let { context ->
                AuthUI.getInstance()
                    .signOut(context)
                    .addOnCompleteListener {
                        // user is now signed out
                        startActivity(Intent(context, FirebaseUIActivity::class.java))
                    }
                true
            }
            false
        }

    }
}