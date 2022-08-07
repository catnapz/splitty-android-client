package dev.anshshukla.splitty

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class SettingsPageFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}