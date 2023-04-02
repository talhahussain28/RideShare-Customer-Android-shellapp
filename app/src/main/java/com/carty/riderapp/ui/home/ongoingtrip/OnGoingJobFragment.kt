package com.carty.riderapp.ui.home.ongoingtrip

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.expand
import com.carty.riderapp.common.makeGone
import com.carty.riderapp.common.makeVisible
import com.carty.riderapp.model.map_poliline.Result
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.ReceiptFragment
import com.carty.riderapp.ui.home.driver_details.DriverDetailFragment
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottom_accept_job.view.*
import kotlinx.android.synthetic.main.bottom_dropoff_job.view.*
import kotlinx.android.synthetic.main.fragment_on_going_job.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class OnGoingJobFragment : BaseFragment(), View.OnClickListener, OnMapReadyCallback {
    private lateinit var bsllAcceptJob: LinearLayout
    private lateinit var bsAcceptJobBehaviour: BottomSheetBehavior<LinearLayout>

    private lateinit var bsllDropOffJob: LinearLayout
    private lateinit var bsDropOffJobBehaviour: BottomSheetBehavior<LinearLayout>

    lateinit var viewOnGoing: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewOnGoing = inflater.inflate(R.layout.fragment_on_going_job, container, false)
        return viewOnGoing
    }

    var vehicle_name = "none"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            var bundle = arguments!!
            if (bundle!!.containsKey("vehicle_name")) {
                vehicle_name = "${bundle!!.getString("vehicle_name")}"
            }
        }


        var mapFragment =
                childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        initBottomSheet()

        bsllDropOffJob.llShareETA.setOnClickListener(View.OnClickListener {
            replaceFragment(ReceiptFragment(), false, true)
        })


        bsllAcceptJob.imgDriverProfile1.setOnClickListener(View.OnClickListener {
            bsDropOffJobBehaviour.expand()
            bsllAcceptJob.makeGone()
            bsllDropOffJob.bottom_dropoff_job.makeVisible()
            llSOS.makeVisible()
        })
//        bsllDropOffJob.imgDriverProfile.setOnClickListener(View.OnClickListener {
//            replaceFragment(DriverDetailFragment(), true, false)
//        })
        bsllAcceptJob.setOnClickListener(View.OnClickListener {

            var driverDetail = DriverDetailFragment()
            var bundle = Bundle()

            bundle.putString("vehicle_name", "${vehicle_name}")
            driverDetail.arguments = bundle
            // baseActivity!!.onBackPressed()
            replaceFragment(driverDetail, true, false)
        })

        bsllAcceptJob.llDriverContact.setOnClickListener(View.OnClickListener {
            placeCall(Constants.SOS_NUMBER)
        })

        llSOS.setOnClickListener(View.OnClickListener {
            placeCall(Constants.SOS_NUMBER)
        })
    }

    private fun placeCall(number: String) {
        baseActivity!!.permissionCheckingOne(
                baseActivity!!.CALL_PHONE,
                isForceFullyPermission = true,
                isCalling = true,
                contactNumber = number

        )
    }

    private fun initBottomSheet() {
        bsllAcceptJob = viewOnGoing!!.bottom_accept_job
        bsAcceptJobBehaviour = BottomSheetBehavior.from(bsllAcceptJob)
        bsAcceptJobBehaviour.isHideable = false

        bsAcceptJobBehaviour.peekHeight = activity!!.resources.getDimension(R.dimen._180sdp).toInt()

        bsllAcceptJob.tvVehicleNumberModel.text = "ST3751 - ${vehicle_name}"


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


        bsllDropOffJob = viewOnGoing!!.bottom_dropoff_job
        bsDropOffJobBehaviour = BottomSheetBehavior.from(bsllDropOffJob)
        bsDropOffJobBehaviour.isHideable = false

        bsDropOffJobBehaviour.peekHeight = activity!!.resources.getDimension(R.dimen._90sdp).toInt()

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


    }

    override fun onClick(v: View?) {
        when (v!!.id) {

        }
    }

    var pickMarker: Marker? = null
    var dropMarker: Marker? = null
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        var pickLatLng = LatLng(baseActivity!!.gpsTracker.latitude, baseActivity!!.gpsTracker.longitude)
        pickMarker = baseActivity!!.placeMarkerOnMapWithSize(mMap!!, pickMarker, pickLatLng, R.drawable.ic_location,
                50, 35, false, baseActivity!!.cameraZoomLavel15_0_f)

        var dropLatLng = LatLng(23.237560, 72.647781)
        dropMarker = baseActivity!!.placeMarkerOnMapWithSize(mMap!!, dropMarker, dropLatLng, R.drawable.ic_drop_off,
                40, 40, false, baseActivity!!.cameraZoomLavel15_0_f)

        //userMarker.position = LatLng(baseActivity!!.gpsTracker.latitude, baseActivity!!.gpsTracker.longitude)

        /*permissionCheckingOne(
                baseActivity!!.LOCATION_PERMISSIONS_LIST,
                isEnableGPS = true,
                isMarkerPlace = true,
                mMap = mMap,
                mMarker = pickMarker,
                markerImg = R.drawable.ic_location,
                isGetAddress = true,
                wedgets = edtpickup
        )*/

        /*permissionCheckingOne(
                baseActivity!!.LOCATION_PERMISSIONS_LIST,
                isEnableGPS = true,
                isMarkerPlace = true,
                mMap = mMap,
                mMarker = dropMarker,
                markerImg = R.drawable.ic_drop_off,
                isGetAddress = true,
                wedgets = edtpickup
        )*/
        if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            drawpoliline(pickLatLng, dropLatLng)
        }
    }

    var gesture: Boolean = false
    private var polyLineList: List<LatLng>? = null
    internal lateinit var polylineOptions: PolylineOptions
    internal var blackPolyline: Polyline? = null
    internal var greyPolyLine: Polyline? = null
    lateinit var cu: CameraUpdate
    lateinit var mMap: GoogleMap


    internal lateinit var blackPolylineOptions: PolylineOptions

    fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng) {
//        runningdriver = false

        Log.e("DGSdgsdsg", "safsfa")
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.latitude)
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.longitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.latitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.longitude)

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

                                val routeList = result.getRoutes()
                                Log.e("DGsdsgdgsgd", "" + routeList.size)

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
                                e.printStackTrace()
                                Log.e("dsgdgdsgdsgdsgg", e.message)
                            }
                        })


    }


    private fun drawPolyLineAndAnimateCar(pickuplatlng: LatLng, dropofflatlng: LatLng) {

        /* if (mMap != null) {
            mMap.clear();
        }*/

        if (!gesture) {
            val builder = LatLngBounds.Builder()
            for (latLng in polyLineList!!) {
                builder.include(latLng)
                var bounds: LatLngBounds = builder.build()
                cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
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

}