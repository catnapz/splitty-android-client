package dev.anshshukla.splitty.pages.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import dev.anshshukla.splitty.databinding.PageSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import dev.anshshukla.splitty.GlideApp
import dev.anshshukla.splitty.R
import dev.anshshukla.splitty.pages.settings.fragments.PreferencesFragment
import dev.anshshukla.splitty.pages.settings.fragments.UserSettingsDialogFragment


class SettingsPageFragment : Fragment() {
    private var _binding: PageSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.preferencesFragmentContainer, PreferencesFragment())
            .commit()
        setupUserSettingsCard()
    }

    private fun setupUserSettingsCard() {
        val user = FirebaseAuth.getInstance().currentUser!!

        binding.username.text =
            user.displayName ?: user.email ?: user.phoneNumber ?: ""

        binding.email.text =
            user.email ?: user.phoneNumber ?: ""

        binding.profileEditButton.setOnClickListener {
            showEditUserSettingsDialog()
        }

        if (user.photoUrl !== null) {
            GlideApp
                .with(this)
                .load(user.photoUrl)
                .override(100, 100)
                .centerCrop()
                .into(binding.userDisplayPicture)
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