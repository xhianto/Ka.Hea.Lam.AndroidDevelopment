package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class LoginRegisterActivity : AppCompatActivity() {
    lateinit var mode: String
    lateinit var editUsername: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editRepeatPassword: EditText
    lateinit var textUsername: TextView
    lateinit var textRepeatPassword: TextView
    lateinit var textTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        val bundle = intent.extras
        mode = bundle?.getString("mode").toString()

        editUsername = findViewById(R.id.editUsername)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        editRepeatPassword = findViewById(R.id.editRepeatPassword)
        textUsername = findViewById(R.id.username)
        textRepeatPassword = findViewById(R.id.repeatPassword)
        textTitle = findViewById(R.id.titel)

        if (mode == "login") {
            textUsername.visibility = View.INVISIBLE
            textRepeatPassword.visibility = View.INVISIBLE
            editUsername.visibility = View.INVISIBLE
            editRepeatPassword.visibility = View.INVISIBLE
            textTitle.text = getString(R.string.login)
        }
        else {
            textTitle.text = getString(R.string.register)
        }
    }
}