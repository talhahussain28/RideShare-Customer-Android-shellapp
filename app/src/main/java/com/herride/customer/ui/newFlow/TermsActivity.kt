package com.herride.customer.ui.newFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.herride.customer.R

class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        val buttonBack: ImageView = findViewById<View>(R.id.imgIconBack) as ImageView
        buttonBack.setOnClickListener {
            var intent = Intent(this, SignupActivityNew::class.java)
            startActivity(intent)
        }
    }
}