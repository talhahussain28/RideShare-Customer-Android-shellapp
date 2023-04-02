package com.carty.riderapp.ui.home.ratetheride

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.HomeFragment
import com.carty.riderapp.ui.home.notification.NotificationFragment
import kotlinx.android.synthetic.main.fragment_rate.*
import kotlinx.android.synthetic.main.normal_toolbar.*

class RateFragment : BaseFragment() {
    var comrFrom = "none"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            var bundle = arguments
            //bundle.putString("comeFrom","MainActivity")
            if (bundle!!.containsKey("comeFrom")) {
                comrFrom = "${bundle!!.getString("comeFrom")}"
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate, container, false)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }


    private fun setToolbar() {

        imgIconBack.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )

        imgIconBack.setOnClickListener {
            if (comrFrom.equals("ForRateFragment", ignoreCase = true)) {
                baseActivity!!.onBackPressed()
            } else {
                replaceFragment(HomeFragment(), false, true)
            }
        }

        imgNotification.setOnClickListener {
            replaceFragment(NotificationFragment(), true, false)
        }

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
        tvTitle.text = "Rate The Ride"

    }

//    private fun setToolbar() {
//        imgIconBack.setOnClickListener {
//            replaceFragment(HomeFragment(), false, true)
//        }
//        imgNotification.setOnClickListener {
//            replaceFragment(NotificationFragment(), false, true)
//        }
//        imgIconBack.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_arrow_left))
//        tvTitle.text = "Rate The Ride"
//        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
//
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvSubmit.setOnClickListener {
            if (comrFrom.equals("ForRateFragment", ignoreCase = true)) {
                baseActivity!!.onBackPressed()
            } else {
                replaceFragment(HomeFragment(), false, true)
            }
        }
    }
}