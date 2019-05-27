package com.example.guessthenumbergame.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guessthenumbergame.model.User

class UserDB (context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, 1 ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME($NAME TEXT PRIMARY KEY , $PASSWORD  TEXT , $SCORE INT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "user.db"

        const val TABLE_NAME = "Users"
        const val NAME: String = "NAME"
        const val PASSWORD: String = "PASSWORD"
        const val SCORE: String = "SCORE"
    }

    fun insertData(user:User): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(NAME, user.getName())
        cv.put(PASSWORD, user.getPassword())
        cv.put(SCORE, user.getScore())
        return db.insert(TABLE_NAME , null, cv)
    }
    
    fun updateData(userName:String, score:Int): Int {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(SCORE, score)
        return db.update(TABLE_NAME, cv, "$NAME=?", arrayOf(userName))
    }
    
    fun getData(name: String): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $NAME=?", arrayOf(name))
    }

}