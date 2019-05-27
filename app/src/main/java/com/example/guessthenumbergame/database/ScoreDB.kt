package com.example.guessthenumbergame.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guessthenumbergame.model.Rank

class ScoreDB(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, 1 ){

    companion object {
        val DATABASE_NAME = "rank.db"
        val TABLE_NAME = "Scores"
        val ID:String = "ID"
        val NAME:String = "NAME"
        val SCORE:String = "SCORE"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_NAME($ID INTEGER PRIMARY KEY autoincrement, $NAME  TEXT , $SCORE INTEGER)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun flushDatabase(){
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun insertData(rank:Rank): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(NAME, rank.getName())
        cv.put(SCORE, rank.getScore())
        return db.insert(TABLE_NAME , null, cv)
    }

    fun getData(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

}