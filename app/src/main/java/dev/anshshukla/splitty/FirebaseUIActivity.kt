package dev.anshshukla.splitty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class FirebaseUIActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // Choose authentication providers
    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
//        AuthUI.IdpConfig.PhoneBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
//        AuthUI.IdpConfig.FacebookBuilder().build(),
//        AuthUI.IdpConfig.TwitterBuilder().build()
    )

    private fun handleSignedIn() {
        // TODO: handle sign in bonuses https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md
//       val user = FirebaseAuth.getInstance().currentUser
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

    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            handleSignedIn()
        } else {
            initSignInFlow()
        }
    }

    private fun initSignInFlow() {
        AuthUI.getInstance().silentSignIn(this, providers)
            .continueWithTask { task ->
                if (task.isSuccessful) {
                    task
                } else {
                    // Ignore any exceptions since we don't care about credential fetch errors.
                    FirebaseAuth.getInstance().signInAnonymously()
                }
            }.addOnCompleteListener(this
            ) { task ->
                if (task.isSuccessful) {
                    handleSignedIn()
                } else {
                    // Uh oh, show error message
                }
            }

        val signInIntent = AuthUI.getInstance(FirebaseApp.getInstance())
            .createSignInIntentBuilder()
//            .setLogo()
//            .setTheme()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            handleSignedIn()
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button.
            val response = result.idpResponse
            if(response !== null) {
                val errorCode = response.error?.errorCode
            }
        }
    }
}