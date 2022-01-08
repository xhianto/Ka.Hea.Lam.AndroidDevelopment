package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.math.BigDecimal
import java.math.RoundingMode

class SettingsActivity : AppCompatActivity() {

    private lateinit var dataBaseService: DataBaseService
    private lateinit var userName: TextView
    private lateinit var emailAddress: TextView
    private lateinit var easyWin: TextView
    private lateinit var easyDraw: TextView
    private lateinit var easyLose: TextView
    private lateinit var easyTotal: TextView
    private lateinit var easyWinPercentage: TextView
    private lateinit var hardWin: TextView
    private lateinit var hardDraw: TextView
    private lateinit var hardLose: TextView
    private lateinit var hardTotal: TextView
    private lateinit var hardWinPercentage: TextView
    private lateinit var friendWin: TextView
    private lateinit var friendDraw: TextView
    private lateinit var friendLose: TextView
    private lateinit var friendTotal: TextView
    private lateinit var friendWinPercentage: TextView
    private lateinit var totalWin: TextView
    private lateinit var totalDraw: TextView
    private lateinit var totalLose: TextView
    private lateinit var totalTotal: TextView
    private lateinit var totalWinPercentage: TextView
    private lateinit var changeNameButton: ImageButton
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        user = intent.getSerializableExtra("user") as User

        val userEasyWin: Int = user.easyWin
        val userEasyDraw: Int = user.easyDraw
        val userEasyLose: Int = user.easyLose
        val userEasyTotal = userEasyWin + userEasyDraw + userEasyLose
        val userEasyWinPercentage: BigDecimal = calcWinPercentage(userEasyWin, userEasyTotal)
        val userHardWin: Int = user.hardWin
        val userHardDraw: Int = user.hardDraw
        val userHardLose: Int = user.hardLose
        val userHardTotal = userHardWin + userHardDraw + userHardLose
        val userHardWinPercentage: BigDecimal = calcWinPercentage(userHardWin, userHardTotal)
        val userFriendWin: Int = user.friendWin
        val userFriendDraw: Int = user.friendDraw
        val userFriendLose: Int = user.friendLose
        val userFriendTotal = userFriendWin + userFriendDraw + userFriendLose
        val userFriendWinPercentage: BigDecimal = calcWinPercentage(userFriendWin, userFriendTotal)
        val userTotalWin = userEasyWin + userHardWin + userFriendWin
        val userTotalDraw = userEasyDraw + userHardDraw + userFriendDraw
        val userTotalLose = userEasyLose + userHardLose + userFriendLose
        val userTotalTotal = userEasyTotal + userHardTotal + userFriendTotal
        val userTotalWinPercentage: BigDecimal = calcWinPercentage(userTotalWin, userTotalTotal)

        userName = findViewById(R.id.userUserName)
        emailAddress = findViewById(R.id.userEmailAddress)

        dataBaseService = DataBaseService(this@SettingsActivity)

        easyWin = findViewById(R.id.easyWin)
        easyDraw = findViewById(R.id.easyDraw)
        easyLose = findViewById(R.id.easyLose)
        easyTotal = findViewById(R.id.easyTotal)
        easyWinPercentage = findViewById(R.id.easyWinPercentage)
        hardWin = findViewById(R.id.hardWin)
        hardDraw = findViewById(R.id.hardDraw)
        hardLose = findViewById(R.id.hardLose)
        hardTotal = findViewById(R.id.hardTotal)
        hardWinPercentage = findViewById(R.id.hardWinPercentage)
        friendWin = findViewById(R.id.friendWin)
        friendDraw = findViewById(R.id.friendDraw)
        friendLose = findViewById(R.id.friendLose)
        friendTotal = findViewById(R.id.friendTotal)
        friendWinPercentage = findViewById(R.id.friendWinPercentage)
        totalWin = findViewById(R.id.totalWin)
        totalDraw = findViewById(R.id.totalDraw)
        totalLose = findViewById(R.id.totalLose)
        totalTotal = findViewById(R.id.totalTotal)
        totalWinPercentage = findViewById(R.id.totalWinPercentage)

        changeNameButton = findViewById(R.id.changeNameButton)
        changeNameButton.setOnClickListener {
            changeName()
        }

        userName.text = user.username
        emailAddress.text = user.emailAddress

        easyWin.text = userEasyWin.toString()
        easyDraw.text = userEasyDraw.toString()
        easyLose.text = userEasyLose.toString()
        easyTotal.text = userEasyTotal.toString()
        easyWinPercentage.text = String.format("%.2f", userEasyWinPercentage)
        hardWin.text = userHardWin.toString()
        hardDraw.text = userHardDraw.toString()
        hardLose.text = userHardLose.toString()
        hardTotal.text = userHardTotal.toString()
        hardWinPercentage.text = String.format("%.2f", userHardWinPercentage)
        friendWin.text = userFriendWin.toString()
        friendDraw.text = userFriendDraw.toString()
        friendLose.text = userFriendLose.toString()
        friendTotal.text = userFriendTotal.toString()
        friendWinPercentage.text = String.format("%.2f", userFriendWinPercentage)
        totalWin.text = userTotalWin.toString()
        totalDraw.text = userTotalDraw.toString()
        totalLose.text = userTotalLose.toString()
        totalTotal.text = userTotalTotal.toString()
        totalWinPercentage.text = String.format("%.2f",userTotalWinPercentage)
    }

    private fun calcWinPercentage(win: Int, total: Int): BigDecimal {
        if (total != 0) {
            return BigDecimal.valueOf(win.toLong()).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(total.toLong()), 2, RoundingMode.HALF_UP)
        }
        return BigDecimal.ZERO
    }

    private fun changeName() {
        val changeNameDialog = AlertDialog.Builder(this@SettingsActivity).create()
        changeNameDialog.setTitle("Change your username")
        val input = EditText(this)
        changeNameDialog.setView(input)
        // Set up the buttons
        changeNameDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "Change"
        ) { dialog, _ ->
            user.username = input.text.toString()
            dataBaseService.updateUser(user)
            userName.text = user.username
            dialog.dismiss()
        }
        changeNameDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        changeNameDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val data = Intent()
        data.putExtra("user", user)
        setResult(RESULT_OK, data)
        finish()
    }




}