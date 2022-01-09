package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.google.android.gms.common.SignInButton

class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private lateinit var buttonSignIn: SignInButton
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var dataPasser: OnFragmentDataPass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnFragmentDataPass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogin = view.findViewById(R.id.login)
        buttonLogin.setOnClickListener(this)
        buttonRegister = view.findViewById(R.id.register)
        buttonRegister.setOnClickListener(this)
        buttonSignIn = view.findViewById(R.id.sign_in_button)
        buttonSignIn.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.login -> fragmentDataPass("login")
            R.id.register -> fragmentDataPass("register")
            R.id.sign_in_button -> fragmentDataPass("google")
        }
    }

    private fun fragmentDataPass(action: String) {
        dataPasser.onFragmentDataPass(action)
    }
}