package com.carty.riderapp.ui.home.trips

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.notification.NotificationFragment
import com.carty.riderapp.ui.home.trips.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.normal_toolbar.*

class TripsFragment : BaseFragment() {


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
        return inflater.inflate(R.layout.fragment_trips, container, false)
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

        imgIconBack.setOnClickListener { baseActivity!!.onBackPressed() }

        imgNotification.setOnClickListener {
            replaceFragment(NotificationFragment(), true, false)
        }

        frNotification.visibility = View.VISIBLE

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
        if (comrFrom.equals("ForRateFragment")) {
            tvTitle.text = "Rate The Ride"
        } else {
            tvTitle.text = "My Trips"
        }


    }

    var viewPager: ViewPager? = null
 var tabLayout: TabLayout? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.viewpager)
        tabLayout = view.findViewById(R.id.tabs)

        setData()
        initView(view)

    }

    private fun initView(view: View) {
        setViewPagerWithTabs(view)
    }

    private fun setViewPagerWithTabs(view: View) {
        setupViewPager(viewPager!!)
        tabLayout!!.setupWithViewPager(viewPager)
        //tabLayout!!.setTabGravity(TabLayout.GRAVITY_CENTER);
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager!!)
        adapter.addFrag(CompletedTripFragment(), "Complete")
        adapter.addFrag(CancelledTripFragment(), "Cancelled")
        viewPager.adapter = adapter
    }




///////////////////////////////////////////////////////////////////////////////

    var tripsModel = ArrayList<TripsModel>()
    private fun setData() {
        tripsModel.clear()
        tripsModel.add(
                TripsModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "19"
                )
        )
        tripsModel.add(
                TripsModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "19"
                )
        )
        tripsModel.add(
                TripsModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "19"
                )
        )
        tripsModel.add(
                TripsModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "19"
                )
        )
    }
}