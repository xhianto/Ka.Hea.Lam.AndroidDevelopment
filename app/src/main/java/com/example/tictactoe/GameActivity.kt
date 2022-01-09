package com.example.tictactoe

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class GameActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var textP1Name: TextView
    private lateinit var textP1Score:TextView
    private lateinit var textP2Name:TextView
    private lateinit var textP2Score:TextView
    private lateinit var textDrawScore:TextView
    private lateinit var buttonReset: Button
    private var gameInput = Array(3) {
        arrayOfNulls<ImageButton>(
            3
        )
    }
    private lateinit var mode: String
    private var player: Int = 1
    private var p1Score: Int = 0
    private var p2Score: Int = 0
    private var drawScore: Int = 0
    private var array = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 0, 0), intArrayOf(0, 0, 0))
    private var gameEnded: Boolean = false
    private lateinit var user: User
    private lateinit var dataBaseHelper: DataBaseService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        val i = intent
        user = i.getSerializableExtra("user") as User

        val bundle = intent.extras
        mode = bundle?.getString("mode").toString()

        dataBaseHelper = DataBaseService(this@GameActivity)

        gameInput[0][0] = findViewById(R.id.imageButton00)
        gameInput[0][0]?.setOnClickListener(this)
        gameInput[0][1] = findViewById(R.id.imageButton01)
        gameInput[0][1]?.setOnClickListener(this)
        gameInput[0][2] = findViewById(R.id.imageButton02)
        gameInput[0][2]?.setOnClickListener(this)
        gameInput[1][0] = findViewById(R.id.imageButton10)
        gameInput[1][0]?.setOnClickListener(this)
        gameInput[1][1] = findViewById(R.id.imageButton11)
        gameInput[1][1]?.setOnClickListener(this)
        gameInput[1][2] = findViewById(R.id.imageButton12)
        gameInput[1][2]?.setOnClickListener(this)
        gameInput[2][0] = findViewById(R.id.imageButton20)
        gameInput[2][0]?.setOnClickListener(this)
        gameInput[2][1] = findViewById(R.id.imageButton21)
        gameInput[2][1]?.setOnClickListener(this)
        gameInput[2][2] = findViewById(R.id.imageButton22)
        gameInput[2][2]?.setOnClickListener(this)

        buttonReset = findViewById(R.id.resetButton)
        buttonReset.setOnClickListener(this)

        textP1Name = findViewById(R.id.player1Name)
        textP1Name.text = user.username
        textP2Name = findViewById(R.id.player2Name)

        when (mode) {
            "easy" -> textP2Name.text = getString(R.string.easy)
            "hard" -> textP2Name.text = getString(R.string.hard)
            else -> textP2Name.text = getString(R.string.friend)
        }

        textP1Score = findViewById(R.id.player1Score)
        textP1Score.text = p1Score.toString()
        textP2Score = findViewById(R.id.player2Score)
        textP2Score.text = p2Score.toString()
        textDrawScore = findViewById(R.id.drawScore)
        textDrawScore.text = drawScore.toString()
        gameStart()
    }

    private fun gameStart() {
        val random = Random()
        player = random.nextInt(2) + 1
        if (player == 1) {
            textP1Name.setBackgroundColor(Color.parseColor("#BBBBBB"))
            textP2Name.setBackgroundColor(Color.parseColor("#EEEEEE"))
        } else {
            textP1Name.setBackgroundColor(Color.parseColor("#EEEEEE"))
            textP2Name.setBackgroundColor(Color.parseColor("#BBBBBB"))
            if (mode == "easy") {
                cPUEasy()
            }
            else if (mode == "hard") {
                cPUHard()
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.resetButton) {
            for (i in 0..2) {
                for (j in 0..2) {
                    gameInput[i][j]!!.setImageResource(0)
                    gameInput[i][j]!!.isEnabled = true
                    array[i][j] = 0
                    gameEnded = false
                }
            }
            gameStart()
        } else {
            when (view.id) {
                R.id.imageButton00 -> {
                    if (player == 1)
                        gameInput[0][0]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[0][0]!!.setImageResource(R.drawable.circle)
                    gameInput[0][0]!!.isEnabled = false
                    array[0][0] = player
                }
                R.id.imageButton01 -> {
                    if (player == 1)
                        gameInput[0][1]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[0][1]!!.setImageResource(R.drawable.circle)
                    gameInput[0][1]!!.isEnabled = false
                    array[0][1] = player
                }
                R.id.imageButton02 -> {
                    if (player == 1)
                        gameInput[0][2]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[0][2]!!.setImageResource(R.drawable.circle)
                    gameInput[0][2]!!.isEnabled = false
                    array[0][2] = player
                }
                R.id.imageButton10 -> {
                    if (player == 1)
                        gameInput[1][0]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[1][0]!!.setImageResource(R.drawable.circle)
                    gameInput[1][0]!!.isEnabled = false
                    array[1][0] = player
                }
                R.id.imageButton11 -> {
                    if (player == 1)
                        gameInput[1][1]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[1][1]!!.setImageResource(R.drawable.circle)
                    gameInput[1][1]!!.isEnabled = false
                    array[1][1] = player
                }
                R.id.imageButton12 -> {
                    if (player == 1)
                        gameInput[1][2]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[1][2]!!.setImageResource(R.drawable.circle)
                    gameInput[1][2]!!.isEnabled = false
                    array[1][2] = player
                }
                R.id.imageButton20 -> {
                    if (player == 1)
                        gameInput[2][0]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[2][0]!!.setImageResource(R.drawable.circle)
                    gameInput[2][0]!!.isEnabled = false
                    array[2][0] = player
                }
                R.id.imageButton21 -> {
                    if (player == 1)
                        gameInput[2][1]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[2][1]!!.setImageResource(R.drawable.circle)
                    gameInput[2][1]!!.isEnabled = false
                    array[2][1] = player
                }
                R.id.imageButton22 -> {
                    if (player == 1)
                        gameInput[2][2]!!.setImageResource(R.drawable.cross)
                    else
                        gameInput[2][2]!!.setImageResource(R.drawable.circle)
                    gameInput[2][2]!!.isEnabled = false
                    array[2][2] = player
                }
            }
            check()
            if (player == 1) {
                player = 2
                textP2Name.setBackgroundColor(Color.parseColor("#BBBBBB"))
                textP1Name.setBackgroundColor(Color.parseColor("#EEEEEE"))
                if (mode == "easy" && !gameEnded)
                    cPUEasy()
                else if (mode == "hard" && !gameEnded)
                    cPUHard()
            }
            else {
                player = 1
                textP1Name.setBackgroundColor(Color.parseColor("#BBBBBB"))
                textP2Name.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }
    }

    private fun check() {
        if (!checkDraw()) {
            for (i in 0..2) {
                if (array[i][0] == array[i][1] && array[i][0] == array[i][2] && array[i][0] != 0) {
                    if (array[i][0] == 1) {
                        gameInput[i][0]!!.setImageResource(R.drawable.cross_win)
                        gameInput[i][1]!!.setImageResource(R.drawable.cross_win)
                        gameInput[i][2]!!.setImageResource(R.drawable.cross_win)
                    } else {
                        gameInput[i][0]!!.setImageResource(R.drawable.circle_win)
                        gameInput[i][1]!!.setImageResource(R.drawable.circle_win)
                        gameInput[i][2]!!.setImageResource(R.drawable.circle_win)
                    }
                    winner()
                }
            }
            for (i in 0..2) {
                if (array[0][i] == array[1][i] && array[0][i] == array[2][i] && array[0][i] != 0) {
                    if (array[0][i] == 1) {
                        gameInput[0][i]!!.setImageResource(R.drawable.cross_win)
                        gameInput[1][i]!!.setImageResource(R.drawable.cross_win)
                        gameInput[2][i]!!.setImageResource(R.drawable.cross_win)
                    } else {
                        gameInput[0][i]!!.setImageResource(R.drawable.circle_win)
                        gameInput[1][i]!!.setImageResource(R.drawable.circle_win)
                        gameInput[2][i]!!.setImageResource(R.drawable.circle_win)
                    }
                    winner()
                }
            }
            if (array[0][0] == array[1][1] && array[0][0] == array[2][2] && array[0][0] != 0) {
                if (array[0][0] == 1) {
                    gameInput[0][0]!!.setImageResource(R.drawable.cross_win)
                    gameInput[1][1]!!.setImageResource(R.drawable.cross_win)
                    gameInput[2][2]!!.setImageResource(R.drawable.cross_win)
                } else {
                    gameInput[0][0]!!.setImageResource(R.drawable.circle_win)
                    gameInput[1][1]!!.setImageResource(R.drawable.circle_win)
                    gameInput[2][2]!!.setImageResource(R.drawable.circle_win)
                }
                winner()
            }
            if (array[0][2] == array[1][1] && array[0][2] == array[2][0] && array[0][2] != 0) {
                if (array[0][2] == 1) {
                    gameInput[0][2]!!.setImageResource(R.drawable.cross_win)
                    gameInput[1][1]!!.setImageResource(R.drawable.cross_win)
                    gameInput[2][0]!!.setImageResource(R.drawable.cross_win)
                } else {
                    gameInput[0][2]!!.setImageResource(R.drawable.circle_win)
                    gameInput[1][1]!!.setImageResource(R.drawable.circle_win)
                    gameInput[2][0]!!.setImageResource(R.drawable.circle_win)
                }
                winner()
            }
        } else {
            drawScore++
            textDrawScore.text = drawScore.toString()
            messageDraw()
        }
    }

    private fun cPUEasy() {
        var notAvailable = true
        while (notAvailable) {
            val random = Random()
            val getalx = random.nextInt(3)
            val getaly = random.nextInt(3)
            if (gameInput[getalx][getaly]!!.isEnabled) {
                notAvailable = false
                gameInput[getalx][getaly]!!.performClick()
            }
        }
    }

    private fun winner() {
        gameEnded = true
        val alertDialog = AlertDialog.Builder(this@GameActivity).create()
        for (i in 0..2) {
            for (j in 0..2) {
                gameInput[i][j]!!.isEnabled = false
            }
        }
        val winner: String?
        if (player == 1) {
            p1Score++
            textP1Score.text = p1Score.toString()
            when (mode) {
                "easy" -> user.easyWin++
                "hard" -> user.hardWin++
                else -> user.friendWin++
            }
            winner = user.username
        } else {
            p2Score++
            textP2Score.text = p2Score.toString()
            winner = when (mode) {
                "easy" -> {
                    user.easyLose++
                    "EasyCPU"
                }
                "hard" -> {
                    user.hardLose++
                    "HardCPu"
                }
                else -> {
                    user.friendLose++
                    "Your friend"
                }
            }
        }
        dataBaseHelper.updateUser(user)
        alertDialog.setTitle("$winner wins!!!")
        alertDialog.setMessage("$winner has won.")
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun checkDraw(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameInput[i][j]!!.isEnabled) return false
            }
        }
        return true
    }

    private fun messageDraw() {
        gameEnded = true
        when (mode) {
            "easy" -> user.easyDraw++
            "hard" -> user.hardDraw++
            else -> user.friendDraw++
        }
        dataBaseHelper.updateUser(user)
        val winDialog = AlertDialog.Builder(this@GameActivity).create()
        winDialog.setTitle("Draw!!!")
        winDialog.setMessage("Game ended in a draw.")
        winDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        winDialog.show()
    }

    private fun cPUHard() {
        var turnDone = false
        //Check kans om te winnen
        for (i in 0..2) {
            if (array[i][0] + array[i][1] + array[i][2] == 4 && (array[i][0] == 0 || array[i][1] == 0 || array[i][2] == 0)) {
                if (!turnDone) {
                    hardCPUClick(i, 0, i, 1, i, 2)
                    turnDone = true
                }
            }
        }
        for (i in 0..2) {
            if (array[0][i] + array[1][i] + array[2][i] == 4 && (array[0][i] == 0 || array[1][i] == 0 || array[2][i] == 0)) {
                if (!turnDone) {
                    hardCPUClick(0, i, 1, i, 2, i)
                    turnDone = true
                }
            }
        }
        if (array[0][0] + array[1][1] + array[2][2] == 4 && (array[0][0] == 0 || array[1][1] == 0 || array[2][2] == 0)) {
            if (!turnDone) {
                hardCPUClick(0, 0, 1, 1, 2, 2)
                turnDone = true
            }
        }
        if (array[2][0] + array[1][1] + array[0][2] == 4 && (array[2][0] == 0 || array[1][1] == 0 || array[0][2] == 0)) {
            if (!turnDone) {
                hardCPUClick(2, 0, 1, 1, 0, 2)
                turnDone = true
            }
        }
        //Check of er geblokeerd moet worden
        for (i in 0..2) {
            if (array[i][0] + array[i][1] + array[i][2] == 2 && (array[i][0] == 1 || array[i][1] == 1)) {
                if (!turnDone) {
                    hardCPUClick(i, 0, i, 1, i, 2)
                    turnDone = true
                }
            }
        }
        for (i in 0..2) {
            if (array[0][i] + array[1][i] + array[2][i] == 2 && (array[0][i] == 1 || array[1][i] == 1)) {
                if (!turnDone) {
                    hardCPUClick(0, i, 1, i, 2, i)
                    turnDone = true
                }
            }
        }
        if (array[0][0] + array[1][1] + array[2][2] == 2 && (array[0][0] == 1 || array[1][1] == 1)) {
            if (!turnDone) {
                hardCPUClick(0, 0, 1, 1, 2, 2)
                turnDone = true
            }
        }
        if (array[2][0] + array[1][1] + array[0][2] == 2 && (array[2][0] == 1 || array[1][1] == 1)) {
            if (!turnDone) {
                hardCPUClick(2, 0, 1, 1, 0, 2)
                turnDone = true
            }
        }
        //anders random neer plaatsen
        if (!turnDone) {
            cPUEasy()
        }
    }

    private fun hardCPUClick(x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int) {
        when {
            array[x1][y1] == 0 -> gameInput[x1][y1]!!.performClick()
            array[x2][y2] == 0 -> gameInput[x2][y2]!!.performClick()
            array[x3][y3] == 0 -> gameInput[x3][y3]!!.performClick()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        val data = Intent()
        data.putExtra("user", user)
        setResult(RESULT_OK, data)
        finish()
    }
}