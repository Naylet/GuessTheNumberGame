package com.example.guessthenumbergame.model

class Rank(private val id: Int, private val name: String, private val score: Int) {

    fun getID(): Int {
        return this.id
    }

    fun getName(): String {
        return this.name
    }

    fun getScore(): Int {
        return this.score
    }
}