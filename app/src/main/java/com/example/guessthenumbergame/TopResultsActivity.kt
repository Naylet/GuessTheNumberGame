package com.example.guessthenumbergame

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_top_results.*
import org.json.JSONArray
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

        private var text = "TOP 10\n"

        override fun onPreExecute() {
            super.onPreExecute()
            activity.refreshButton?.visibility = View.INVISIBLE
            activity.progressBar?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {

            sleep(500)

            try {
                val data = URL("http://hufiecgniezno.pl/br/record.php?f=get").readText()
                val json = JSONArray(data)
                if (data != "") {
                    var field: JSONArray
                    for (i in 0..json.length()) {
                        field = json.getJSONArray(i)
                        text += "\t" + (i + 1) + " " + field.getString(1) + " " + field.getString(2) + "\n"
                    }
                    //wpisać do SQLite
                }
                // else if data == err wypisać z SQLite

            } catch (t: Throwable) {
                t.printStackTrace()
            }



            return "OK"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            activity.progressBar?.visibility = View.INVISIBLE
            activity.refreshButton?.visibility = View.VISIBLE
            activity.resultBoard?.text = text
        }
    }


}