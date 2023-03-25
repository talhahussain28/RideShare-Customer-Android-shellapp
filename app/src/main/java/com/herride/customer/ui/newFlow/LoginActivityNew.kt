package com.herride.customer.ui.newFlow

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.herride.customer.R

class LoginActivityNew : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)
        val button: Button = findViewById<View>(R.id.btnNext) as Button
        val signUp: TextView = findViewById<View>(R.id.signUpTV) as TextView
        val buttonBack: ImageButton = findViewById<View>(R.id.backButton) as ImageButton
        button.setOnClickListener {
            var intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
        signUp.setOnClickListener {
            var intent = Intent(this, SignupActivityNew::class.java)
            startActivity(intent)
        }
        buttonBack.setOnClickListener {
            var intent = Intent(this, SignupActivityNew::class.java)
            startActivity(intent)
        }
    }
}