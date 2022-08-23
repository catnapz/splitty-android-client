package dev.anshshukla.splitty.pages.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import dev.anshshukla.splitty.GlideApp
import dev.anshshukla.splitty.R
import dev.anshshukla.splitty.pages.settings.fragments.PreferencesFragment
import dev.anshshukla.splitty.pages.settings.fragments.UserSettingsDialogFragment


class SettingsPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.page_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.preferencesFragmentContainer, PreferencesFragment())
            .commit()
        setupUserSettingsCard(view)
    }

    private fun setupUserSettingsCard(view: View) {
        val user = FirebaseAuth.getInstance().currentUser!!
        val userSettingsView = view.findViewById<MaterialCardView>(R.id.userSettings)
        userSettingsView.findViewById<TextView>(R.id.username).text =
            user.displayName ?: user.email ?: user.phoneNumber ?: ""

        userSettingsView.findViewById<TextView>(R.id.email).text =
            user.email ?: user.phoneNumber ?: ""

        userSettingsView.findViewById<Button>(R.id.profileEditButton).setOnClickListener {
            showEditUserSettingsDialog()
        }

        if (user.photoUrl !== null) {
            GlideApp
                .with(view.context)
                .load(user.photoUrl)
                .override(100, 100)
                .centerCrop()
                .into(userSettingsView.findViewById<ShapeableImageView>(R.id.userDisplayPicture))
        }
    }

    private fun showEditUserSettingsDialog() {
        val fragmentManager = parentFragmentManager
        val dialog = UserSettingsDialogFragment()
        if (resources.getBoolean(R.bool.large_layout)) {
            // The device is using a large layout, so show the fragment as a dialog
            dialog.show(fragmentManager, "dialog")
        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction
                .add(android.R.id.content, dialog)
                .addToBackStack("userEditDialog")
                .commit()
        }
    }
}