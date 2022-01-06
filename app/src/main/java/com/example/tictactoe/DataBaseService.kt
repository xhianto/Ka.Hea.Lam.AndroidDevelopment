package com.example.tictactoe

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseService(context: Context?) :
    SQLiteOpenHelper(context, "user.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement =
            "CREATE TABLE $USER_TABLE ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EMAILADDRESS TEXT NOT NULL UNIQUE, $COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT, $COLUMN_EASYWIN INT, $COLUMN_EASYDRAW INT, $COLUMN_EASYLOSE INT, $COLUMN_HARDWIN INT, $COLUMN_HARDDRAW INT, $COLUMN_HARDLOSE INT, $COLUMN_FRIENDWIN INT, $COLUMN_FRIENDDRAW INT, $COLUMN_FRIENDLOSE INT)"
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun addUser(user: User) {
        val db = this.readableDatabase
        val cv = makeCVWithoutId(user)
        db.insert(USER_TABLE, null, cv)
        db.close()
    }

    fun getUserByEmail(email: String): User {
        val user = User()
        val query =
            "SELECT * FROM $USER_TABLE WHERE '$email' = $COLUMN_EMAILADDRESS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            user.userId = cursor.getInt(0)
            user.emailAddress = cursor.getString(1)
            user.username = cursor.getString(2)
            user.password = cursor.getString(3)
            user.easyWin = cursor.getInt(4)
            user.easyDraw = cursor.getInt(5)
            user.easyLose = cursor.getInt(6)
            user.hardWin = cursor.getInt(7)
            user.hardDraw = cursor.getInt(8)
            user.hardLose = cursor.getInt(9)
            user.friendWin = cursor.getInt(10)
            user.friendDraw = cursor.getInt(11)
            user.friendLose = cursor.getInt(12)
        }
        cursor.close()
        db.close()
        return user
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase
        val cv = makeCVWithoutId(user)
        cv.put(COLUMN_ID, user.userId)
        db.update(
            USER_TABLE,
            cv,
            COLUMN_ID + " = ?",
            arrayOf(java.lang.String.valueOf(user.userId))
        )
        db.close()
    }

    private fun makeCVWithoutId(user: User): ContentValues {
        val cv = ContentValues()
        cv.put(COLUMN_USERNAME, user.username)
        cv.put(COLUMN_EMAILADDRESS, user.emailAddress)
        cv.put(COLUMN_PASSWORD, user.password)
        cv.put(COLUMN_EASYWIN, user.easyWin)
        cv.put(COLUMN_EASYDRAW, user.easyDraw)
        cv.put(COLUMN_EASYLOSE, user.easyLose)
        cv.put(COLUMN_HARDWIN, user.hardWin)
        cv.put(COLUMN_HARDDRAW, user.hardDraw)
        cv.put(COLUMN_HARDLOSE, user.hardLose)
        cv.put(COLUMN_FRIENDWIN, user.friendWin)
        cv.put(COLUMN_FRIENDDRAW, user.friendDraw)
        cv.put(COLUMN_FRIENDLOSE, user.friendLose)
        return cv
    }

    companion object {
        const val USER_TABLE = "USER_TABLE"
        const val COLUMN_ID = "ID"
        const val COLUMN_USERNAME = "USERNAME"
        const val COLUMN_EMAILADDRESS = "EMAILADDRESS"
        const val COLUMN_PASSWORD = "PASSWORD"
        const val COLUMN_EASYWIN = "EASYWIN"
        const val COLUMN_EASYDRAW = "EASYDRAW"
        const val COLUMN_EASYLOSE = "EASYLOSE"
        const val COLUMN_HARDWIN = "HARDWIN"
        const val COLUMN_HARDDRAW = "HARDDRAW"
        const val COLUMN_HARDLOSE = "HARDLOSE"
        const val COLUMN_FRIENDWIN = "FRIENDWIN"
        const val COLUMN_FRIENDDRAW = "FRIENDDRAW"
        const val COLUMN_FRIENDLOSE = "FRIENDLOSE"
    }
}