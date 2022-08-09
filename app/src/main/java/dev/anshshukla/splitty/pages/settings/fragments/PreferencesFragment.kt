package dev.anshshukla.splitty.pages.settings.fragments

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import dev.anshshukla.splitty.R

class PreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.overScrollMode = View.OVER_SCROLL_NEVER
    }
}