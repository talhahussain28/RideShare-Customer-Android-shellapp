package com.herride.customer.ui.newFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.herride.customer.R

class FaqActivityNew : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_new)
        val buttonBack: ImageView = findViewById<View>(R.id.imgIconBack) as ImageView
        buttonBack.setOnClickListener {
            var intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }
    }
}