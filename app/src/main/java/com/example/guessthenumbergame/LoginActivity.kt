package com.example.guessthenumbergame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guessthenumbergame.database.UserDB
import com.example.guessthenumbergame.model.User

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var userDatabase: UserDB
    private lateinit var loginEditText:EditText
    private lateinit var passwordEditText: EditText

    private var username:String = ""
    private var password:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDatabase = UserDB(this@LoginActivity)

        loginEditText = findViewById(R.id.loginEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        loginButton = findViewById(R.id.loginButton)
        loginButton.setOnClickListener { signIn() }
    }


    private fun signIn(){
        username = loginEditText.text.toString()
        password = passwordEditText.text.toString()

        if (loginNotEmpty(username) && passwordNotEmpty(password)){
            val cursor = userDatabase.getData(username)

            if(cursor.count == 0){
                val newUser = User(username, password, 0)
                userDatabase.insertData(newUser)

                proceed(newUser.getName(), newUser.getScore())
            } else {
                cursor.moveToFirst()
                if(passwordValidate(cursor.getString(1))){
                    proceed(cursor.getString(0), cursor.getInt(2))
                } else{
                    Toast.makeText(this@LoginActivity, "Nieprawidłowe hasło", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loginNotEmpty(username: String): Boolean {
        if(username.isEmpty() ||  username.equals("")){
            Toast.makeText(this@LoginActivity, "Login nie może być pusty", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun passwordNotEmpty(password: String): Boolean {
        if(password.isEmpty() ||  password.equals("")) {
            Toast.makeText(this@LoginActivity, "Hasło nie może być puste", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun passwordValidate(passwordCursor: String): Boolean{
        return password == passwordCursor
    }

    private fun proceed(name: String, score: Int) {
        val loginShared = this.getSharedPreferences("com.example.guessthenumbergame.prefs", 0)
        val editor = loginShared.edit()
        editor.putString("userName", name)
        editor.putInt("recordValue", score)
        editor.apply()

        startActivity(Intent(this@LoginActivity, MainActivity::class.java ))
        finish()
    }
}
