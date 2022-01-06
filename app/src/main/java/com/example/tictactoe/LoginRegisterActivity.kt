package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent




class LoginRegisterActivity : AppCompatActivity() {
    lateinit var mode: String
    lateinit var username: String
    lateinit var email: String
    lateinit var editUsername: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editRepeatPassword: EditText
    lateinit var textUsername: TextView
    lateinit var textRepeatPassword: TextView
    lateinit var textTitle: TextView
    lateinit var buttonSubmit: Button
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        mode = bundle?.getString("mode").toString()
        username = bundle?.getString("username").toString()
        email = bundle?.getString("email").toString()

        editUsername = findViewById(R.id.editUsername)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        editRepeatPassword = findViewById(R.id.editRepeatPassword)
        textUsername = findViewById(R.id.username)
        //textRepeatPassword = findViewById(R.id.repeatPassword)
        textTitle = findViewById(R.id.title)
        buttonSubmit = findViewById(R.id.submit)

        if (mode == "login") {
            textUsername.visibility = View.INVISIBLE
            //textRepeatPassword.visibility = View.INVISIBLE
            editUsername.visibility = View.INVISIBLE
            editRepeatPassword.visibility = View.INVISIBLE
            textTitle.text = getString(R.string.login)
            actionBar.title = getString(R.string.login)
            buttonSubmit.text = getString(R.string.login)
        }
        else {
            textTitle.text = getString(R.string.register)
            actionBar.title = getString(R.string.register)
            buttonSubmit.text = getString(R.string.register)
            if (mode == "googleRegister"){
                editEmail.setText(email)
                editEmail.isEnabled = false
                editUsername.setText(username)
            }

        }

        buttonSubmit.setOnClickListener {
            if (editUsername.text.isEmpty() && editUsername.visibility == View.VISIBLE)
                Toast.makeText(this@LoginRegisterActivity, "Username cannot be empty", Toast.LENGTH_LONG).show()
            else if (editEmail.text.isEmpty())
                Toast.makeText(this@LoginRegisterActivity, "Email cannot be empty", Toast.LENGTH_LONG).show()
            else if (editPassword.text.isEmpty())
                Toast.makeText(this@LoginRegisterActivity, "Password cannot be empty", Toast.LENGTH_LONG).show()
            else if (editPassword.text.toString() != editRepeatPassword.text.toString() && editRepeatPassword.visibility == View.VISIBLE)
                Toast.makeText(this@LoginRegisterActivity, "Repeat Password is not the same", Toast.LENGTH_LONG).show()
            else {
                val dbService = DataBaseService(this@LoginRegisterActivity)
                user = dbService.getUserByEmail(editEmail.text.toString())
                if (mode == "login") {
                    if (user.emailAddress == null)
                        Toast.makeText(this@LoginRegisterActivity, "User nog found", Toast.LENGTH_LONG).show()
                    else if (editPassword.text.toString() != user.password)
                        Toast.makeText(this@LoginRegisterActivity, "Wrong password", Toast.LENGTH_LONG).show()
                    else {
                        // successful login
                        // send user info back to mainpage and change buttons...
                        loginUser()
                    }
                }
                else {
                    if (user.emailAddress != null)
                        Toast.makeText(this@LoginRegisterActivity, "Email already in use", Toast.LENGTH_LONG).show()
                    else {
                        user = User(-1, editUsername.text.toString(), editEmail.text.toString(), editPassword.text.toString())
                        dbService.addUser(user)
                        Toast.makeText(this@LoginRegisterActivity, "New User Added", Toast.LENGTH_LONG).show()
                        loginUser()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loginUser() {
        val data = Intent()
        data.putExtra("user", user)
        setResult(RESULT_OK, data)
        finish()
    }
    /*override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }*/
}