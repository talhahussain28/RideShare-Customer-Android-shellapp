package com.carty.riderapp.ui.newFlow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carty.riderapp.R
import kotlinx.android.synthetic.main.fragment_register.*

class SignupActivityNew : AppCompatActivity() {
    var isSpinnerClick = false
    var gender = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_new)
        val button: Button = findViewById<View>(R.id.btnNext) as Button
        val signIn: TextView = findViewById<View>(R.id.signInTV) as TextView
        val terms: TextView = findViewById<View>(R.id.terms_conditions) as TextView
        button.setOnClickListener {
            var intent = Intent(this, LoginActivityNew::class.java)
            startActivity(intent)
        }
        signIn.setOnClickListener {
            var intent = Intent(this, SignupActivityNew::class.java)
            startActivity(intent)
        }
        terms.setOnClickListener {
            var intent = Intent(this, TermsActivity::class.java)
            startActivity(intent)
        }
        genterList = spinnerGenderData()
        var spinnerGenderAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, genterList)
        spinnerGender.adapter = spinnerGenderAdapter


        flGender.setOnClickListener {
            isSpinnerClick = true
            spinnerGender.performClick()
        }

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e("TAG", "Country-> onNothingSelected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("TAG", "Country-> onItemSelected $position")
                if (position >= 0 && isSpinnerClick) {
                    isSpinnerClick = false
                    val mGender = genterList[position]
                    tvGenderItem.text = mGender
                    gender = mGender.toLowerCase()
//                        getStateListAPI(country.countryId)

                } else {
//                    resetState()
//                    resetCity()
                }
            }
        }

    }

    var genterList = arrayListOf<String>()
    fun spinnerGenderData(): ArrayList<String> {
        var arr = ArrayList<String>()

        //arr.add("Gender")
        arr.add("Male")
        arr.add("Female")
        return arr
    }
}