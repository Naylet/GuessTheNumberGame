package com.example.guessthenumbergame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class EntryFullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_entry_fullscreen)

        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        val userName = loginShared.getString("userName", null)

        Handler().postDelayed({
            if (userName == null){
                //login activity
                startActivity(Intent(this@EntryFullscreenActivity, LoginActivity::class.java ))
            }else{
                startActivity(Intent(this@EntryFullscreenActivity, MainActivity::class.java ))
            }
            finish()
        },3000)
    }

}
