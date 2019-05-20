package com.example.guessthenumbergame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_entry_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class EntryFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_entry_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Handler().postDelayed({
            startActivity(Intent(this@EntryFullscreenActivity, MainActivity::class.java ))
            finish()
        },3000)
    }

}
