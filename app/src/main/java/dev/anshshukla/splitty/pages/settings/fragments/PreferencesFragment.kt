package dev.anshshukla.splitty.pages.settings.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dev.anshshukla.splitty.LoginActivity
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

                FirebaseAuth.getInstance(Firebase.app)
                    .signOut()
                startActivity(Intent(context, LoginActivity::class.java))
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