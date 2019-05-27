package com.example.guessthenumbergame.model

import java.net.PasswordAuthentication

class User(private val name: String, private val password: String, private var score: Int) {

    fun getName(): String {
        return this.name
    }

    fun getPassword(): String {
        return this.password
    }

    fun getScore(): Int {
        return this.score
    }

    fun setScore(score: Int){
        this.score = score
    }
}