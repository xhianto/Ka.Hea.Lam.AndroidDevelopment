package com.example.tictactoe

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException

class MainActivity : AppCompatActivity(), OnFragmentDataPass {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var fragment: FrameLayout
    private lateinit var imageButtonSettings: ImageButton
    private lateinit var user: User
    private lateinit var dataBaseService: DataBaseService

    private val loginFragment = LoginFragment()
    private val loggedInFragment = LoggedInFragment()

    private var launchLoginActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private var getLoginRegisterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            user = result.data?.getSerializableExtra("user") as User
            gameButtons()
        }
        else
            signInOutButtons()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataBaseService = DataBaseService(this@MainActivity)
        fragment = findViewById(R.id.flFragment)
        imageButtonSettings = findViewById(R.id.settingsButton)
        signInOutButtons()
        user = User()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        imageButtonSettings.setOnClickListener {
            val intentSettings = Intent(this@MainActivity, SettingsActivity::class.java)
            intentSettings.putExtra("user", user)
            getLoginRegisterResult.launch(intentSettings)
        }
    }

    override fun onResume() {
        super.onResume()
        if (user.emailAddress != null)
            user = dataBaseService.getUserByEmail(user.emailAddress.toString())
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        launchLoginActivity.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            user = dataBaseService.getUserByEmail(account.email.toString())
            if (user.emailAddress == null) {
                val intentLogin = Intent(this@MainActivity, LoginRegisterActivity::class.java)
                intentLogin.putExtra("mode", "googleRegister")
                intentLogin.putExtra("username", account.displayName)
                intentLogin.putExtra("email", account.email)
                getLoginRegisterResult.launch(intentLogin)
            }
            // Signed in successfully, show authenticated UI.
            mGoogleSignInClient.signOut()
            gameButtons()

        }
        catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this@MainActivity, R.string.error_google_sign_in + e.statusCode, Toast.LENGTH_LONG).show()
        }
    }

    private fun signOut() {
        signInOutButtons()
        Toast.makeText(
            this@MainActivity,
            R.string.message_successful_sign_out,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onFragmentDataPass(action: String) {
        val intentLogin = Intent(this@MainActivity, LoginRegisterActivity::class.java)
        val intentGame = Intent(this@MainActivity, GameActivity::class.java)
        intentGame.putExtra("user", user)

        when (action) {
            "google"-> googleSignIn()
            "signOut" -> signOut()
            "login" -> {
                intentLogin.putExtra("mode", "login")
                getLoginRegisterResult.launch(intentLogin)
            }
            "register" -> {
                intentLogin.putExtra("mode", "register")
                getLoginRegisterResult.launch(intentLogin)
            }
            "easy" -> {
                intentGame.putExtra("mode", "easy")
                getLoginRegisterResult.launch(intentGame)
            }
            "hard" -> {
                intentGame.putExtra("mode", "hard")
                getLoginRegisterResult.launch(intentGame)
            }
            "p1vsp2" -> {
                intentGame.putExtra("mode", "p1vsp2")
                getLoginRegisterResult.launch(intentGame)
            }
        }
    }

    private fun gameButtons() {
        imageButtonSettings.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, loggedInFragment)
            commit()
        }
    }

    private fun signInOutButtons() {
        imageButtonSettings.visibility = View.INVISIBLE
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, loginFragment)
            commit()
        }
    }
}