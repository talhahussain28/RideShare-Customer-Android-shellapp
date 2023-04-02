package com.carty.riderapp.ui.newFlow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.carty.riderapp.R

class NavigationActivity : AppCompatActivity(), View.OnClickListener {
    var ride: TextView? =null
    var payment: LinearLayout? =null
    var support: LinearLayout? =null
    var trip: LinearLayout? =null
    var preference: LinearLayout? =null
    var rating: LinearLayout? =null
    var setting: LinearLayout? =null
    var faq: LinearLayout? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        ride = findViewById<TextView>(R.id.rideNow)
        payment = findViewById<LinearLayout>(R.id.Payment)
        support = findViewById<LinearLayout>(R.id.support)
        trip = findViewById<LinearLayout>(R.id.trip)
        preference = findViewById<LinearLayout>(R.id.preference)
        rating = findViewById<LinearLayout>(R.id.rating)
        setting = findViewById<LinearLayout>(R.id.setting)
        faq = findViewById<LinearLayout>(R.id.faq)
        ride!!.setOnClickListener(this)
        payment!!.setOnClickListener(this)
        support!!.setOnClickListener(this)
        trip!!.setOnClickListener(this)
        preference!!.setOnClickListener(this)
        rating!!.setOnClickListener(this)
        setting!!.setOnClickListener(this)
        faq!!.setOnClickListener(this)
    }

    override fun onClick(v: View) { when(v.id){
      /*  R.id.map ->{
            var intent = Intent(this, MapActivityNew::class.java)
            startActivity(intent)
        }*/
        R.id.Payment ->{
            var intent = Intent(this, PaymentActivityNew::class.java)
            startActivity(intent)
        }R.id.support ->{
            var intent = Intent(this, SupportActivityNew::class.java)
            startActivity(intent)
        }R.id.trip ->{
            var intent = Intent(this, TripActivityNew::class.java)
            startActivity(intent)
        }R.id.setting ->{
            var intent = Intent(this, SettingNewActivity::class.java)
            startActivity(intent)
        }R.id.preference ->{
            var intent = Intent(this, PreferenceActivity::class.java)
            startActivity(intent)
        }
        R.id.rating ->{
            var intent = Intent(this, RatingActivityNew::class.java)
            startActivity(intent)
        }R.id.faq ->{
            var intent = Intent(this, FaqActivityNew::class.java)
            startActivity(intent)
        }
        R.id.rideNow ->{
            var intent = Intent(this, MapActivityNew::class.java)
            startActivity(intent)
        }


    }
    }
}