package com.example.guessthenumbergame

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_top_results.*
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var resultInfo: TextView
    private lateinit var gameInfo: TextView
    private lateinit var guess: EditText

    var hits:Int = 0
    var resultSum:Int = 0
    var randomNumber:Int = 0
    var points:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultInfo = findViewById(R.id.resultInfo)
        gameInfo = findViewById(R.id.gameInfo)
        guess = findViewById(R.id.numberInput)

        getRecord()
        makeNewGame()
    }

    fun fire(view: View){
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
                        //put result into application cache and to remote database
                        HttpPost(this@MainActivity).execute()
                        setRecord()

                        winOrLoseAlert("Wygrałeś", "Udało Ci się trafić za $hits razem. \n Zdobyłeś $points pkt")
                    }
                }
            } else {
                if(guess.text.toString().toInt() != randomNumber){
                    winOrLoseAlert("Przegrałeś", "Udało Ci się zdobyć $resultSum pkt")
                } else {
                    //put result into application cache and to remote database
                    HttpPost(this@MainActivity).execute()
                    setRecord()
                    winOrLoseAlert("Wygrałeś", "Udało Ci się trafić za $hits razem. \n Zdobyłeś $points pkt")
                }
            }
        }
    }

    fun newGame(view:View){
        winOrLoseAlert("Nowa gra", "Skończyłeś z wynikiem $resultSum pkt")
    }

    fun showResultBoard(view:View){
        startActivity(Intent(this@MainActivity, TopResultsActivity::class.java))
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

    private fun winOrLoseAlert(title: String, message: String){
        val dialogBuilder = AlertDialog.Builder(this@MainActivity)

        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(message)

        dialogBuilder.setPositiveButton("OK"){ _, _ ->
            makeNewGame()
        }

        val dialog:AlertDialog = dialogBuilder.create()
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun getRecord(){
        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        resultSum = loginShared.getInt("recordValue", 0)
        resultInfo.text = "Twój wynik: $resultSum"
    }

    @SuppressLint("SetTextI18n")
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

    class HttpPost(private var activity: MainActivity)  : AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            //activity.progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            val loginShared = activity.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
            val userLogin = loginShared.getString("userLogin", "132271")

            URL("http://hufiecgniezno.pl/br/record.php?f=add&id=$userLogin&r=${activity.resultSum}").readText()

            //if (connection != "err")

            return "OK"

        }
    }
}