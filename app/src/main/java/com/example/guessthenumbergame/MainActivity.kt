package com.example.guessthenumbergame

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.guessthenumbergame.database.UserDB
import java.io.IOException
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var resultInfo: TextView
    private lateinit var gameInfo: TextView
    private lateinit var guess: EditText
    private lateinit var userDatabase: UserDB

    var hits:Int = 0
    var resultSum:Int = 0
    var userName:String = ""
    var randomNumber:Int = 0
    var points:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultInfo = findViewById(R.id.resultInfo)
        gameInfo = findViewById(R.id.gameInfo)
        guess = findViewById(R.id.numberInput)
        userDatabase = UserDB(this@MainActivity)

        getRecord()
        makeNewGame()
    }

    fun fire(view :View) {
        if(guess.text.toString().toInt() < 0 || guess.text.toString().toInt() > 20){
            wrongNumberAlert()
        }
        else {
            hits++

            if(hits < 10){
                when(guess.text.toString().toInt()) {
                    in 0..(randomNumber-1) -> {
                        gameInfo.text = "Wylosowana liczba jest WIĘKSZA.\n To była Twoja $hits próba"
                        guess.setText("")
                    }
                    in (randomNumber+1)..20 ->{
                        gameInfo.text = "Wylosowana liczba jest MNIEJSZA.\n To była Twoja $hits próba"
                        guess.setText("")
                    }
                    randomNumber ->{
                        //put result into application cache
                        setRecord()
                        showMessage("Wygrałeś", "Udało Ci się trafić za $hits razem. \n Zdobyłeś $points pkt")
                    }
                }
            } else {
                if(guess.text.toString().toInt() != randomNumber){
                    HttpPost(this@MainActivity).execute()
                    showMessage("Przegrałeś", "Udało Ci się zdobyć $resultSum pkt")
                    flushRecord()
                } else {
                    //put result into application cache
                    setRecord()
                    showMessage("Wygrałeś", "Udało Ci się trafić za $hits razem. \n Zdobyłeś $points pkt")
                }
            }
        }
    }

    fun newGame(view: View) {
        HttpPost(this@MainActivity).execute()
        showMessage("Nowa gra", "Skończyłeś z wynikiem $resultSum pkt")
        flushRecord()
    }

    fun showResultBoard(view: View) {
        startActivity(Intent(this@MainActivity, TopResultsActivity::class.java))
    }

    fun signOut(view: View){
        userDatabase.updateData(userName,resultSum)

        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        val editor = loginShared.edit()
        editor.putInt("recordValue", resultSum)
        editor.putString("userName", null)
        editor.apply()

        startActivity(Intent(this@MainActivity, LoginActivity::class.java ))
        finish()
    }

    private fun makeNewGame(){
        gameInfo.text = "Zacznij strzelać!"
        guess.setText("")

        randomNumber = Random.nextInt(0,20)
        hits = 0

        Toast.makeText(this@MainActivity, "Wylosowano liczbę", Toast.LENGTH_SHORT).show()
    }

    private fun wrongNumberAlert(){
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)

        dialogBuilder.setTitle("Zły numer!")
        dialogBuilder.setMessage("Wybierz liczbę z przedziału 0 do 20")

        dialogBuilder.setPositiveButton("OK",null)

        val dialog:AlertDialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showMessage(title: String, message: String){
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)

        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)

        dialogBuilder.setPositiveButton("OK"){ _, _ ->
            makeNewGame()
        }

        val dialog:AlertDialog = dialogBuilder.create()
        dialog.show()
    }

    private fun getRecord(){
        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        userName = loginShared.getString("userName", null)
        resultSum = loginShared.getInt("recordValue", 0)

        Toast.makeText(this@MainActivity, "Witaj $userName", Toast.LENGTH_SHORT).show()
        resultInfo.text = "Twój wynik: $resultSum"
    }

    private fun setRecord(){
        when(hits) {
            in 1..1 -> {
                points = 5
                resultSum += points
            }
            in 2..4 -> {
                points = 3
                resultSum += points
            }
            in 5..6 -> {
                points = 2
                resultSum += points
            }
            in 7..10 -> {
                points = 1
                resultSum += points
            }
        }

        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        val editor = loginShared.edit()
        editor.putInt("recordValue", resultSum)
        editor.apply()

        resultInfo.text = "Twój wynik: $resultSum"
    }

    private fun flushRecord(){
        resultSum = 0

        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        val editor = loginShared.edit()
        editor.putInt("recordValue", resultSum)
        editor.apply()

        resultInfo.text = "Twój wynik: $resultSum"
    }

    class HttpPost(private var activity: MainActivity)  : AsyncTask<String, String, String>(){

        override fun doInBackground(vararg params: String?): String? {

            try {
                URL("http://hufiecgniezno.pl/br/record.php?f=add&id=${activity.userName}&r=${activity.resultSum}").readText()
                return "OK"
            }
            catch(e:IOException){
                return "FAILED"
            }


        }
    }
}