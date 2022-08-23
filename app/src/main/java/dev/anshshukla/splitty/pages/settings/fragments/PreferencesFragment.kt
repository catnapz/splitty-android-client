package dev.anshshukla.splitty.pages.settings.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import com.firebase.ui.auth.AuthUI
import dev.anshshukla.splitty.FirebaseUIActivity
import dev.anshshukla.splitty.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isDark = false
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> isDark = true
            Configuration.UI_MODE_NIGHT_NO -> isDark = false
        }

        tintIcons(preferenceScreen, isDark)
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

    private fun tintIcons(preference: Preference, isDark: Boolean) {
        if (preference is PreferenceGroup) {
            val group: PreferenceGroup = preference
            for (i in 0 until group.preferenceCount) {
                tintIcons(group.getPreference(i), isDark)
            }
        } else {
            val icon = preference.icon
            if (icon != null) {
                DrawableCompat.setTint(
                    icon,
                    resources.getColor(
                        if (isDark) R.color.white else R.color.black,
                        resources.newTheme()
                    )
                )
            }
        }
    }
}