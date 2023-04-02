package com.carty.riderapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.ui.base.BaseActivity
import com.carty.riderapp.ui.home.HomeFragment
import com.carty.riderapp.ui.home.ProfileFragment
import com.carty.riderapp.ui.home.SupportFragment
import com.carty.riderapp.ui.home.faq.FaqFragment
import com.carty.riderapp.ui.home.payment_card_setting.PaymentSettingFragment
import com.carty.riderapp.ui.home.ratetheride.RateTheRideFragment
import com.carty.riderapp.ui.home.response.GetProfileApiResponse
import com.carty.riderapp.ui.home.trips.TripsFragment
import com.carty.riderapp.ui.home.setting.SettingFragment
import com.carty.riderapp.ui.signin_up.SplashFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_navigation_menu.*
import retrofit2.Response


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getSettingCall()
        //repo.pref.isUserLogin = true
        setContentView(R.layout.activity_main)
        //replaceFragment(SplashFragment(), false, true)
        //replaceFragment(HomeFragment(), false, true)
        //getProfileAPICall(true)
        replaceFragment(SplashFragment(), false, true)
    }


    public fun clickNavigation(view: View) {
        drawerClose()
        when (view.id) {
            R.id.navDashBoard -> {
                replaceFragment(HomeFragment(), false, true)
//                addFragment(HomeFragment(), false)
            }

            R.id.imgvUserProfileHome -> {
                replaceFragment(ProfileFragment(), true, false)
//                addFragment(ProfileFragment(), true)
            }
            R.id.navTrips -> {
                var tripList = TripsFragment()
                var bundle = Bundle()
                bundle.putString("comeFrom", "MainActivity")
                tripList.arguments = bundle
                replaceFragment(tripList, true, true)
//                addFragment(tripList, true)
            }
            R.id.navRateTheRide -> {

//                var tripList = TripsFragment()
                var tripList = RateTheRideFragment()
                var bundle = Bundle()
                bundle.putString("comeFrom", "ForRateFragment")
                tripList.arguments = bundle
                replaceFragment(tripList, true, true)
//                addFragment(tripList, true)

                //replaceFragment(RateTheRideFragment(), true, true)
            }
            R.id.navSetting -> {
                replaceFragment(SettingFragment(), true, true)
//                addFragment(SettingFragment(), true)
            }
            R.id.navSupport -> {
                replaceFragment(SupportFragment(), true, true)
//                addFragment(SettingFragment(), true)
            }
            R.id.navFaq -> {
                replaceFragment(FaqFragment(), true, true)
//                addFragment(SettingFragment(), true)
            }
            R.id.navPaymentSetting -> {
                replaceFragment(PaymentSettingFragment(), true, true)
//                addFragment(PaymentSettingFragment(), true)
            }
            R.id.navBecomeADriver -> {
              /*  DialogYesNo("Would you like to become \na Driver?","Join a  community that cares about your safety and earn extra money!"
                ,"JOIN NOW","")*/
            }
            R.id.navLogout -> {
                dialogLogout()
            }
        }
    }

//MTH
    /*fun DialogYesNo(title: String,msg: String, yesLabel: String, noLabel: String, isTwoBtn: Boolean = false) {
        val dialog = Dialog(this@MainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_become_driver)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        var txt_dialog_msg = dialog.findViewById<TextView>(R.id.txt_dialog_msg)
        var txt_dialog_ok = dialog.findViewById<TextView>(R.id.txt_dialog_ok)
        var txt_dialog_cancel = dialog.findViewById<TextView>(R.id.txt_dialog_cancel)
        var txt_dialog_title = dialog.findViewById<TextView>(R.id.txt_dialog_title)
        var imgv_close_dialog = dialog.findViewById<ImageView>(R.id.imgv_close_dialog)

        txt_dialog_ok.setText(yesLabel)
        txt_dialog_cancel.setText(noLabel)
        txt_dialog_title.setText(title)

        if (isTwoBtn) {
            txt_dialog_cancel.setVisibility(View.VISIBLE)
        } else {
            txt_dialog_cancel.setVisibility(View.GONE)
        }


        txt_dialog_msg.setText(msg)
        // txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
        // txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
        // txt_dialog_cancel.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));

        txt_dialog_ok.setOnClickListener {
            dialog.dismiss()

        }
        txt_dialog_cancel.setOnClickListener {
            dialog.dismiss()

        }
        imgv_close_dialog.setOnClickListener {
            dialog.dismiss()
        }


        val window = dialog.getWindow()
        val wlp = window!!.getAttributes()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wlp.windowAnimations = R.style.DialogAnimation
        wlp.gravity = Gravity.CENTER
        window!!.setAttributes(wlp)
        if (!isFinishing) {
            dialog.show()
        }
*//*        if (!(this as AppCompatActivity).isFinishing())
            dialog.show()*//*
    }
*/

    private fun dialogLogout() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to Logout?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            /*Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()*/

           // logoutApiCall()
            /*var intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()*/

        }

        builder.setNegativeButton("No") { dialog, which ->
            /*Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()*/
        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(applicationContext,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }


//MTH
    /*fun logoutApiCall() {
        repo.api.logoutAPI(repo.pref.languageCode,Constants.logout,repo.pref.USER_ID,Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetSettingApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<GetSettingApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                logoutAccount()
                            }
                        } else {
                               showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                         hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

*/
    fun updateProfile() {
        tvUserNameHome.text = "${repo.pref.userName}"
        if (!repo.pref.profile_photo.isNullOrEmpty()) {
            GlobalMethods().loadImagesWithProgressbar(
                    repo.pref.profile_photo,
                    imgvUserProfileHome,
                    R.drawable.ic_user,
                    pbLoadingEditProfile,
                    300,
                    300
            )
        }
    }

    var userPhoto = ""
    fun getProfileAPICall(isLoading: Boolean) {
        if (!isInternetAvailable(getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getProfileAPI(repo.pref.languageCode, repo.pref.USER_ID, Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetProfileApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<GetProfileApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {

                                        repo.pref.userName = (response.body()!!.payload.name)
                                        repo.pref.email = (response.body()!!.payload.email)
                                        repo.pref.mobile = (response.body()!!.payload.mobile)
                                        repo.pref.mobile_country_code = (response.body()!!.payload.mobileCountryCode)

                                        repo.pref.mode_id = response.body()!!.payload.preferences.modeId
                                        repo.pref.music_id = response.body()!!.payload.preferences.musicId
                                        repo.pref.accessible_id = response.body()!!.payload.preferences.accessibleId
                                        if (!response.body()!!.payload.preferences.temperature.isNullOrEmpty()) {
                                            repo.pref.temperature = response.body()!!.payload.preferences.temperature
                                        } else {
                                            repo.pref.temperature = "0"
                                        }

                                        tvUserNameHome.text = "${repo.pref.userName}"
                                        if (!response.body()!!.payload.profilePicture.isNullOrEmpty()) {
                                            userPhoto = ""
                                            userPhoto = response.body()!!.payload.profilePicture
                                            repo.pref.profile_photo = userPhoto
                                            GlobalMethods().loadImagesWithProgressbar(
                                                    response.body()!!.payload.profilePicture,
                                                    imgvUserProfileHome,
                                                    R.drawable.ic_user,
                                                    pbLoadingEditProfile,
                                                    300,
                                                    300
                                            )
                                        }

                                    } else {

                                    }
                                    //baseActivity!!.onBackPressed()
                                }
                            }
                        } else {
                            showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

//MTH
    /*fun getSettingCall() {
        repo.api.getSettingAPI(repo.pref.languageCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetSettingApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        //showLoading()
                    }

                    override fun onNext(response: Response<GetSettingApiResponse>) {
                        //hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        if (!response.body()!!.payload.callUs.isNullOrEmpty()) {
                                            repo.pref.call_us = response.body()!!.payload.callUs
                                        }
                                        if (!response.body()!!.payload.supportEmail.isNullOrEmpty()) {
                                            repo.pref.support_email = response.body()!!.payload.supportEmail
                                        }
                                        if (!response.body()!!.payload.defaultCurrency.isNullOrEmpty()) {
                                            repo.pref.defaultCurrency = response.body()!!.payload.defaultCurrency
                                        }
                                    }
                                }
                            }
                        } else {
                            //  baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        // hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }
*/
    override fun onResume() {
        super.onResume()
        if (repo.pref.isUserLogin){
            userSessionListener()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            dbReference!!.removeEventListener(userSessionListener!!)
        } catch (e: Exception) {
            Log.e("newJobListener", "${e.message}")
        }
    }

/*

    // todo new job firebase listener
    var isSeeionLogout = false
    var dbReference: DatabaseReference? = null
    var userSessionListener: ValueEventListener? = null
    fun userSessionListener() {
        dbReference = FirebaseDatabase.getInstance()
                .getReference("${Constants.devices}/${Constants.devices_Cusomer}/${repo.pref.USER_ID}")

        userSessionListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    var userSession = dataSnapshot.getValue(FirebaseUserData::class.java)

                    if (userSession != null) {
                        var device_id = userSession.device_id

                        if (!device_id.equals(repo.pref.deviceId)) {
                            isSeeionLogout = true
                            logoutAccount()
                        }
                    }
                }
            }
        }

        dbReference!!.addValueEventListener(userSessionListener!!)

    }

    private fun logoutAccount() {
        var languageCode = repo.pref.languageCode

        repo.pref.sharedPref.clearSharedPreference()

        repo.pref.languageCode = languageCode
        replaceFragment(LoginFragment(), false, true)
    }
*/


}