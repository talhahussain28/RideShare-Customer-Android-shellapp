package com.carty.riderapp.newFragmentFlow

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.carty.riderapp.R
import com.carty.riderapp.common.*
import com.carty.riderapp.model.map_poliline.Result
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.HomeFragment
import com.carty.riderapp.ui.home.address.SelectAddressFragment
import com.carty.riderapp.ui.home.driver_details.DriverDetailFragment
import com.carty.riderapp.ui.home.model.TripStatusTracking
import com.carty.riderapp.ui.home.notification.NotificationFragment
import com.carty.riderapp.ui.home.order_place.OrderPlaceFragment
import com.carty.riderapp.ui.home.order_place.model.DriverLocationModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_accept_job.view.*
import kotlinx.android.synthetic.main.bottom_dropoff_job.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragmentNew.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragmentNew : BaseFragment(),
    OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraIdleListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var mMap: GoogleMap? = null
    var homeView: View? = null
    var edtpickup: TextView? =null
    var edtDropoff: TextView? =null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_map_new, container, false)

        edtpickup = homeView!!.findViewById(R.id.appCompatEditTextLocation)
        edtDropoff = homeView!!.findViewById(R.id.appCompatEditTextDropOff)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return homeView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtpickup!!.isSingleLine = true
        edtpickup!!.ellipsize = TextUtils.TruncateAt.END

        edtDropoff!!.isSingleLine = true
        edtDropoff!!.ellipsize = TextUtils.TruncateAt.END

        imgIconBack.setOnClickListener {
            baseActivity!!.drawerOpen()
        }

        imgvLocateMe.setOnClickListener {
            if (!isMarkersSeted) {
                setCustomerMarker("imgvLocateMe btn ", isSetMarker = true, isMapCler = false)
            }
        }

        edtpickup!!.setOnClickListener {
            try {
                var frag = SelectAddressFragment()
                frag.setTargetFragment(this, Constants.ADDRESSPICKUP)
                baseActivity!!.addFragment(frag, true)
            } catch (e: Exception) {
                Log.e("Exception Here", "Exception Here")
            }
        }
        edtDropoff!!.setOnClickListener {
            Log.e("CLicked Here", "CLicked Here")
            try {
                var frag = SelectAddressFragment()
                frag.setTargetFragment(this, Constants.ADDRESSDROPOFF)
                baseActivity!!.addFragment(frag, true)
            } catch (e: Exception) {
                Log.e("Exception Here", "Exception Here")
            }
        }
        //talha
        tvConfirmForOrder.setOnClickListener {
            Log.e("DGSdgsdsg", "tvConfirmForOrder - pickuplatlng ==>${pickupLat},${pickupLong}")
            Log.e("DGSdgsdsg", "tvConfirmForOrder - dropofflatlng==>${DropOffLat},${DropOffLong}")
            pickupAddress = edtpickup!!.text.toString()
            if (pickupAddress.isNullOrEmpty()) {
                msgDialog("Please select pickup address")
            } else if (pickupAddress.equals("Pickup ?")) {
                msgDialog("Please select pickup address")
            } else if (DropOffAddress.isNullOrEmpty()) {
                msgDialog("Please select dropoff address")
            } else if (DropOffAddress.equals("Drop off?")) {
                msgDialog("Please select dropoff address")
            } else {
                Log.e("CLicked Here", "CLicked Here")
                var placeOrder = OrderPlaceFragment()
                var bundle = Bundle()
                bundle.putString("pickupAddress", edtpickup!!.text.toString())
                bundle.putString("dropoffAddress", edtDropoff!!.text.toString())
                bundle.putString("pickupLat", pickupLat)
                bundle.putString("pickupLong", pickupLong)
                bundle.putString("DropOffLat", DropOffLat)
                bundle.putString("DropOffLong", DropOffLong)
                bundle.putString("trip_Id", Constants.none)
                bundle.putString("trip_status", Constants.none)
                isMarkersSeted = false
                onAdSelect = true
                placeOrder.arguments = bundle
                DropOffAddress = ""
                mMap!!.clear()
                replaceFragment(placeOrder, true, false)
            }
        }

        imgvSwipeAddress.setOnClickListener {
            var pickupAdds = edtpickup!!.text.toString()
            var dropoffAdds = edtDropoff!!.text.toString()

            if (pickupAdds.isNullOrEmpty()) {
                msgDialog("Please select pickup address")
            } else if (pickupAdds.contains("Pickup", ignoreCase = true)) {
                msgDialog("Please select pickup address")
            } else if (dropoffAdds.isNullOrEmpty()) {
                msgDialog("Please select dropoff address")
            } else if (dropoffAdds.contains("Drop off", ignoreCase = true)) {
                msgDialog("Please select dropoff address")
            } else {
                swipeAddress()
            }
        }

        tcCancelWaitingTripHome.setOnClickListener {
            dialogRejectJob()

        }

       // getProfileAPICall(true)

        ongoingJobFlow()

    }


    private fun swipeAddress() {
        var pickupAdds = edtpickup!!.text.toString()
        var pickupAddsLat = pickupLat
        var pickupAddsLng = pickupLong

        var dropoffAdds = edtDropoff!!.text.toString()
        var dropoffAddsLat = DropOffLat
        var dropoffAddsLng = DropOffLong


        edtpickup!!.text = ("${dropoffAdds}")
        edtDropoff!!.text = ("${pickupAdds}")

        pickupLat = dropoffAddsLat
        pickupLong = dropoffAddsLng
        DropOffLat = pickupAddsLat
        DropOffLong = pickupAddsLng

        var pickUpLatlng = LatLng(pickupLat.toDouble(), pickupLong.toDouble())
        var dropOffLatlng = LatLng(DropOffLat.toDouble(), DropOffLong.toDouble())

        if (pickUpLatlng != null && dropOffLatlng != null) {
            drawpoliline(pickUpLatlng, dropOffLatlng)
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
        baseActivity!!.isHomeView = true
        //getNewCurrentJobAPICall(false, "onResume")
    }


    private fun setToolbar() {

        /* imgIconBack.setImageDrawable(
                 ContextCompat.getDrawable(
                         baseActivity!!,
                         R.drawable.ic_menu
                 )
         )
         imgIconBack.setOnClickListener {  }*/


        imgNotification.setOnClickListener {
            replaceFragment(NotificationFragment(), true, false)
        }

        frNotification.visibility = View.VISIBLE

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
        tvTitle.text = "Map"

    }

    var pickupAddress = ""
    var DropOffAddress = ""
    var pickupLat = "0"
    var pickupLong = "0"
    var DropOffLat = "0"
    var DropOffLong = "0"
    var isMarkersSeted = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADDRESSDROPOFF) {
                onAdSelect = true

                var address = data!!.getStringExtra("address")
                DropOffLat = data!!.getStringExtra("addressLatitude")
                DropOffLong = data!!.getStringExtra("addressLongitude")

                edtDropoff!!.text = address
                DropOffAddress = address

                if (pickupAddress.isNullOrEmpty()) {
                    pickupLat = "${baseActivity!!.gpsTracker!!.latitude}"
                    pickupLong = "${baseActivity!!.gpsTracker!!.longitude}"
                } else {
                    if (pickupLat.equals("0", ignoreCase = true) || pickupLat.equals("0.0", ignoreCase = true)) {
                        pickupLat = "${baseActivity!!.gpsTracker!!.latitude}"
                        pickupLong = "${baseActivity!!.gpsTracker!!.longitude}"
                    }

                }

                var pickUpLatlng = LatLng(pickupLat.toDouble(), pickupLong.toDouble())
                var dropOffLatlng = LatLng(DropOffLat.toDouble(), DropOffLong.toDouble())

                if (pickUpLatlng != null && dropOffLatlng != null) {

                    drawpoliline(pickUpLatlng, dropOffLatlng)
                }

                Log.e("Address", "addressHome===>" + address)

            }
            if (requestCode == Constants.ADDRESSPICKUP) {

                onAdSelect = true

                var address = data!!.getStringExtra("address")
                pickupLat = data!!.getStringExtra("addressLatitude")
                pickupLong = data!!.getStringExtra("addressLongitude")

                edtpickup!!.text = address
                pickupAddress = address
                Log.e("Address", "addressHome===>" + address)

                if (!DropOffAddress.isNullOrEmpty()) {

                    var pickUpLatlng = LatLng(pickupLat.toDouble(), pickupLong.toDouble())
                    var dropOffLatlng = LatLng(DropOffLat.toDouble(), DropOffLong.toDouble())

                    if (pickUpLatlng != null && dropOffLatlng != null) {
                        drawpoliline(pickUpLatlng, dropOffLatlng)
                    }
                }

            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        mMap!!.setOnMapLoadedCallback(this)
        mMap!!.setOnCameraIdleListener(this)
        mMap!!.setOnCameraMoveListener(this)
    }


    var customerMarker: Marker? = null
    override fun onMapLoaded() {
        if (mMap != null) {

            var curLatLng = LatLng(baseActivity!!.gpsTracker.latitude, baseActivity!!.gpsTracker.longitude)
//            customerMarker = baseActivity!!.placeMarkerOnMapWithSize(mMap!!, customerMarker, curLatLng, R.drawable.ic_pin_home,
//                    50, 35, baseActivity!!.cameraZoomLavel15_0_f)

            //customerMarker.position = LatLng(baseActivity!!.gpsTracker.latitude, baseActivity!!.gpsTracker.longitude)
            // mMap!!.clear()


            if (jobStatus.equals(Constants.PENDING, ignoreCase = true) ||
                jobStatus.equals(Constants.none, ignoreCase = true)) {
                setCustomerMarker("onMapLoaded ", false)
            }

        }
    }

    fun setCustomerMarker(callFrom: String, isSetMarker: Boolean = false, isMapCler: Boolean = true) {
        Log.e("setCustomerMarker", "callFrom = ${callFrom} && isSetMarker = ${isSetMarker} && isMarkersSeted = ${isMarkersSeted}")
        if (isSetMarker && !isMarkersSeted) {
            if (isMapCler) {
                mMap!!.clear()
            }

            if (customerMarker != null) {
                customerMarker!!.remove()
                customerMarker = null
            }

            permissionCheckingOne(
                baseActivity!!.LOCATION_PERMISSIONS_LIST,
                isEnableGPS = true,
                isMarkerPlace = true,
                isAnimateCamera = true,
                mMap = mMap,
                mMarker = customerMarker,
                markerImg = R.drawable.ic_pin_home,
                isGetAddress = true,
                wedgets = edtpickup,
                markerHeight = 85,
                markerWidth = 50
            )
            /*permissionCheckingOne(
                    baseActivity!!.LOCATION_PERMISSIONS_LIST,
                    isEnableGPS = true,
                    isMarkerPlace = true,
                    isAnimateCamera = true,
                    mMap = mMap,
                    mMarker = customerMarker,
                    markerImg = R.drawable.ic_pin_home,
                    markerHeight = 85,
                    markerWidth = 50
            )*/
        }
    }


    override fun onCameraMove() {

    }

    var isFirstTimeOpen = true
    var onAdSelect = true
    var currentLatlngs: LatLng? = null
    override fun onCameraIdle() {

        Log.e("onCameraIdle", "onAdSelect=$onAdSelect && isMarkersSeted=$isMarkersSeted")
        if (!onAdSelect && !isMarkersSeted) {
            pickupLat = "${mMap!!.cameraPosition.target.latitude}"
            pickupLong = "${mMap!!.cameraPosition.target.longitude}"
            Log.e("onCameraIdle", "pickupLatlng=$pickupLat,$pickupLong")

            currentLatlngs = LatLng(
                mMap!!.cameraPosition.target.latitude,
                mMap!!.cameraPosition.target.longitude
            )
            if (!isFirstTimeOpen) {
                try {
                    setAddressFromLocations(currentLatlngs!!)
                    setCurrentMarker(currentLatlngs!!)
                } catch (e: Exception) {
                    Log.e("onCameraIdle", "Exception=${e.localizedMessage}")
                }


            }
            isFirstTimeOpen = false
//        edtpickup.text = address
//        pickupAddress = address
        } else {
            Handler().postDelayed({
                onAdSelect = false
                isFirstTimeOpen = false
            }, 2000)
        }
    }

    private fun setCurrentMarker(curentlatlngs: LatLng) {

        if (customerMarker == null) {
            Log.e("onCameraIdle", "customerMarker == null")
            mMap!!.clear()
            customerMarker = baseActivity!!.placeMarkerOnMapWithSize(
                mMap!!, customerMarker, curentlatlngs, R.drawable.ic_pin_home,
                85, 50, false, baseActivity!!.cameraZoomLavel15_0_f
            )
        } else {
            Log.e("onCameraIdle", "customerMarker != null")
            customerMarker!!.position = curentlatlngs
        }

    }

    var isCameraIdle = false
    var isAddSelect = false
    private fun setAddressFromLocations(mCenterLatLong: LatLng) {
        mCenterLatLong?.let {
            val address = getAddressByLatLongs(
                baseActivity!!,
                mCenterLatLong!!.latitude,
                mCenterLatLong!!.longitude
            )
            Handler().postDelayed({
                isCameraIdle = true
//                binding.fDashboardTvFromWhere.setText(address)
                // _progressState.value = ProgressState.HIDE
                /* imgv_ClearAddress.let {
                     imgv_ClearAddress.visibility = View.VISIBLE
                 }*/
                if (edtpickup == null){
                    return@postDelayed
                }
                isAddSelect = true
                edtpickup!!.text = address.trim()
                pickupAddress = address.trim()
            }, 500)
        }

        Handler().postDelayed({
            isCameraIdle = false
        }, 2000)


    }

    private fun getAddressByLatLongs(
        context: Context,
        LATITUDE: Double,
        LONGITUDE: Double
    ): String {
        val stringBuilder = StringBuilder("")
        try {
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

    //////////////////////////////////////////////////////////////////////////////////////////

    var userPhoto = ""
   /* fun getProfileAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
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

                                    if (!response.body()!!.payload.profilePicture.isNullOrEmpty()) {
                                        userPhoto = ""
                                        userPhoto = response.body()!!.payload.profilePicture
                                        repo.pref.profile_photo = userPhoto
                                    }
                                    if (activity != null) {
                                        (activity as MainActivity)!!.updateProfile()
                                    }

                                    //MainActivity().updateProfile()

                                } else {

                                }
                                //baseActivity!!.onBackPressed()
                            }
                        }
                    } else {
                        // baseActivity!!.showApiResponseERROR(response.errorBody())
                    }

                    //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }*/


    /////////////////////////////////////////////////////////////////////////////////////////////
    // TODO Map relevent flow
    var gesture: Boolean = false
    private var polyLineList: List<LatLng>? = null
    internal lateinit var polylineOptions: PolylineOptions
    internal var blackPolyline: Polyline? = null
    internal var greyPolyLine: Polyline? = null
    lateinit var cu: CameraUpdate
    internal lateinit var blackPolylineOptions: PolylineOptions

    fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng) {
//        runningdriver = false

        Log.e("DGSdgsdsg", "pickuplatlng==>${pickuplatlng.latitude},${pickuplatlng.longitude}")
        Log.e("DGSdgsdsg", "dropofflatlng==>${dropofflatlng.latitude},${dropofflatlng.longitude}")

        val apiInterface: ApiService

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
        apiInterface = retrofit.create<ApiService>(ApiService::class.java!!)

        apiInterface.getDirectionsSingle(
            "driving",
            "less_driving",
            //pickuplocation, dropOfflocation,
            pickuplatlng.latitude.toString() + "," + pickuplatlng.longitude,
            dropofflatlng.latitude.toString() + "," + dropofflatlng.longitude,
            baseActivity!!.getResources().getString(R.string.map_key)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : SingleObserver<Result> {

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(result: Result) {
                        if (result.getRoutes() == null) {
                            Handler().postDelayed({
                                onAdSelect = false
                            }, 2000)
                            return
                        }
                        if (result.getRoutes().size == 0) {
                            Handler().postDelayed({
                                onAdSelect = false
                            }, 2000)
                            return
                        }


                        val routeList = result.getRoutes()
                        Log.e("DGSdgsdsg", "routeList.size= ${routeList.size}")

                        var dur = routeList[0].legs[0].duration.text
                        bsllAcceptJob.tvESTArrivalTime.text = "Driver will pick you up at ${dur}"

                        for (route in routeList) {
                            val polyLine = route.getOverviewPolyline().getPoints()
                            polyLineList = decodePoly(polyLine)

                            if (greyPolyLine != null) {
                                greyPolyLine!!.remove()
                            }
                            if (blackPolyline != null) {
                                blackPolyline!!.remove()
                            }

                            drawPolyLineAndAnimateCar(pickuplatlng, dropofflatlng)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Handler().postDelayed({
                            onAdSelect = false
                        }, 2000)
                        e.printStackTrace()
                        Log.e("dsgdgdsgdsgdsgg", "Error= ${e.message}")
                    }
                })


    }

    var pickupMarker: Marker? = null
    var dropoffMarker: Marker? = null

    private fun drawPolyLineAndAnimateCar(pickuplatlngs: LatLng, dropofflatlngs: LatLng) {

        /* if (mMap != null) {
            mMap.clear();
        }*/


        if (customerMarker != null) {
            customerMarker!!.remove()
            customerMarker!!.isVisible = false
            customerMarker = null
        }
        if (pickupMarker != null) {
            pickupMarker!!.remove()
            pickupMarker = null
        }
        if (dropoffMarker != null) {
            dropoffMarker!!.remove()
            dropoffMarker = null
        }
        isMarkersSeted = true


        if (jobStatus.equals(Constants.PENDING, ignoreCase = true) ||
            jobStatus.equals(Constants.none, ignoreCase = true)) {
            mMap!!.clear()
            pickupMarker = baseActivity!!.placeMarkerOnMapWithSize(
                mMap!!, pickupMarker, pickuplatlngs, R.drawable.ic_pick_up,
                50, 50, false, baseActivity!!.cameraZoomLavel15_0_f
            )

        }

        dropoffMarker = baseActivity!!.placeMarkerOnMapWithSize(
            mMap!!, dropoffMarker, dropofflatlngs, R.drawable.ic_drop_off,
            50, 50, false, baseActivity!!.cameraZoomLavel15_0_f
        )


        if (!gesture) {
            val builder = LatLngBounds.Builder()
            for (latLng in polyLineList!!) {
                builder.include(latLng)
                var bounds: LatLngBounds = builder.build()
                cu = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                mMap!!.animateCamera(cu)
            }
        }

        polylineOptions = PolylineOptions()
        polylineOptions.color(ContextCompat.getColor(baseActivity!!, R.color.colorPrimary))
        polylineOptions.width(10f)
        polylineOptions.startCap(SquareCap())
        polylineOptions.endCap(SquareCap())
        polylineOptions.jointType(JointType.ROUND)
        polylineOptions.addAll(polyLineList)
        greyPolyLine = mMap!!.addPolyline(polylineOptions)

        blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(10f)
        blackPolylineOptions.color(ContextCompat.getColor(baseActivity!!, R.color.colorPrimary))
        blackPolylineOptions.startCap(SquareCap())
        blackPolylineOptions.endCap(SquareCap())
        blackPolylineOptions.jointType(JointType.ROUND)
        blackPolyline = mMap!!.addPolyline(blackPolylineOptions)
//        runningdriver = true
        Handler().postDelayed({
            onAdSelect = false
        }, 2000)

    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = java.util.ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }


    // TODO  Trip flow

    var tripPickupLatLng: LatLng? = null
    var tripDropLatLng: LatLng? = null
    var tripId = ""


/////--------------------------------------------

    var driverContactNUmber = "0"
    var driverName = ""
    var driverPhoto = ""
    var jobStatus = Constants.none
    /*fun getNewCurrentJobAPICall(isLoading: Boolean, callFrom: String) {

        Log.e("getNewCurrentJobAPI", "callFrom=${callFrom}")
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getNewCurrentJobAPI(repo.pref.languageCode, Constants.customer, repo.pref.USER_ID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<NewCurrentJobApiResponse>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    if (isLoading) {
                        showLoading()
                    }
                }

                override fun onNext(response: Response<NewCurrentJobApiResponse>) {
                    hideLoading()
                    Log.e("getNewCurrentJobAPI", "response.isSuccessful=${response.isSuccessful}")
                    Log.e("getNewCurrentJobAPI", "response.code()=${response.code()}")
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                if (response.body()!!.payload != null) {
                                    var payload = response.body()!!.payload
                                    tripId = payload.tripId
                                    jobStatus = payload.status

                                    //mMap!!.clear()

                                    if (jobStatus.equals(Constants.PENDING, ignoreCase = true)) {

                                        rlWaitingLayoutHome.visibility = View.VISIBLE
                                        flBlankLayout.makeGone()
                                    } else {
                                        rlWaitingLayoutHome.visibility = View.GONE
                                        initBottomSheet()


                                        flHomeViews.visibility = View.GONE
                                        llToolbarHome.visibility = View.GONE

                                        driver_Id = payload.driverId
                                        driverName = payload.driver.name
                                        driverPhoto = payload.driver.profilePicture

                                        tripPickupLatLng = LatLng(payload.startLatitude, payload.startLongitude)
                                        tripDropLatLng = LatLng(payload.finishLatitude, payload.finishLongitude)

                                        if (jobStatus.equals(Constants.ACCEPTED, ignoreCase = true) ||
                                            jobStatus.equals(Constants.PICKED_UP, ignoreCase = true)) {
                                            bsDropOffJobBehaviour.expand()
                                            bsllAcceptJob.makeVisible()
                                            bsllDropOffJob.bottom_dropoff_job.makeGone()
                                            llSOSHome.makeGone()
                                            llMenuOpen.makeVisible()
                                            setMarkers()

                                            bsllAcceptJob.tvVehicleNumberModel.text = "${payload.driver.vehiclePlateNumber} - ${payload.driver.vehicleName}"
                                            bsllAcceptJob.tvESTArrivalTime.text = "Driver will pick you up at ${payload.formattedDuration}"
                                            bsllAcceptJob.tvDriverContactNumber.text = "${payload.driver.mobileCountryCode}${payload.driver.mobile}"
                                            bsllAcceptJob.tvDriverNameHome.text = "${payload.driver.name}"
                                            bsllAcceptJob.ratingBarAccepted.rating = payload.driver.rating.toFloat()
                                            driverContactNUmber = "${payload.driver.mobileCountryCode}${payload.driver.mobile}"

                                            GlobalMethods().loadImagesWithProgressbar(
                                                payload.driver.profilePicture,
                                                bsllAcceptJob.imgDriverProfile1,
                                                R.drawable.ic_user,
                                                pbLoadingEditProfile,
                                                300,
                                                300
                                            )


                                        }*//* else if (jobStatus.equals(Constants.PICKED_UP, ignoreCase = true)) {
                                                bsDropOffJobBehaviour.expand()
                                                bsllAcceptJob.makeVisible()
                                                bsllDropOffJob.bottom_dropoff_job.makeGone()
                                                llSOSHome.makeVisible()
                                            }*//* else if (jobStatus.equals(Constants.STARTED, ignoreCase = true)) {
                                            llSOSHome.makeVisible()
                                            llMenuOpen.makeVisible()
                                            bsDropOffJobBehaviour.expand()
                                            bsllAcceptJob.makeGone()
                                            bsllDropOffJob.bottom_dropoff_job.makeVisible()
                                            setMarkers()

                                            bsllDropOffJob.tvOrderID.text = payload.tripId
                                            bsllDropOffJob.tvDriverName.text = payload.driver.name
                                            //bsllDropOffJob.tvHeaderVehicleNumber.text = payload.driver.vehiclePlateNumber
                                            bsllDropOffJob.tvVehicleNumber.text = payload.driver.vehiclePlateNumber
                                            bsllDropOffJob.tvHeaderVehicleModel.text = payload.driver.vehicleModel
                                            bsllDropOffJob.tvDropOffAddress.text = payload.finishAddress
                                            bsllDropOffJob.tvDistance.text = payload.formattedDistance
                                            bsllDropOffJob.tvEST.text = payload.formattedDuration
                                            bsllDropOffJob.tvTotalFare.text = "${Constants.currency_symbol}${payload.total}"

                                            bsllDropOffJob.ratingBarStarted.rating = payload.driver.rating.toFloat()
                                            GlobalMethods().loadImagesWithProgressbar(
                                                payload.driver.profilePicture,
                                                bsllDropOffJob.imgDriverProfile,
                                                R.drawable.ic_user,
                                                pbLoadingEditProfile,
                                                300,
                                                300
                                            )

                                        } else {
                                            //replaceFragment(OnGoingJobFragment(), true, false)

                                        }
                                        //mMap!!.clear()
                                        if (driverMarker != null) {
                                            if (jobStatus.equals(Constants.ACCEPTED, ignoreCase = true) ||
                                                jobStatus.equals(Constants.PICKED_UP, ignoreCase = true)) {

                                                drawpoliline(driverLatLong!!, tripPickupLatLng!!)
                                            } else if (jobStatus.equals(Constants.STARTED, ignoreCase = true)) {

                                                drawpoliline(driverLatLong!!, tripDropLatLng!!)
                                            }
                                        } else {

                                        }

                                        if (driverLocListener == null) {
                                            driverLocationListener()
                                        }
                                    }

                                    if (newJobListener == null) {
                                        newJobListener()
                                    }
                                } else {
                                    setCustomerMarker("getNewCurrentJobAPICall - Payload = null", true)
                                }
                                //baseActivity!!.onBackPressed()
                            } else {
                                setCustomerMarker("getNewCurrentJobAPICall - body = null", true)
                            }
                        } else {
                            setCustomerMarker("getNewCurrentJobAPICall - code != 200", true)
                        }
                    } else {
                        setCustomerMarker("getNewCurrentJobAPICall - response != isSuccessful", true)

                        if (baseActivity!!.isHomeView) {
                            getReceiptAPICall(false)
                        }


                        //baseActivity!!.showApiResponseERROR(response.errorBody())
                    }

                    //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    setCustomerMarker("getNewCurrentJobAPICall - onError", true)
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }*/

    fun setMarkers() {

        if (pickupMarker != null) {
            pickupMarker!!.remove()
        }
        if (dropoffMarker != null) {
            dropoffMarker!!.remove()
        }

        if (jobStatus.equals(Constants.ACCEPTED, ignoreCase = true) ||
            jobStatus.equals(com.carty.riderapp.common.Constants.PICKED_UP, ignoreCase = true)) {

            /*  pickupMarker = baseActivity!!.placeMarkerOnMapWithSize(mMap!!, pickupMarker, tripPickupLatLng, R.drawable.ic_drop_off,
                      40, 40, false, baseActivity!!.cameraZoomLavel15_0_f)*/

            pickupMarker = placeMarkerOnMapWithSize(mMap!!, tripPickupLatLng!!, R.drawable.ic_drop_off,
                40, 40, "Home - pickupMarker")

        } else {

            dropoffMarker = placeMarkerOnMapWithSize(mMap!!, tripDropLatLng!!, R.drawable.ic_drop_off,
                40, 40, "Home - dropoffMarker")
            /*   dropoffMarker = baseActivity!!.placeMarkerOnMapWithSize(mMap!!, dropoffMarker, tripDropLatLng, R.drawable.ic_drop_off,
                       40, 40, false, baseActivity!!.cameraZoomLavel15_0_f)*/
        }
    }

    /////--------------------------------------------
    override fun onPause() {
        super.onPause()
        baseActivity!!.isHomeView = false
        Log.e(
            "newJobListener",
            "onPause"
        )
    }

    override fun onStop() {
        super.onStop()
        Log.e(
            "newJobListener",
            "onStop"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(
            "newJobListener",
            "onDestroy"
        )
        if (dbReference != null) {
            if (newJobListener != null) {
                dbReference!!.removeEventListener(newJobListener!!)
                dbReference = null
                newJobListener = null
            }
        }
        if (driverDBReference != null) {
            if (driverLocListener != null) {
                driverDBReference!!.removeEventListener(driverLocListener!!)
                driverDBReference = null
                driverLocListener = null
            }
        }
    }

    ////// --------------------------------------------------

    var dbReference: DatabaseReference? = null
    var newJobListener: ValueEventListener? = null
    fun newJobListener() {

        dbReference = FirebaseDatabase.getInstance()
            .getReference("${Constants.trips}/${tripId}")

        newJobListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "newJobListener",
                    "onCancelled = ${p0.message}"
                )
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    var datas = dataSnapshot.getValue(TripStatusTracking::class.java)!!
                    Log.e(
                        "newJobListener",
                        "11-->  trip_id ${datas.driver_id} && status ${datas.status}"
                    )
                    jobStatus = "${datas.status}"
                    if (baseActivity!!.isHomeView) {
                        if (jobStatus.equals(Constants.PENDING)) {
                            flHomeViews.visibility = View.VISIBLE
                            isMarkersSeted = true
                        } else if (jobStatus.equals(Constants.ACCEPTED)) {
                            flHomeViews.visibility = View.GONE
                            isMarkersSeted = true
                        } else if (jobStatus.equals(Constants.PICKED_UP)) {
                            flHomeViews.visibility = View.GONE
                            isMarkersSeted = true
                        } else if (jobStatus.equals(Constants.STARTED)) {
                            flHomeViews.visibility = View.GONE
                            isMarkersSeted = true
                        } else {
                            isMarkersSeted = false
                        }

                        //getNewCurrentJobAPICall(false, "newJobListener - jobStatus =${jobStatus}")

                    } else {

                    }

                } else {
                    isMarkersSeted = false
                    if (jobStatus.equals(Constants.STARTED) ||
                        jobStatus.equals(Constants.COMPLETED)
                    ) {
                        if (baseActivity!!.isHomeView) {
                            //getReceiptAPICall(false)
                        }

                    }
                    Log.e(
                        "newJobListener",
                        "dataSnapshot.value == null"
                    )
                }


            }
        }
        dbReference!!.addValueEventListener(newJobListener!!)
    }

    /////------------------------------------------------------------------
    var driverDBReference: DatabaseReference? = null
    var driverLocListener: ValueEventListener? = null
    var driver_Id = "0"
    var driverLatLong: LatLng? = null
    var driverMarker: Marker? = null
    var currentBearing: Float = 0f
    fun driverLocationListener() {

        driverDBReference = FirebaseDatabase.getInstance()
            .getReference("${Constants.drivers}/${driver_Id}")

        driverLocListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(
                    "driverLocationLstnr",
                    "onCancelled = ${p0.message}"
                )
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.value != null) {
                    var datas = dataSnapshot.getValue(DriverLocationModel::class.java)!!
                    Log.e(
                        "driverLocationLstnr",
                        "11-->  driverId =${datas.driverId} && bearing =${datas.bearing} && LatLong = ${datas.latitude},${datas.longitude}"
                    )

                    if (baseActivity!!.isHomeView) {

                        driverLatLong = LatLng(datas.latitude, datas.longitude)
                        currentBearing = datas.bearing

                        if (jobStatus.equals(Constants.ACCEPTED, ignoreCase = true) ||
                            jobStatus.equals(Constants.PICKED_UP, ignoreCase = true)) {

                            drawpoliline(driverLatLong!!, tripPickupLatLng!!)
                        } else if (jobStatus.equals(Constants.STARTED, ignoreCase = true)) {

                            drawpoliline(driverLatLong!!, tripDropLatLng!!)
                        }
                        addMarker()
                        animateCar(driverMarker!!, driverLatLong!!)
                    } else {

                    }

                } else {

                    Log.e(
                        "driverLocationLstnr",
                        "dataSnapshot.value == null"
                    )
                }


                var count = 0

            }
        }
        driverDBReference!!.addValueEventListener(driverLocListener!!)
    }

    fun addMarker() {

        if (driverMarker == null) {
            driverMarker = placeMarkerOnMapWithSize(
                mMap!!,
                LatLng(driverLatLong!!.latitude, driverLatLong!!.longitude),
                R.drawable.ic_driver_car,
                70,
                40,
                "onMapReady"
            )
        }

    }


    private fun animateCar(
        marker: Marker,
        destination: LatLng
    ) {

        val startPosition = marker.position
        val endPosition = LatLng(destination.latitude, destination.longitude)
        val latLngInterpolator: LatLngInterpolator = LatLngInterpolator.LinearFixed()

        val startRotation = marker.rotation
//        val rotationAngle: Float = getBearing(startPosition, destination)
        val valueAnimator =
            ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1000 // duration 1 seconds
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            try {
                val v = animation.animatedFraction
                val newPosition: LatLng? =
                    latLngInterpolator.interpolate(v, startPosition, endPosition)
                marker.setPosition(newPosition!!)

                marker.setAnchor(0.5f, 0.5f)
//                marker.rotation = computeRotation(v, startRotation, rotationAngle)
                marker.rotation = computeRotation(v, startRotation, currentBearing)
            } catch (ex: Exception) {
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })
        valueAnimator.start()
    }

    private fun computeRotation(
        fraction: Float,
        start: Float,
        end: Float
    ): Float {

        var endBearing: Float = 0.0f
        endBearing = end
        //todo remove below if block its extra added
//        if (end > 20) {
//            endBearing = end - 20;
//        }

        val normalizeEnd = endBearing - start // rotate start to 0
        val normalizedEndAbs = (normalizeEnd + 360) % 360
        val direction: Float =
            (if (normalizedEndAbs > 180) -1 else 1).toFloat() // -1 = anticlockwise, 1 = clockwise
        val rotation: Float
        rotation = if (direction > 0) {
            normalizedEndAbs
        } else {
            normalizedEndAbs - 360
        }
        val result = fraction * rotation + start
        return (result + 360) % 360
    }


    private interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolator {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360
                }
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }

    /////---------------------------------------------------------------------------
  /*  fun getReceiptAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getReceiptJobAPI(repo.pref.languageCode, tripId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<ReceiptApiResponse>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    if (isLoading) {
                        showLoading()
                    }
                }

                override fun onNext(response: Response<ReceiptApiResponse>) {
                    hideLoading()
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                if (response.body()!!.payload != null) {
                                    var payload = response.body()!!.payload
                                    if (payload.status.equals(Constants.COMPLETED)) {
                                        flBlankLayout.makeGone()
                                        replaceFragment(ReceiptFragment.newInstance(tripId, "${driverName}", "${driverPhoto}"), false, true)
                                    }
                                }
                                //baseActivity!!.onBackPressed()
                            }
                        }
                    } else {
                        //baseActivity!!.showApiResponseERROR(response.errorBody())
                    }

                    //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }*/


    /////-------------------------------------------------------------------------
    private lateinit var bsllAcceptJob: LinearLayout
    private lateinit var bsAcceptJobBehaviour: BottomSheetBehavior<LinearLayout>

    private lateinit var bsllDropOffJob: LinearLayout
    private lateinit var bsDropOffJobBehaviour: BottomSheetBehavior<LinearLayout>
    var vehicle_name = ""


    private fun ongoingJobFlow() {
        bsllAcceptJob = homeView!!.bottom_accept_job
        bsAcceptJobBehaviour = BottomSheetBehavior.from(bsllAcceptJob)

        bsllDropOffJob = homeView!!.bottom_dropoff_job
        bsDropOffJobBehaviour = BottomSheetBehavior.from(bsllDropOffJob)

    }


    private fun initBottomSheet() {
        //TODO /// View after trip accept

//       /* bsllAcceptJob = homeView!!.bottom_accept_job
//        bsAcceptJobBehaviour = BottomSheetBehavior.from(bsllAcceptJob)*/
        bsAcceptJobBehaviour.isHideable = false

        bsAcceptJobBehaviour.peekHeight = baseActivity!!.resources.getDimension(R.dimen._160sdp).toInt()

        flBlankLayout.makeVisible()

        bsllAcceptJob.imgDriverProfile1.setOnClickListener(View.OnClickListener {
//            replaceFragment(DriverDetailFragment.newInstance(driver_Id,""), true, false)
            addFragment(DriverDetailFragment.newInstance(driver_Id, ""), true)
        })
        bsllDropOffJob.imgDriverProfile.setOnClickListener(View.OnClickListener {
            //replaceFragment(DriverDetailFragment.newInstance(driver_Id,""), true, false)
            addFragment(DriverDetailFragment.newInstance(driver_Id, ""), true)
        })

        bsAcceptJobBehaviour.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(view: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                      activity!!.main_bottom_menu.makeVisible()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    else -> {

                    }
                }
            }
        })

        //TODO /// View after trip start

//       /* bsllDropOffJob = homeView!!.bottom_dropoff_job
//        bsDropOffJobBehaviour = BottomSheetBehavior.from(bsllDropOffJob)*/
        bsDropOffJobBehaviour.isHideable = false

        bsDropOffJobBehaviour.peekHeight = baseActivity!!.resources.getDimension(R.dimen._130sdp).toInt()

        bsllDropOffJob.tvHeaderVehicleModel.text = "${vehicle_name}"

        bsDropOffJobBehaviour.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(view: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    else -> {

                    }
                }
            }
        })

        bsllAcceptJob.llDriverContact.setOnClickListener(View.OnClickListener {
            if (isPermissionGranted()) {
                Log.v("placeCall", "Permission is granted")
                placeCall("$driverContactNUmber")
            } else {
                Log.v("placeCall", "Permission not granted")
            }
        })

        llSOSHome.setOnClickListener(View.OnClickListener {
            if (isPermissionGranted()) {
                Log.v("placeCall", "Permission is granted")
                placeCall(Constants.SOS_NUMBER)
            } else {
                Log.v("placeCall", "Permission not granted")
            }
        })

        llMenuOpen.setOnClickListener(View.OnClickListener {
            baseActivity!!.drawerOpen()
        })

    }

    fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(baseActivity!!, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
                Log.v("placeCall", "Permission is granted")
                true
            } else {
                Log.v("placeCall", "Permission is revoked")
                ActivityCompat.requestPermissions(baseActivity!!, arrayOf(Manifest.permission.CALL_PHONE), 1)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("placeCall", "Permission is granted")
            true
        }
    }

    private fun placeCall(number: String) {

        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$number")
        startActivity(callIntent)

        /*baseActivity!!.permissionCheckingOne(
                baseActivity!!.CALL_PHONE,
                isForceFullyPermission = true,
                isCalling = true,
                contactNumber = number

        )*/
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private fun dialogRejectJob() {
        val builder = AlertDialog.Builder(baseActivity!!)
        builder.setTitle("Cancel")
        builder.setMessage("Are you sure you want to cancel this trip?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            //rejectJobAPICall(true, Constants.action_reject)
        }

        builder.setNegativeButton("No") { dialog, which ->
        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(applicationContext,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }
    //////////////////////////////////////////////////////////////////////////////////////////

  /*  fun rejectJobAPICall(isLoading: Boolean, action: String) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.rejectJobAPI(
            repo.pref.languageCode,
            repo.pref.USER_ID,
            tripId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<NewCurrentJobApiResponse>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    if (isLoading) {
                        showLoading()
                    }
                }

                override fun onNext(response: Response<NewCurrentJobApiResponse>) {
                    hideLoading()

                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            rlWaitingLayoutHome.visibility = View.GONE
                            flHomeViews.visibility = View.VISIBLE

                            bsDropOffJobBehaviour.expand()
                            bsllAcceptJob.makeGone()
                            bsllDropOffJob.bottom_dropoff_job.makeGone()
                            llSOSHome.makeGone()
                            llMenuOpen.makeGone()

                        }
                    } else {

                        //baseActivity!!.showApiResponseERROR(response.errorBody())
                    }

                    //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }*/


}