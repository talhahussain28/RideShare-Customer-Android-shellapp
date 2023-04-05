package com.carty.riderapp.newFragmentFlow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.newFlow.*

class SideFragment : BaseFragment(), View.OnClickListener {
    var ride: TextView? = null
    var payment: LinearLayout? = null
    var support: LinearLayout? = null
    var trip: LinearLayout? = null
    var preference: LinearLayout? = null
    var rating: LinearLayout? = null
    var setting: LinearLayout? = null
    var faq: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_side, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ride = view.findViewById<TextView>(R.id.rideNow)
        payment = view.findViewById<LinearLayout>(R.id.Payment)
        support = view.findViewById<LinearLayout>(R.id.support)
        trip = view.findViewById<LinearLayout>(R.id.trip)
        preference = view.findViewById<LinearLayout>(R.id.preference)
        rating = view.findViewById<LinearLayout>(R.id.rating)
        setting = view.findViewById<LinearLayout>(R.id.setting)
        faq = view.findViewById<LinearLayout>(R.id.faq)
        ride!!.setOnClickListener(this)
        payment!!.setOnClickListener(this)
        support!!.setOnClickListener(this)
        trip!!.setOnClickListener(this)
        preference!!.setOnClickListener(this)
        rating!!.setOnClickListener(this)
        setting!!.setOnClickListener(this)
        faq!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            /*  R.id.map ->{
                  var intent = Intent(this, MapActivityNew::class.java)
                  startActivity(intent)
              }*/
            R.id.Payment -> {
                replaceFragment(PaymentCardAddFragment(), true, false,R.id.login_container)
            }
            R.id.support -> {

            }
            R.id.trip -> {

            }
            R.id.setting -> {

            }
            R.id.preference -> {

            }
            R.id.rating -> {

            }
            R.id.faq -> {

            }
            R.id.rideNow -> {

            }


        }
    }

}