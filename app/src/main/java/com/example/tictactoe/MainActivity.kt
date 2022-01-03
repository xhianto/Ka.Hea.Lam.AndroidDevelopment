package com.example.tictactoe

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.OnCompleteListener

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var buttonSignIn: SignInButton
    lateinit var textLoginNotice: TextView
    lateinit var settingsImageButton: ImageButton
    lateinit var buttonSignOut: Button
    lateinit var buttonEasy: Button
    lateinit var buttonHard: Button
    lateinit var buttonP1vsP2: Button
    lateinit var user: User

    private var launchLoginActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSignIn = findViewById(R.id.sign_in_button)
        buttonSignIn.setOnClickListener(this)
        buttonSignOut = findViewById(R.id.signout)
        buttonSignOut.setOnClickListener(this)
        buttonEasy = findViewById(R.id.easy)
        buttonEasy.setOnClickListener(this)
        buttonHard = findViewById(R.id.hard)
        buttonHard.setOnClickListener(this)
        buttonP1vsP2 = findViewById(R.id.p1vsp2)
        buttonP1vsP2.setOnClickListener(this)
        settingsImageButton = findViewById(R.id.settingsButton)
        settingsImageButton.setOnClickListener(this)
        textLoginNotice = findViewById(R.id.TextLoginNotice)
        textLoginNotice.text = getString(R.string.no_login)


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignIn.getLastSignedInAccount(this)
        signIn()
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        launchLoginActivity.launch(signInIntent)
        //startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            user = User(-1, account.displayName, account.email)
            buttonSignIn.visibility = View.INVISIBLE
            textLoginNotice.visibility = View.INVISIBLE
            buttonEasy.visibility = View.VISIBLE
            buttonHard.visibility = View.VISIBLE
            buttonP1vsP2.visibility = View.VISIBLE
            buttonSignOut.visibility = View.VISIBLE
            settingsImageButton.visibility = View.VISIBLE
            // Signed in successfully, show authenticated UI.
            //updateUI(account)
            textLoginNotice.text = account.displayName
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
            textLoginNotice.text = e.statusCode.toString()
        }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                buttonSignIn.visibility = View.VISIBLE
                textLoginNotice.visibility = View.VISIBLE
                buttonEasy.visibility = View.INVISIBLE
                buttonHard.visibility = View.INVISIBLE
                buttonP1vsP2.visibility = View.INVISIBLE
                buttonSignOut.visibility = View.INVISIBLE
                settingsImageButton.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity, "Signed Out Successfully", Toast.LENGTH_LONG)
                    .show()
            }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> signIn()
            R.id.signout -> signOut()
        }
    }
}