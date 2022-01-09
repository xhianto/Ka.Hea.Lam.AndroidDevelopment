package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button

// Philipp Lackner, FRAGMENTS - Android Fundamentals
// https://www.youtube.com/watch?v=-vAI7RSPxOA&ab_channel=PhilippLackner
// Geraadpleegd op 6 jan
class LoggedInFragment : Fragment(R.layout.fragment_logged_in), View.OnClickListener {

    private lateinit var dataPasser: OnFragmentDataPass
    private lateinit var buttonSignOut: Button
    private lateinit var buttonEasy: Button
    private lateinit var buttonHard: Button
    private lateinit var buttonP1vsP2: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnFragmentDataPass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSignOut = view.findViewById(R.id.signOut)
        buttonSignOut.setOnClickListener(this)
        buttonEasy = view.findViewById(R.id.easy)
        buttonEasy.setOnClickListener(this)
        buttonHard = view.findViewById(R.id.hard)
        buttonHard.setOnClickListener(this)
        buttonP1vsP2 = view.findViewById(R.id.p1vsp2)
        buttonP1vsP2.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.signOut -> fragmentDataPass("signOut")
            R.id.easy -> fragmentDataPass("easy")
            R.id.hard -> fragmentDataPass("hard")
            R.id.p1vsp2 -> fragmentDataPass("p1vsp2")
        }
    }

    // Harlo Holmes, Passing data between a fragment and its container activity
    // https://stackoverflow.com/a/9977370
    // Geraadpleegd op 9 jan 2022
    private fun fragmentDataPass(action: String) {
        dataPasser.onFragmentDataPass(action)
    }
}