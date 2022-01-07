package com.example.tictactoe

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.SignInButton

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var buttonSignIn: SignInButton
    private lateinit var buttonSignOut: Button
    private lateinit var buttonEasy: Button
    private lateinit var buttonHard: Button
    private lateinit var buttonP1vsP2: Button
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var user: User

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
        buttonLogin = findViewById(R.id.login)
        buttonLogin.setOnClickListener(this)
        buttonRegister = findViewById(R.id.register)
        buttonRegister.setOnClickListener(this)
        user = User()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /*override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignIn.getLastSignedInAccount(this)
        signIn()
    }*/

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        launchLoginActivity.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val dbService = DataBaseService(this@MainActivity)
            user = dbService.getUserByEmail(account.email.toString())
            if (user.emailAddress == null) {
                val intentLogin = Intent(this@MainActivity, LoginRegisterActivity::class.java)
                intentLogin.putExtra("mode", "googleRegister")
                intentLogin.putExtra("username", account.displayName)
                intentLogin.putExtra("email", account.email)
                getLoginRegisterResult.launch(intentLogin)
            }
            gameButtons()
            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                signInOutButtons()
                Toast.makeText(this@MainActivity, "Signed Out Successfully", Toast.LENGTH_LONG).show()
            }
    }

    override fun onClick(view: View) {
        val intentLogin = Intent(this@MainActivity, LoginRegisterActivity::class.java)
        val intentGame = Intent(this@MainActivity, GameActivity::class.java)
        intentGame.putExtra("user", user)

        when (view.id) {
            R.id.sign_in_button -> googleSignIn()
            R.id.signout -> signOut()
            R.id.login -> {
                intentLogin.putExtra("mode", "login")
                getLoginRegisterResult.launch(intentLogin)
            }
            R.id.register -> {
                intentLogin.putExtra("mode", "register")
                getLoginRegisterResult.launch(intentLogin)
            }
            R.id.easy -> {
                intentGame.putExtra("mode", "easy")
                startActivity(intentGame)
            }
            R.id.hard -> {
                intentGame.putExtra("mode", "hard")
                startActivity(intentGame)
            }
            R.id.p1vsp2 -> {
                intentGame.putExtra("mode", "p1vsp2")
                startActivity(intentGame)
            }
        }
    }

    private fun gameButtons() {
        buttonSignIn.visibility = View.INVISIBLE
        buttonEasy.visibility = View.VISIBLE
        buttonHard.visibility = View.VISIBLE
        buttonP1vsP2.visibility = View.VISIBLE
        buttonSignOut.visibility = View.VISIBLE
    }

    private fun signInOutButtons() {
        buttonSignIn.visibility = View.VISIBLE
        buttonEasy.visibility = View.INVISIBLE
        buttonHard.visibility = View.INVISIBLE
        buttonP1vsP2.visibility = View.INVISIBLE
        buttonSignOut.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intentSettings = Intent(this@MainActivity, SettingsActivity::class.java)
            if (user.emailAddress != null)
                intentSettings.putExtra("user", user)
            startActivity(intentSettings)
        }
        return true
    }
}