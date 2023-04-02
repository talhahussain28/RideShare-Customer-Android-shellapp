package com.carty.riderapp.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.location_services.GPSTracker
import com.carty.riderapp.ui.base.listeners.BaseView
import com.carty.riderapp.utils.MessageDialog
import com.carty.riderapp.utils.RepoModel
import com.google.gson.Gson
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.home.response.FirebaseUserData
import com.carty.riderapp.ui.signin_up.LoginFragment
import com.carty.riderapp.ui.signin_up.response.SignInApiResponse
import com.onesignal.OneSignal
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import okhttp3.ResponseBody
import org.koin.android.ext.android.inject
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

/**
 *
 */

abstract class BaseActivity : AppCompatActivity(), BaseView {


    //    protected abstract fun initializeViewModel()
//    abstract fun observeViewModel()
//    protected abstract fun initViewBinding()
    val repo: RepoModel by inject()
    var isHomeView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        // AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        gpsTracker = GPSTracker(this)

//        initViewBinding()
//        initializeViewModel()
//        observeViewModel()

        // OneSignal.addSubscriptionObserver(this)

        try {

            OneSignal.idsAvailable { userId, registrationId ->
                Log.e(
                        "one_signal_player",
                        "player_id - userId==>$userId"
                )
                Log.e(
                        "one_signal_player",
                        "player_id - registrationId==>$registrationId"
                )
                if (TextUtils.isEmpty(repo.pref.one_signal_player_id)) {
                    repo.pref.one_signal_player_id = userId
                    Log.e(
                            "one_signal_player",
                            "player_id - one_signal_player_id==>" + repo.pref.one_signal_player_id
                    )
                    if (repo.pref.isUserLogin && !repo.pref.one_signal_player_id.isNullOrEmpty()) {
                        updatePlayerIdAPI(false)
                    }

                }
                if (!userId.isNullOrEmpty()){
                    repo.pref.one_signal_player_id = userId
                }
                if (TextUtils.isEmpty(repo.pref.fcm_token)) {
                    repo.pref.fcm_token =
                            OneSignal.getPermissionSubscriptionState().subscriptionStatus.pushToken
                    Log.e(
                            "one_signal_player",
                            "player_id - repo.pref.fcm_token==>" + repo.pref.fcm_token
                    )
                }
            }


            Log.e(
                    "one_signal_player",
                    "111one_signal_player_id==>" + repo.pref.one_signal_player_id
            )

            if (TextUtils.isEmpty(repo.pref.one_signal_player_id)) {
                repo.pref.one_signal_player_id =
                        OneSignal.getPermissionSubscriptionState().subscriptionStatus.userId
                Log.e(
                        "one_signal_player",
                        "player_id - one_signal_player_id==>" + repo.pref.one_signal_player_id
                )

            }
        } catch (e: Exception) {
            Log.e("one_signal_player", "player_id - Exception = ${e.localizedMessage}")
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////

    fun updatePlayerIdAPI(isLoading: Boolean) {

        if (!isInternetAvailable(getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        var PayIdMap = HashMap<String, String>()
        PayIdMap.put(ApiService.parameters.code, "${repo.pref.languageCode}")
        PayIdMap.put(ApiService.parameters.user_id, "${repo.pref.USER_ID}")
        PayIdMap.put(ApiService.parameters.user_type, "${Constants.customer}")
        PayIdMap.put(ApiService.parameters.player_id, "${repo.pref.one_signal_player_id}")

        repo.api.updatePayerIdAPI(PayIdMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<SignInApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<SignInApiResponse>) {

//                    if (response.isSuccessful) {
//                        if (response.code() == 200) {
//                            if (response.body() != null) {
//
//                            }
//                        }
//                    } else {
//                        hideLoading()
//                       // baseActivity!!.showApiResponseERROR(response.errorBody())
//                    }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    ////////////////////////////////////////////////////////////////////////////////////

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }


    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        // else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        // }
    }

    fun replaceFragment(
            @NonNull fragment: Fragment,
            backStackName: Boolean = false,
            popStack: Boolean = false,
            @IdRes containerViewId: Int = R.id.main_content
    ) {
        Log.e("DSfsdfsdsdf", "" + popStack)
        var transition = supportFragmentManager.beginTransaction()
        transition.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_right
        )
        if (popStack) {
            supportFragmentManager.popBackStack()
        }

//        if (popStack) {
////            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        }

        if (backStackName) {
            transition.addToBackStack("")
        }


        transition.replace(containerViewId, fragment).commitAllowingStateLoss()

    }


    fun addFragment(
            @NonNull fragment: Fragment,
            backStackName: Boolean = false,
            aTAG: String = "",
            @IdRes containerViewId: Int = R.id.main_content
    ) {


        /*  val transition = supportFragmentManager.beginTransaction()
  //        transition.setCustomAnimations(
  //                R.anim.slide_in_from_right,
  //                R.anim.slide_out_from_left,
  //                R.anim.slide_in_from_left,
  //                R.anim.slide_out_from_right
  //        )
          if (backStackName)
              transition.addToBackStack(aTAG)
          transition.addToBackStack(null)
          transition.add(containerViewId, fragment).commitAllowingStateLoss()*/

        var transition = supportFragmentManager.beginTransaction()
        /* transition.setCustomAnimations(
                 R.anim.slide_in_from_right,
                 R.anim.slide_out_from_left,
                 R.anim.slide_in_from_left,
                 R.anim.slide_out_from_right
         )*/


//        if (popStack) {
////            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        }

        transition.addToBackStack(aTAG)

        transition.add(containerViewId, fragment, aTAG).commitAllowingStateLoss()


        /*supportFragmentManager
             .beginTransaction()
             .add(containerViewId, fragment)
             .commit()*/

/*        val transition = supportFragmentManager.beginTransaction()
        transition.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_right
        )
        if (backStackName)
            transition.addToBackStack(aTAG)
        transition.addToBackStack(null)
        transition.add(containerViewId, fragment).commitAllowingStateLoss()*/


    }


    /////////////////////////////////////////////////////////////////////////////////////

    fun setToolbar(
            title: String,
            isBack: Boolean? = false,
            switch: Boolean? = false,
            isLeftImage: Int = -1,
            isRightImage: Int = -1
    ) {
        toolbar.visibility = View.VISIBLE
        // set title
        setToolBarTitle(title)
        // set left Image icon
        if (isBack!!) {
            imgIconBack.setImageDrawable(
                    ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_arrow_left
                    )
            )
            imgIconBack.setOnClickListener {
                onBackPressed()
            }
        } else {
            imgIconBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu))

            imgIconBack.setOnClickListener {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }

    }

    private fun setToolBarTitle(title: String) {
        tvTitle.text = title
    }

    fun drawerOpen() {

        drawer_layout.openDrawer(GravityCompat.START)

    }

    fun drawerClose() {

        drawer_layout.closeDrawer(GravityCompat.START)

    }


    /////////////////////////////////////////////////////////////////////////////////////

    lateinit var gpsTracker: GPSTracker

    val READ_CONTACT = arrayOf(Manifest.permission.READ_CONTACTS)
    val WRITE_EXTERNAL_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val READ_EXTERNAL_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val READ_PHONE_STATE = arrayOf(Manifest.permission.READ_PHONE_STATE)
    val INTERNET = arrayOf(Manifest.permission.INTERNET)
    val ACCESS_NETWORK_STATE = arrayOf(Manifest.permission.ACCESS_NETWORK_STATE)
    val CALL_PHONE = arrayOf(Manifest.permission.CALL_PHONE)

    val EXTERNAL_STORAGE_LIST = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val NETWORK_LIST =
            arrayOf(Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NETWORK_STATE)
    val LOCATION_PERMISSIONS_LIST = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    )

    var appliedPermissions = arrayOf<String>()


    val cameraZoomLavel11_0_f = 11.0f
    val cameraZoomLavel12_0_f = 12.0f
    val cameraZoomLavel13_0_f = 13.0f
    val cameraZoomLavel14_0_f = 14.0f
    val cameraZoomLavel15_0_f = 15.0f
    val cameraZoomLavel16_0_f = 16.0f
    val cameraZoomLavel17_0_f = 17.0f
    val cameraZoomLavel18_0_f = 18.0f
    val cameraZoomLavel19_0_f = 19.0f
    val cameraZoomLavel20_0_f = 20.0f

    val PERMISSION_REQUEST_CODE: Int = 1003
    val LOCATION_REQUEST: Int = 1004
    var tempMap: GoogleMap? = null
    var tempMarker: Marker? = null
    var tempLatLong: LatLng? = null

    var tempCameraZoomLavel = cameraZoomLavel15_0_f
    var tempMarkerImage: Int = 0
    var tempMarkerHeight: Int = 0
    var tempMarkerWidth: Int = 0
    var tempWedgets: Any? = null

    var isForceFullyPermission = false
    var tempIsUpdateLocation = false
    var tempisMarkerPlace = false
    var tempisAnimateCamera = false
    var tempisEnableGPS = false
    var tempisGetAddress = false

    var tempisCalling = false
    var tempContactNumber = "0"


    fun permissionCheckingOne(
            permissionArray: Array<String>,
            isForceFullyPermission: Boolean = true,
            isCalling: Boolean = false,
            contactNumber: String = "0",
            isGetAddress: Boolean = false,
            isMarkerPlace: Boolean = false,
            isAnimateCamera: Boolean = false,
            isEnableGPS: Boolean = false,
            mMap: GoogleMap? = null,//
            mMarker: Marker? = null,//
            latLng: LatLng? = null,
            markerImg: Int = 0,
            markerHeight: Int = 20,
            markerWidth: Int = 20,
            cameraZoomLavel: Float = cameraZoomLavel15_0_f,
            wedgets: Any? = null
    ): Any? {
        appliedPermissions = permissionArray
        this.isForceFullyPermission = isForceFullyPermission

        tempisGetAddress = isGetAddress
        tempisMarkerPlace = isMarkerPlace
        tempisAnimateCamera = isAnimateCamera
        tempisEnableGPS = isEnableGPS
        tempMap = mMap
        tempMarker = mMarker
        tempMarkerImage = markerImg
        tempMarkerHeight = markerHeight
        tempMarkerWidth = markerWidth
        tempCameraZoomLavel = cameraZoomLavel
        tempLatLong = latLng
        tempWedgets = wedgets

        tempisCalling = isCalling
        tempContactNumber = contactNumber

        var actionResponse: Any? = null
        var positive = false
        var negative = false

        permissionArray.forEach {
            if (ActivityCompat.checkSelfPermission(
                            this@BaseActivity,
                            it
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                positive = true
            } else {
                negative = true
            }
        }

        if (!positive && negative) {
            //PERMISSION_NOT_GRANTED
            actionResponse = false


            ActivityCompat.requestPermissions(
                    this@BaseActivity,
                    appliedPermissions,
                    PERMISSION_REQUEST_CODE
            )

        } else {
            // PERMISSION_GRANTED
            if (isMarkerPlace) {
                if (tempMap == null) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Map",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Map")
                } /*else if (tempMarker == null) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Marker",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Marker")
                } */ else if (tempMarkerImage == 0) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Marker image",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Marker image")
                } /*else if (tempMarkerHeight == 0) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Marker height ",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Marker height ")
                } else if (tempMarkerWidth == 0) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Marker width",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Marker width")
                    actionResponse = "Marker width must not be empty"
                } */ else if (tempCameraZoomLavel.equals(0.0)) {
//                    Toast.makeText(
//                            this,
//                            "permissionChecking - provide Map zoom lavel",
//                            Toast.LENGTH_LONG
//                    ).show()
                    Log.e("permissionChecking", "provide Map zoom lavel")
                } else {
                    actionResponse = placeMarkerOnMapWithSize(
                            tempMap!!,
                            tempMarker,
                            tempLatLong,
                            tempMarkerImage,
                            tempMarkerHeight,
                            tempMarkerWidth,
                            tempisMarkerPlace,
                            tempCameraZoomLavel
                    )
//                    if (tempisMarkerPlace) {
//                        setMarker()
//                    }
                    tempMarker = actionResponse
                }
                //todo for set Address
                if (tempisEnableGPS || tempisGetAddress) {
                    if (gpsTracker.canGetLocation()) {
                        if (gpsTracker.latitude == 0.0 || gpsTracker.longitude == 0.0) {
                            gpsTracker = GPSTracker(this)

                            Handler().postDelayed({
                                displayLocationSettingsRequest(this@BaseActivity)
                            }, 1500)

                        } else {
                            Log.e(
                                    "getAddressByLatLong",
                                    "111 gpsTracker  LATITUDE= ${gpsTracker!!.latitude},LONGITUDE= ${gpsTracker!!.longitude}"
                            )
                            if (tempisGetAddress) {
                                setAddress()
                            }
                            //setAddressFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude())
                        }
                    } else {
                        displayLocationSettingsRequest(this@BaseActivity)
                    }
                }
            } else if (tempisEnableGPS || tempisGetAddress) {
                if (gpsTracker.canGetLocation()) {
                    if (gpsTracker.latitude == 0.0 || gpsTracker.longitude == 0.0) {
                        gpsTracker = GPSTracker(this)
                        Handler().postDelayed({
                            displayLocationSettingsRequest(this@BaseActivity)
                        }, 1500)
                    } else {
                        Log.e(
                                "getAddressByLatLong",
                                "111 gpsTracker  LATITUDE= ${gpsTracker!!.latitude},LONGITUDE= ${gpsTracker!!.longitude}"
                        )
                        setAddress()
                        //setAddressFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude())
                    }
                } else {
                    displayLocationSettingsRequest(this@BaseActivity)
                }


            } else if (isCalling) {
                if (contactNumber.equals("0", ignoreCase = true)) {
                    Toast.makeText(
                            this,
                            "permissionChecking - provide Contact number",
                            Toast.LENGTH_LONG
                    ).show()
                    Log.e("permissionChecking", "provide Contact number")
                } else {
                    call_action(tempContactNumber)
                }
            } else {
                actionResponse = true
            }
        }

        return actionResponse
    }

    fun requestPermissionsMultiple() {

        ActivityCompat.requestPermissions(
                this@BaseActivity,
                appliedPermissions,
                PERMISSION_REQUEST_CODE
        )

    }

    @SuppressLint("MissingPermission")
    fun call_action(phone: String) {
        Log.e(
                "call_action", "$phone<=phone -"
        )
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phone")
        startActivity(callIntent)
    }


    public fun placeMarkerOnMapWithSize(
            mMap: GoogleMap,
            mMarker: Marker? = null,
            latLng: LatLng?,
            markerImg: Int? = null,
            markerHeight: Int,
            markerWeight: Int,
            isMarkerPlace: Boolean,
            cameraZoomLavel: Float = cameraZoomLavel15_0_f
    ): Marker? {

        tempisMarkerPlace = isMarkerPlace

        var tempImag = 0
        var latLngs = LatLng(0.0, 0.0)

        if (latLng == null) {
            Log.e("placeMarkerOnMapWith", "BaseActivty - latLng == null")
            latLngs = LatLng(gpsTracker.latitude, gpsTracker.longitude)
            Log.e("placeMarkerOnMapWith", "BaseActivty - gpsTracker - ${gpsTracker.latitude},${gpsTracker.longitude}")
        } else {
            latLngs = latLng!!
            Log.e("placeMarkerOnMapWith", "BaseActivty - ${latLngs!!.latitude},${latLngs!!.longitude}")
            if (latLngs.latitude == 0.0 && latLngs.latitude == 0.0) {
                latLngs = LatLng(gpsTracker.latitude, gpsTracker.longitude)
            }
            Log.e("placeMarkerOnMapWith", "BaseActivty - ${latLngs!!.latitude},${latLngs!!.longitude}")
        }

        //var latLngs = latLng


        var cameraZoomLavels = cameraZoomLavel15_0_f
        if (markerImg == null) {
            tempImag = R.drawable.ic_location
        } else {
            tempImag = markerImg
        }
        val bitmapdraw = ContextCompat.getDrawable(this, tempImag) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, markerWeight, markerHeight, false)

        val markerOptions =
                MarkerOptions().position(latLngs!!).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

        var marker: Marker? = null
        try {
            if (mMarker == null) {
                marker = mMap!!.addMarker(markerOptions)
            } else {
                marker!!.position = latLngs
            }
        } catch (e: Exception) {
            Log.e("placeMarkerOnMapWith", "Exception = ${e.localizedMessage}")
            e.printStackTrace()
        }

        //var marker = mMarker
        //tempMarker = mMap!!.addMarker(markerOptions)

        try {
            marker.let {
                marker!!.position = latLng
                marker!!.title = ""
            }
        } catch (e: Exception) {
            marker?.let {
                marker = mMap!!.addMarker(markerOptions)
            }
        }


        //val titleStr = getAddress(location)  // add these two lines
//        markerOptions.title("Current location")

//        mMap!!.addMarker(markerOptions)

        if (tempisAnimateCamera) {
            val cameraPosition = CameraPosition.fromLatLngZoom(latLngs, cameraZoomLavels)
            val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap?.animateCamera(cu)
        }
        return marker
    }

    public fun placeMarkerOnMapWithOriginal(
            mMap: GoogleMap,
            mMarker: Marker?,
            latLng: LatLng?,
            markerImg: Int? = null,
            cameraZoomLavel: Float = cameraZoomLavel15_0_f
    ): Marker? {
        var tempImag = R.drawable.ic_location
        var latLngs = LatLng(gpsTracker.latitude, gpsTracker.longitude)
        var cameraZoomLavels = cameraZoomLavel15_0_f
        if (markerImg == null) {
            tempImag = R.drawable.ic_location
        } else {
            tempImag = markerImg
        }
//        val bitmapdraw = ContextCompat.getDrawable(this, tempImag) as BitmapDrawable
//        val b = bitmapdraw.bitmap
//        val smallMarker = Bitmap.createScaledBitmap(b, markerWeight, markerHeight, false)
        var smallMarker = BitmapDescriptorFactory.fromResource(markerImg as Int)


        val markerOptions =
                MarkerOptions().position(latLngs).icon(smallMarker)

        var marker: Marker? = null
        try {
            if (mMarker == null) {
                marker = mMap!!.addMarker(markerOptions)
            } else {
                marker!!.position = latLngs
            }
        } catch (e: Exception) {
            Log.e("placeMarkerOnMapWith", "Exception = ${e.localizedMessage}")
            e.printStackTrace()
        }

        //var marker = mMarker
        //tempMarker = mMap!!.addMarker(markerOptions)

        try {
            marker.let {
                marker!!.position = latLng
                marker!!.title = ""
            }
        } catch (e: Exception) {
            marker?.let {
                marker = mMap!!.addMarker(markerOptions)
            }
        }


        //val titleStr = getAddress(location)  // add these two lines
//        markerOptions.title("Current location")

//        mMap!!.addMarker(markerOptions)

        if (tempisAnimateCamera) {
            val cameraPosition = CameraPosition.fromLatLngZoom(latLngs, cameraZoomLavels)
            val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap?.animateCamera(cu)
        }
        return marker
    }


    private fun displayLocationSettingsRequest(context: Context?) {

        val googleApiClient = GoogleApiClient.Builder(context!!)
                .addApi(LocationServices.API).build()
        googleApiClient.connect()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2.toLong()
        val builder =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val pendingResult: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        pendingResult.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    Log.e("BaseActivity", "All location settings are satisfied.")
                    //iniMap()
//                    if (tempIsUpdateLocation) {
                    //setCurrentLocation()
//                    }
                    if (gpsTracker.latitude == 0.0 || gpsTracker.longitude == 0.0) {
                        Handler().postDelayed({
                            this.let {
                                requestPermissionsMultiple()
                            }
                        }, 500)

                    } else {
                        if (tempisGetAddress) {
                            setAddress()
                        }
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    Log.e(
                            "BaseActivity",
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                    )
                    try { // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().

                        status.startResolutionForResult(
                                context as Activity?,
                                LOCATION_REQUEST
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("BaseActivity", "PendingIntent unable to execute request.")
                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    Log.e(
                            "BaseActivity",
                            "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                    )
                }
            }
        }

    }


    fun setAddress() {

        if (tempWedgets != null) {

            var textview: Any? = null
            var address = ""

            if (tempLatLong == null) {

                Log.e(
                        "getAddressByLatLong",
                        "222 gpsTracker  LATITUDE= ${gpsTracker!!.latitude},LONGITUDE= ${gpsTracker!!.longitude}"
                )
                address = getAddressByLatLong(
                        this,
                        gpsTracker!!.latitude,
                        gpsTracker!!.longitude
                )
                var addressHM = HashMap<String, String>()

                addressHM.put("address", address)
                addressHM.put("latitude", "${gpsTracker!!.latitude}")
                addressHM.put("longitude", "${gpsTracker!!.longitude}")
                if (anyInterface != null) {
                    (anyInterface!! as CommonInterface).returnValues(addressHM, address)
                }
            } else {
                Log.e(
                        "getAddressByLatLong",
                        "tempLatLong  LATITUDE= ${tempLatLong!!.latitude},LONGITUDE= ${tempLatLong!!.longitude}"
                )
                address = getAddressByLatLong(
                        this,
                        tempLatLong!!.latitude,
                        tempLatLong!!.longitude
                )
            }
            if (!address.isNullOrEmpty()) {
                if (tempWedgets is TextView) {
                    textview = tempWedgets as TextView
                    textview.text = "$address"
                } else if (tempWedgets is EditText) {
                    textview = tempWedgets as EditText
                    textview.setText("$address")

                }
            }
        } else {
            Log.e(
                    "getAddressByLatLong",
                    "gpsTracker  tempWedgets= is null"
            )
        }

    }


    private fun getAddressByLatLong(context: Context, LATITUDE: Double, LONGITUDE: Double): String {
        val stringBuilder = StringBuilder("")
        try {
            Log.e(
                    "getAddressByLatLong",
                    "setAddressFromLocation  LATITUDE= ${LATITUDE},LONGITUDE= ${LONGITUDE}"
            )
            val geoCoder = Geocoder(context, Locale.getDefault())
            val list = geoCoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (list.isEmpty())
                return ""
            val returnAddress = list.get(0)

            for (i in 0..returnAddress.maxAddressLineIndex) {
                stringBuilder.append(returnAddress.getAddressLine(i)).append("\n")
            }
        } catch (e: Exception) {
            Log.e("getAddressByLatLong", "exception = ${e.printStackTrace()}")
        }

        return stringBuilder.toString()
    }

    interface CommonInterface {
        fun returnValues(any: Any, type: String)
    }


    var TAG = "BaseActivity"
    var grantResultCode = 1001
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSION_REQUEST_CODE) {
            return
        }
        Log.e(
                TAG,
                "onRequestPermissionsResult = requestCode->${requestCode}, permissions->${permissions}, grantResults->${grantResults} "
        )
        grantResultCode = 1001
        grantResults.forEach {
            Log.e(
                    TAG,
                    "onRequestPermissionsResult =  grantResults->${it}''''''''''''''''"
            )

            grantResultCode = it
        }

        Log.e(
                TAG,
                "onRequestPermissionsResult =  grantResultCode->${grantResultCode}''''''''''''''''"
        )
        permissions.forEach {
            Log.e(
                    TAG,
                    "onRequestPermissionsResult =  permissions ** ->${it}''''''''''''''''"
            )

        }
        if (grantResultCode == -1) {
            // denied and don't ask again
            if (isForceFullyPermission) {
                requestPermissionsMultiple()
            } else {
                fordwardRequestPermissionsResultToFragment(requestCode, permissions, grantResults)
            }

        } else {
            // accepted

            if (permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION) || permissions.contains(
                            Manifest.permission.ACCESS_FINE_LOCATION
                    )
            ) {
                Log.e(
                        TAG,
                        "onRequestPermissionsResult = ACCESS_COARSE_LOCATION || ACCESS_FINE_LOCATION Granted"
                )
                gpsTracker = GPSTracker(this)
                if (gpsTracker.canGetLocation()) {
                    Log.e(
                            TAG,
                            "onRequestPermissionsResult = gpsTracker.latitude->${gpsTracker.latitude}, gpsTracker.longitude->${gpsTracker.longitude}"
                    )

                    if (gpsTracker.latitude == 0.0 || gpsTracker.longitude == 0.0) {
                        displayLocationSettingsRequest(this@BaseActivity)
                        return
                    } else {

                        if (tempisGetAddress) {
                            setAddress()
                        }

                        if (tempisMarkerPlace) {
                            setMarker()
                        }
                    }
                } else {
                    Log.e(
                            TAG,
                            "onRequestPermissionsResult = gpsTracker cant GetLocation()"
                    )
                    displayLocationSettingsRequest(this@BaseActivity)
                }


            } else if (permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) || permissions.contains(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            ) {
                Log.e(
                        TAG,
                        "onRequestPermissionsResult = WRITE_EXTERNAL_STORAGE || READ_EXTERNAL_STORAGE  Granted"
                )
                fordwardRequestPermissionsResultToFragment(requestCode, permissions, grantResults)
            } else if (permissions.contains(Manifest.permission.READ_CONTACTS)) {
                Log.e(TAG, "onRequestPermissionsResult = READ_CONTACTS  Granted")
                fordwardRequestPermissionsResultToFragment(requestCode, permissions, grantResults)
            } else if (permissions.contains(Manifest.permission.CALL_PHONE)) {
                Log.e(TAG, "onRequestPermissionsResult = READ_CONTACTS  Granted")
                call_action(tempContactNumber)
            } else if (permissions.contains(Manifest.permission.CAMERA)) {
                Log.e(TAG, "onRequestPermissionsResult = CAMERA  Granted ")
                fordwardRequestPermissionsResultToFragment(requestCode, permissions, grantResults)
            } else {
                fordwardRequestPermissionsResultToFragment(requestCode, permissions, grantResults)
            }

        }


    }


    fun fordwardRequestPermissionsResultToFragment(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        for (fragment in supportFragmentManager.fragments) {
            Log.e(
                    TAG,
                    "onRequestPermissionsResult = fragment->${fragment.tag}"
            )
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(
                TAG,
                "onActivityResult = requestCode->${requestCode}, resultCode->${resultCode}, data->${data.toString()} "
        )
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUEST || requestCode == PERMISSION_REQUEST_CODE) {
                gpsTracker = GPSTracker(this)
                if (tempisGetAddress) {
                    //setAddress()
                    //displayLocationSettingsRequest(this@BaseActivity)
                }
                requestPermissionsMultiple()
            } else {
                fordwardActivityResultToFragment(requestCode, resultCode, data)
            }

        } else {
            if (isForceFullyPermission) {
                requestPermissionsMultiple()
//                requestPermissionsMultipleOne()
            } else {
                fordwardActivityResultToFragment(requestCode, resultCode, data)
            }
        }


    }

    fun fordwardActivityResultToFragment(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            Log.e(
                    TAG,
                    "onActivityResult = fragment->${fragment.tag}"
            )
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun setMarker() {
        tempLatLong = LatLng(gpsTracker.latitude, gpsTracker.longitude)
        tempMarker = placeMarkerOnMapWithSize(
                tempMap!!,
                tempMarker,
                tempLatLong,
                tempMarkerImage,
                tempMarkerHeight,
                tempMarkerWidth,
                tempisMarkerPlace,
                tempCameraZoomLavel
        )

    }


    var anyInterface: Any? = null
    fun setInterface(anyInterface: Any) {
        this.anyInterface = anyInterface
    }

    //////////////////////////////////////////////////////////////

    open fun compressImage(filePath: String?): String? {

        //String filePath = getRealPathFromURI(imageUri);
        var scaledBitmap: Bitmap? = null
        val options =
                BitmapFactory.Options()

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp =
                BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

//      max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 512.0f
        val maxWidth = 512.0f
        var imgRatio = actualWidth / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(
                    actualWidth,
                    actualHeight,
                    Bitmap.Config.ARGB_8888
            )
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
                bmp,
                middleX - bmp.width / 2,
                middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
        )

//      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val orientation: Int = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap!!, 0, 0,
                    scaledBitmap!!.width, scaledBitmap!!.height, matrix,
                    true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filename: String = getFilename()
        try {
            out = FileOutputStream(filename)

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    open fun getFilename(): String {
        val file = File(
                Environment.getExternalStorageDirectory().path,
                ".Fabra/Images"
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
    }


    open fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                    Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio =
                    Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun setStatusBar(color: Int, isTextColorWhite: Boolean? = false) {
        if (isTextColorWhite!!) {
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView()
                            .getSystemUiVisibility() and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            ) //set status text  light
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color)
        }
    }


    lateinit var timer: CountDownTimer
    fun msgDialog(msg: String, dialogType: String? = Constants.ERROR) {
        var dialogMsg = MessageDialog.getInstance()
        val bundle = Bundle()
        bundle.putString("tvMsgText", msg)
        bundle.putString("okTxt", "OK")
        bundle.putString("msgType", "" + dialogType)

        dialogMsg.arguments = bundle
        if (dialogType.equals(Constants.ERROR)) {
            setStatusBar(ContextCompat.getColor(baseContext!!, R.color.colorPrimary))
        } else {
            setStatusBar(ContextCompat.getColor(baseContext!!, R.color.colorPrimary))
        }

        if (dialogMsg.isAdded) {
            return
        }

        dialogMsg.show(supportFragmentManager, "")
        timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                timer!!.cancel()
                if (dialogMsg.isVisible)
                    dialogMsg!!.dismiss()
            }
        }.start()
        dialogMsg.setListener(object : MessageDialog.OnClick {
            override fun set(ok: Boolean) {
                dialogMsg.dismiss()
                if (dialogMsg.isVisible)
                    timer!!.cancel()

            }
        })
    }


    internal var dialog: Dialog? = null

    @Synchronized
    fun showLoading() {
        if (dialog == null) {
            dialog = Dialog(this)
        }
        dialog!!.setContentView(R.layout.progress_loader)
        // ((TextView) dialog.findViewById(R.id.tvMsg)).setText(getString(R.string.text_please_wait));
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.show()
    }

    fun hideLoading() {
        if ((dialog != null) and dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    //    fun showApiResponseERROR(errorBody : String?)
    fun showApiResponseERROR(errorBody: ResponseBody?) {
//        Log.e("TAG", "showApiResponseERROR-> errorString ${errorBody?.string()}")
//        Log.e("TAG", "showApiResponseERROR-> errorBody ${errorBody}")
        try {
            val error = Gson().fromJson(
                    errorBody?.string(),
                    ErrorMessage::class.java
            ).error
            msgDialog("${error.message}")
            //GlobalMethods.Dialog(this@BaseActivity, error.message)
        } catch (ex: Exception) {
            Log.e("TAG", "showApiResponseERROR-> server error Exception ${ex.message}")
//            GlobalMethods.Dialog(
//                    this@BaseActivity,
//                    repo.labelPref.getValue(PrefKeys.INFO_MSG_LANG_SERVER_ISSUE_TRY_AGAIN)
//            )
            msgDialog("Something went wrong! please try again")
        }
    }


    fun isInternetAvailable(msg: String): Boolean {

        val connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        var status = false
        status = activeNetwork?.isConnectedOrConnecting == true

        if (!status) {
            //Toast.makeText(ctx,"No Internet Connection",Toast.LENGTH_SHORT).show();
            //GlobalMethods.Dialog(this, msg)
            msgDialog(msg)
        }

        return status
    }

    //////////////////////////////////////////////////////////////////////////////////////////////


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

    fun logoutAccount() {
        var languageCode = repo.pref.languageCode
        var one_signal_player_id = repo.pref.one_signal_player_id
        var fcm_token = repo.pref.fcm_token
       // OneSignal.setSubscription(false)
        repo.pref.sharedPref.clearSharedPreference()

        repo.pref.languageCode = languageCode
        repo.pref.one_signal_player_id = one_signal_player_id
        repo.pref.fcm_token = fcm_token

        replaceFragment(LoginFragment(), false, true)

       /* var intent = Intent(baseActivity!!, LoginActivity::class.java)
        startActivity(intent)
        baseActivity!!.finish()*/
    }

}
