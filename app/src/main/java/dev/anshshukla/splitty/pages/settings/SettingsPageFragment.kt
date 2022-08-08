package dev.anshshukla.splitty.pages.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dev.anshshukla.splitty.FirebaseUIActivity
import dev.anshshukla.splitty.R
import dev.anshshukla.splitty.pages.settings.fragments.UserSettingsDialogFragment

class SettingsPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.page_settings, container, false)
        setupUserSettingsCard(view)

        view.findViewById<LinearLayout>(R.id.logoutButton).setOnClickListener {
            context?.let { context ->
                AuthUI.getInstance().signOut(context)
                    .addOnCompleteListener {
                        // user is now signed out
                        startActivity(Intent(context, FirebaseUIActivity::class.java))
                    }
            }
        }

        return view;
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
            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
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