package com.example.tictactoe

import java.io.Serializable

class User : Serializable {
    var userId = 0
    var username: String? = null
    var emailAddress: String? = null
    var password: String? = null
    var easyWin = 0
    var easyDraw = 0
    var easyLose = 0
    var hardWin = 0
    var hardDraw = 0
    var hardLose = 0
    var friendWin = 0
    var friendDraw = 0
    var friendLose = 0

    constructor(userId: Int, username: String?, emailAddress: String?, password: String?) {
        this.userId = userId
        this.username = username
        this.emailAddress = emailAddress
        this.password = password
        easyWin = 0
        easyDraw = 0
        easyLose = 0
        hardWin = 0
        hardDraw = 0
        hardLose = 0
        friendWin = 0
        friendDraw = 0
        hardLose = 0
    }

    constructor()
}