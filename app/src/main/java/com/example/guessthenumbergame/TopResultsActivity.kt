package com.example.guessthenumbergame

import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.guessthenumbergame.database.ScoreDB
import com.example.guessthenumbergame.model.Rank
import kotlinx.android.synthetic.main.activity_top_results.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Thread.sleep
import java.net.URL

class TopResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_results)

        HttpGet(this@TopResultsActivity).execute()

    }

    fun returnToMain(view: View) {
        finish()
    }

    fun refresh(view: View) {
        HttpGet(this@TopResultsActivity).execute()
    }

    class HttpGet(private var activity: TopResultsActivity) : AsyncTask<String, String, String>() {

        private lateinit var buffer: StringBuffer
        private lateinit var database: ScoreDB

        override fun onPreExecute() {
            super.onPreExecute()
            activity.refreshButton?.visibility = View.INVISIBLE
            activity.progressBar?.visibility = View.VISIBLE

            database = ScoreDB(activity)
            buffer = StringBuffer()
        }

        override fun doInBackground(vararg params: String?): String? {
            sleep(500)

            try{
                val data = URL("http://hufiecgniezno.pl/br/record.php?f=get").readText() //.lines()[6]
                val json = JSONArray(data)

                if (data != "") {
                    var field: JSONArray
                    var rank: Rank

                    database.flushDatabase()
                    buffer.append("TOP 10 \n")

                    for (i: Int in 0 until json.length()) {
                        field = json.getJSONArray(i)
                        rank = Rank((i+1), field.getString(1), field.getInt(2))

                        buffer.append("\t\t")
                        buffer.append(rank.getID().toString() + " ")
                        buffer.append(rank.getName() + " ")
                        buffer.append(rank.getScore().toString() + "\n")

                        val result = database.insertData(rank)

                        if(result.equals(-1)){
                            Toast.makeText(activity, "Błąd wstawiania danych do bazy", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
            catch (e:IOException){

                activity.runOnUiThread {
                    kotlin.run { Toast.makeText(activity, "Brak połączenia sieciowego.\n Dane pobrane z bazy danych", Toast.LENGTH_SHORT).show() }
                }

                val cursor = database.getData()

                if (cursor.count == 0) {
                    buffer.append("Brak danych w bazie")
                } else {
                    buffer.append("TOP 10 \n")
                    while (cursor.moveToNext()) {
                        buffer.append("\t\t")
                        buffer.append(cursor.getInt(0).toString() + " ")
                        buffer.append(cursor.getString(1) + " ")
                        buffer.append(cursor.getInt(2).toString() + "\n")
                    }
                }
            }

            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            activity.progressBar?.visibility = View.INVISIBLE
            activity.refreshButton?.visibility = View.VISIBLE
            activity.resultBoard?.text = buffer.toString()
        }
    }


}