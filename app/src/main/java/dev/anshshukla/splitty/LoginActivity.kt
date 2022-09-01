package dev.anshshukla.splitty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dev.anshshukla.splitty.databinding.ActivityLoginBinding
import dev.anshshukla.splitty.utils.FormUtils
import dev.anshshukla.splitty.utils.FormValidationErrorCode


class LoginActivity : AppCompatActivity() {
    private val tag = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance(Firebase.app)

        val harmonizedColor = MaterialColors.harmonizeWithPrimary(
            this,
            resources.getColor(R.color.brand_primary, theme)
        )
        binding.loginButton.setBackgroundColor(harmonizedColor)

        initEmailLoginForm()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            handleSignedIn()
        }
    }

    private fun handleSignedIn() {
        // TODO: handle sign in bonuses https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
//        val metadata: FirebaseUserMetadata = user.getMetadata()
//        if (metadata.creationTimestamp == metadata.lastSignInTimestamp) {
//            // The user is new, show them a fancy intro screen!
//        } else {
//            // This is an existing user, show them a welcome back screen.
//        }
        val startMainActivityIntent = Intent(this, MainActivity::class.java)
        startMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(startMainActivityIntent)
        ActivityCompat.finishAffinity(this)
    }

    private fun validateForm(): Boolean {
        var valid = true

        when(FormUtils.validateEmail(binding.loginEmailField.text.toString())) {
            FormValidationErrorCode.BLANK -> {
                binding.loginEmailInputLayout.error = getString(R.string.required)
                valid = false
            }
            FormValidationErrorCode.EMAIL_MALFORMED -> {
                binding.loginEmailInputLayout.error = getString(R.string.malformed_email)
                valid = false
            }
            else ->
                binding.loginEmailInputLayout.error = null
        }

        when(FormUtils.validatePassword(binding.loginPasswordField.text.toString())) {
            FormValidationErrorCode.BLANK -> {
                binding.loginPasswordInputLayout.error = getString(R.string.required)
                valid = false
            }
            else ->
                binding.loginPasswordInputLayout.error = null
        }

        return valid
    }

    private fun setLoading(isLoading: Boolean = true) {
        if (isLoading) {
            binding.loginProgressIndicator.show()
            binding.loginButton.isEnabled = false
            binding.signInWithGoogleButton.isEnabled = false
        } else {
            binding.loginProgressIndicator.hide()
            binding.loginButton.isEnabled = true
            binding.signInWithGoogleButton.isEnabled = true
        }
    }

    private fun initEmailLoginForm() {
        binding.loginPasswordField.doOnTextChanged { _, _, _, _ ->
            binding.loginPasswordInputLayout.error = null
        }
        binding.loginEmailField.doOnTextChanged { _, _, _, _ ->
            binding.loginEmailInputLayout.error = null
        }
        binding.loginButton.setOnClickListener {
            if (validateForm()) {
                setLoading()
                auth.signInWithEmailAndPassword(
                    binding.loginEmailField.text.toString(),
                    binding.loginPasswordField.text.toString()
                )
                    .addOnCompleteListener {
                        setLoading(false)
                        if (it.isSuccessful) {
                            val user = auth.currentUser!!
                            Log.d(tag, "Logged in user: ${user.uid}")
                            handleSignedIn()
                        } else {
                            it.exception?.message?.let { errorMessage ->
                                Log.e(tag, "Login failure: $errorMessage")
                            }
                            Toast.makeText(
                                baseContext, R.string.sign_in_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}