package com.carty.riderapp.ui.home.order_place

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.model.TripStatusTracking
import com.carty.riderapp.ui.home.order_place.adapter.VehicleListPagerAdapter
import com.carty.riderapp.ui.home.order_place.model.BookOrderRequest
import com.carty.riderapp.ui.home.order_place.model.VehicleModel
import com.carty.riderapp.ui.home.order_place.response.BookTripApiResponse
import com.carty.riderapp.ui.home.order_place.response.GetVehicleListApiResponse
import com.carty.riderapp.ui.home.order_place.response.PreferenceListApiResponse
import com.carty.riderapp.ui.home.payment_card_setting.PaymentSettingFragment
import com.carty.riderapp.ui.home.payment_card_setting.response.AddCardApiResponse
import com.carty.riderapp.ui.home.response.GetProfileApiResponse
import com.carty.riderapp.ui.home.response.NewCurrentJobApiResponse
import com.carty.riderapp.ui.widgets.NDSpinner
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_order_place.*
import kotlinx.android.synthetic.main.fragment_order_place.edtDropoff
import org.json.JSONObject
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderPlaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderPlaceFragment : BaseFragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                OrderPlaceFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    var pickupAddress = ""
    var dropoffAddress = ""

    var pickupLat = ""
    var pickupLong = ""
    var DropOffLat = ""
    var DropOffLong = ""

    var trip_Id = ""
    var trip_status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (arguments != null) {

            var bundle = arguments


            if (bundle!!.containsKey("trip_Id")) {
                trip_Id = "${bundle!!.getString("trip_Id")}"

            }
            if (bundle!!.containsKey("trip_status")) {
                trip_status = "${bundle!!.getString("trip_status")}"
            }

            if (bundle!!.containsKey("pickupAddress")) {
                pickupAddress = "${bundle!!.getString("pickupAddress")}"
            }
            if (bundle!!.containsKey("dropoffAddress")) {
                dropoffAddress = "${bundle!!.getString("dropoffAddress")}"
            }
            if (bundle!!.containsKey("pickupLat")) {
                pickupLat = "${bundle!!.getString("pickupLat")}"
            }
            if (bundle!!.containsKey("pickupLong")) {
                pickupLong = "${bundle!!.getString("pickupLong")}"
            }
            if (bundle!!.containsKey("DropOffLat")) {
                DropOffLat = "${bundle!!.getString("DropOffLat")}"
            }
            if (bundle!!.containsKey("DropOffLong")) {
                DropOffLong = "${bundle!!.getString("DropOffLong")}"
            }

        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!trip_Id.equals(Constants.none)) {
            if (trip_status.equals(Constants.PENDING)) {
                rlWaitingLayout.visibility = View.VISIBLE
                tvOrderConfirmBTN.isEnabled = false
            }
        }

        viewPagerVehicleList = view.findViewById(R.id.viewPagerVehicleList)
        tvNoVehicle = view.findViewById(R.id.tvNoVehicle)

        imgvEditCard.setOnClickListener(this)
        tvAddNewCardBtn.setOnClickListener(this)
        tvOrderConfirmBTN.setOnClickListener(this)
        tvPreferences.setOnClickListener(this)
        tcCancelWaitingTrip.setOnClickListener(this)


        edtPickUp.text = pickupAddress
        edtDropoff.text = dropoffAddress

        getCardAPICall(true)
        getVehicleAPICall(true)
        //getPreferencesAPICall(false)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }


    private fun setToolbar() {

        imgIconBackOrder.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )

        imgIconBackOrder.setOnClickListener { baseActivity!!.onBackPressed() }

        /* imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
         imgNotification.setOnClickListener {
             replaceFragment(NotificationFragment(), true, false)
         }*/

        tvTitleOrder.text = "Order"

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgvEditCard -> {
                try {
                    var frag = PaymentSettingFragment()
                    frag.setTargetFragment(this@OrderPlaceFragment, Constants.PAYMENT_SELECTION)
//                    baseActivity!!.addFragment(frag, true)
                    baseActivity!!.replaceFragment(frag, true)
                } catch (e: Exception) {
                    Log.e("Exception Here", "Exception Here")
                }
                //  replaceFragment(PaymentSettingFragment(), true, false)
            }
            R.id.tvAddNewCardBtn -> {
                try {
                    var frag = PaymentSettingFragment()
                    frag.setTargetFragment(this@OrderPlaceFragment, Constants.PAYMENT_SELECTION)
                    baseActivity!!.replaceFragment(frag, true)
                } catch (e: Exception) {
                    Log.e("Exception Here", "Exception Here")
                }

            }
            R.id.tvOrderConfirmBTN -> {
                // rlWaitingLayout.visibility = View.VISIBLE

                if (vehicle_type_id.isNullOrEmpty()) {
                    msgDialog("Please select any vehicle")
                } else if (cardId.isNullOrEmpty()) {
                    msgDialog("Please select any credit card")
                } else {
                    bookRequest()
                }
            }
            R.id.tvPreferences -> {
                getPreferencesAPICall(true)

            }
            R.id.tcCancelWaitingTrip -> {
                /* rlWaitingLayout.visibility = View.GONE
                 var ongoing = OnGoingJobFragment()
                 var bundle = Bundle()

                 bundle.putString("vehicle_name", "${vehicle_name}")
                 ongoing.arguments = bundle
                 // baseActivity!!.onBackPressed()
                 replaceFragment(ongoing, false, true)*/
                dialogRejectJob()
            }
        }
    }

    var bookingJson = JSONObject()
    var bookingDataMap = HashMap<String, String>()
    var bookorderModelRequest = BookOrderRequest()
    private fun bookRequest() {
        // rlWaitingLayout.visibility = View.VISIBLE

//
//        bookingDataMap.put("code", "${repo.pref.languageCode}")
//        bookingDataMap.put("customer_id", "${repo.pref.USER_ID}")
//        bookingDataMap.put("card_id", "${cardId}")
//        bookingDataMap.put("vehicle_id", "${vehicle_type_id}")
//        bookingDataMap.put("start_address", "Hansol, Ahmedabad, Gujarat")
//        bookingDataMap.put("start_latitude", "23.0859533")
//        bookingDataMap.put("start_longitude", "72.6300682")
//        bookingDataMap.put("finish_address", "Navarangpura, Gujarat 382430")
//        bookingDataMap.put("finish_latitude", "23.0653374")
//        bookingDataMap.put("finish_longitude", "72.72449399999999")
//        bookingDataMap.put("total_distance", "18995")
//        bookingDataMap.put("total_duration", "2404")
//        bookingDataMap.put("formatted_distance", "11.87 mi")
//        bookingDataMap.put("formatted_duration", "40 mins")
//        //bookingDataMap.put("preferences", "string")
//        bookingDataMap.put("total", "189.56")
//        //////////////////////////////////////////////////////////////////////////
//        try {
//            bookingJson.put("code", "${repo.pref.languageCode}")
//            bookingJson.put("customer_id", "${repo.pref.USER_ID}")
//            bookingJson.put("card_id", "${cardId}")
//            bookingJson.put("vehicle_id", "${vehicle_type_id}")
//            bookingJson.put("start_address", "Hansol, Ahmedabad, Gujarat")
//            bookingJson.put("start_latitude", "23.0859533")
//            bookingJson.put("start_longitude", "72.6300682")
//            bookingJson.put("finish_address", "Navarangpura, Gujarat 382430")
//            bookingJson.put("finish_latitude", "23.0653374")
//            bookingJson.put("finish_longitude", "72.72449399999999")
//            bookingJson.put("total_distance", "18995")
//            bookingJson.put("total_duration", "2404")
//            bookingJson.put("formatted_distance", "11.87 mi")
//            bookingJson.put("formatted_duration", "40 mins")
//            //bookingJson.put("preferences", "string")
//            bookingJson.put("total", "189.56")
//
//            var preferenceJson = JSONObject()
//            preferenceJson.put("mode_id", "MOD160577982553")
//            preferenceJson.put("music_id", "MUS160578011248")
//            preferenceJson.put("accessible_id", "ASB160578036515")
//            preferenceJson.put("temperature", "68.1")
//
//            Log.e("bookRequest", "==========================================================")
//            Log.e("bookRequest", "11 bookingJson = ${bookingJson.toString()}")
//            Log.e("bookRequest", "==========================================================")
//            Log.e("bookRequest", "22 bookingJson = ${Gson().toJson(bookingJson)}")
//            Log.e("bookRequest", "==========================================================")
//
//            bookingDataMap.put("preferences", preferenceJson.toString())
//
//        } catch (e: Exception) {
//            Log.e("bookRequest", e.localizedMessage)
//            e.printStackTrace()
//        }
//
//        Log.e("bookRequest", "bookingDataMap = ${Gson().toJson(bookingDataMap)}")

        //////////////////////////////////////////////////////////////////////////

        bookorderModelRequest.code = "${repo.pref.languageCode}"
        bookorderModelRequest.customer_id = "${repo.pref.USER_ID}"
        bookorderModelRequest.card_id = "${cardId}"
        bookorderModelRequest.vehicle_id = "${vehicle_type_id}"
        bookorderModelRequest.start_address = "${pickupAddress}"
        bookorderModelRequest.start_latitude = "${pickupLat}".toDouble()
        bookorderModelRequest.start_longitude = "${pickupLong}".toDouble()
        bookorderModelRequest.finish_address = "${dropoffAddress}"
        bookorderModelRequest.finish_latitude = "${DropOffLat}".toDouble()
        bookorderModelRequest.finish_longitude = "${DropOffLong}".toDouble()
        bookorderModelRequest.total_distance = "${vehicle_total_distance}".toDouble()
        bookorderModelRequest.total_duration = "${vehicle_total_duration}".toDouble()
        bookorderModelRequest.formatted_distance = "${vehicle_formatted_distance}"
        bookorderModelRequest.formatted_duration = "${vehicle_formatted_duration}"
        //bookingJson.put("preferences", "string")
        bookorderModelRequest.total = "${vehicle_total}".toDouble()

        var preferenceModelRequest = BookOrderRequest.preferences()
        preferenceModelRequest.mode_id = "${mode_id}"
        preferenceModelRequest.music_id = "${music_id}"
        preferenceModelRequest.accessible_id = "${accessible_id}"
        preferenceModelRequest.temperature = "${temperature}".toDouble()
        bookorderModelRequest.mPreferences = preferenceModelRequest

        var fareModelRequestBookinb = BookOrderRequest.FareInfo()

        fareModelRequestBookinb.baseFare = "${fareModelRequest.baseFare}".toDouble()
        fareModelRequestBookinb.distanceFare = "${fareModelRequest.distanceFare}".toDouble()
        fareModelRequestBookinb.durationFare = "${fareModelRequest.durationFare}".toDouble()

        bookorderModelRequest.fare_info = fareModelRequestBookinb

        Log.e("bookRequest", "bookorderModelRequest = ${Gson().toJson(bookorderModelRequest)}")

        bookTripApiCall(true)
    }

    private fun bookTripApiCall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
//        repo.api.bookTripByJsonAPI(bookingJson.toString())
//        repo.api.bookTripByMapAPI(bookingDataMap)
        repo.api.bookTripByModelAPI(bookorderModelRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<BookTripApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<BookTripApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    //msgDialog("Trip booked successfully")
                                    rlWaitingLayout.visibility = View.VISIBLE
                                    tripId = response.body()!!.payload.tripId
                                    newJobListener()
//                                    baseActivity!!.onBackPressed()
                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    //////////////////////////////////////////////////////////////////
    var dbReference: DatabaseReference? = null
    var newJobListener: ValueEventListener? = null
    var tripId = ""
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
                    var jobStatus = "${datas.status}"
                    if (jobStatus.equals(Constants.ACCEPTED)) {

                        if (dbReference != null) {
                            if (newJobListener != null) {
                                dbReference!!.removeEventListener(newJobListener!!)
                                dbReference = null
                                newJobListener = null
                            }
                        }
                        baseActivity!!.onBackPressed()
                    }


                } else {

                    Log.e(
                            "newJobListener",
                            "dataSnapshot.value == null"
                    )
                }


                var count = 0
//                Constants.isJobAccepted = false
//                Constants.isJobStarted = false
//                for (jobList in dataSnapshot.children) {
//
//                     count++
//
//                    if (jobList.value != null) {
//                        Log.e(
//                            "newJobListener",
//                            "count = $count & jobList.key = ${jobList.key}"
//                        )
//                        var statuses = jobList.getValue(AssignTripModel::class.java)!!
//                        Log.e("newJobListener", "statuses.size = ${statuses.trip_id} & status = ${statuses.status}")
//                        count++
//
////                        if (statuses.status.equals(Constants.ACCEPTED)) {
////                            Constants.isJobAccepted = true
////                        }
////                        if (statuses.status.equals(Constants.STARTED)) {
////                            Constants.isJobStarted = true
////                        }
//
//                    }
//                }
                //checkInternetForGetOrdersAPI()
            }
        }
        dbReference!!.addValueEventListener(newJobListener!!)
    }

    //////////////////////////////////////////////////////////////////
    var selectedCardNumber = ""
    var selectedCardName = ""
    var selectionType = Constants.CASH
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PAYMENT_SELECTION) {
                selectionType = data!!.getStringExtra("selectionType")
                selectedCardName = data!!.getStringExtra("selectedCardName")
                selectedCardNumber = data!!.getStringExtra("selectedCardNumber")
//                if (selectionType.equals(Constants.CASH)) {
//                    viewCash.visibility = View.VISIBLE
//                    viewCard.visibility = View.GONE
//                }
//                if (selectionType.equals(Constants.CreditCard)) {
//                    viewCash.visibility = View.GONE
//                    viewCard.visibility = View.VISIBLE
//                }
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    var dialogPreferences: Dialog? = null
    var mode_id = ""
    var music_id = ""
    var accessible_id = ""
    var temperature = "0"
    var isSpinnerClick = false

    fun dialogPreferences() {

        dialogPreferences = Dialog(baseActivity!!)
        dialogPreferences!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogPreferences!!.setCancelable(false)
        dialogPreferences!!.setContentView(R.layout.dialog_preferences)
        dialogPreferences!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogPreferences!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (!baseActivity!!.isFinishing) {
            dialogPreferences!!.show()
        }
        spinnerData()

        var seeknarTemperature = dialogPreferences!!.findViewById<SeekBar>(R.id.seeknarTemperature)
        var imgv_dialog_close = dialogPreferences!!.findViewById<ImageView>(R.id.imgv_dialog_close)
        var dVerify_btnSubmit = dialogPreferences!!.findViewById<TextView>(R.id.dVerify_btnSubmit)
        var tvModeItem = dialogPreferences!!.findViewById<TextView>(R.id.tvModeItem)
        var tvMusicItem = dialogPreferences!!.findViewById<TextView>(R.id.tvMusicItem)
        var tvAccessibleItem = dialogPreferences!!.findViewById<TextView>(R.id.tvAccessibleItem)
        var tvTemperatureCount = dialogPreferences!!.findViewById<TextView>(R.id.tvTemperatureCount)
        var tvTempLow = dialogPreferences!!.findViewById<TextView>(R.id.tvTempLow)
        var tvTempHigh = dialogPreferences!!.findViewById<TextView>(R.id.tvTempHigh)

        var spinnerMode = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerMode)
        var spinnerMusic = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerMusic)
        var spinnerAccessible = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerAccessible)


        var spinnerModeAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, ModeList)
        spinnerMode.adapter = spinnerModeAdapter
        var spinnerMusicAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, MusicTypeList)
        spinnerMusic.adapter = spinnerMusicAdapter
        var spinnerAccessibleAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, AccessibleList)
        spinnerAccessible.adapter = spinnerAccessibleAdapter



        tvTempLow.text = "0\u2109"
        tvTempHigh.text = "100\u2109"
        tvTemperatureCount.text = "0\u2109"

        imgv_dialog_close.setOnClickListener {
            dialogPreferences!!.dismiss()
        }
        tvModeItem.setOnClickListener {
            isSpinnerClick = true
            spinnerMode.performClick()
        }
        tvAccessibleItem.setOnClickListener {
            isSpinnerClick = true
            spinnerAccessible.performClick()
        }
        tvMusicItem.setOnClickListener {
            isSpinnerClick = true
            spinnerMusic.performClick()
        }

        dVerify_btnSubmit.setOnClickListener {
            dialogPreferences!!.dismiss()
            // updatePreference("", "")
        }

        seeknarTemperature!!.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                tvTemperatureCount.text = "${progress} \u2109"
                temperature = "${progress}"
                //updatePreference("", "")
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Toast.makeText(baseActivity!!,
//                        "Progress is: " + seek.progress + "%",
//                        Toast.LENGTH_SHORT).show()
            }
        })
        spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e("SpinnerSelection", "spinnerMode-> onNothingSelected")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Log.e("SpinnerSelection", "spinnerMode-> onItemSelected $position")
                if (position >= 0 && isSpinnerClick) {

                    val mode = ModeList[position]
                    tvModeItem.text = mode.title
                    mode_id = mode.modeId
                    if (mode.title.equals("none", ignoreCase = true)) {
                        mode_id = ""
                    }
                    isSpinnerClick = false
                    //updatePreference("", "")
//                        getStateListAPI(country.countryId)

                } else {
//                    val mode = ModeList[0]
//                    tvModeItem.text = mode.title

                }
            }
        }

        spinnerMusic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e("SpinnerSelection", "spinnerMusic-> onNothingSelected")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Log.e("SpinnerSelection", "spinnerMusic-> onItemSelected $position")
                if (position >= 0 && isSpinnerClick) {

                    val music = MusicTypeList[position]
                    tvMusicItem.text = music.title
                    music_id = music.musicId
                    if (music.title.equals("none", ignoreCase = true)) {
                        music_id = ""
                    }
                    isSpinnerClick = false
                    //updatePreference("", "")
//                        getStateListAPI(country.countryId)

                } else {

//                    val music = MusicTypeList[0]
//                    tvMusicItem.text = music.title
                }
            }
        }

        spinnerAccessible.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.e("SpinnerSelection", "spinnerAccessible-> onNothingSelected")
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Log.e("SpinnerSelection", "spinnerAccessible-> onItemSelected $position & isSpinnerClick=${isSpinnerClick}")
                if (position >= 0 && isSpinnerClick) {

                    val accessible = AccessibleList[position]
                    tvAccessibleItem.text = accessible.title
                    accessible_id = accessible.accessibleId
                    if (accessible.title.equals("none", ignoreCase = true)) {
                        accessible_id = ""
                    }
                    isSpinnerClick = false
                    //updatePreference("", "")
//                        getStateListAPI(country.countryId)

                } else {
//                    val accessible = AccessibleList[0]
//                    tvAccessibleItem.text = accessible.title
//                    accessible_id = accessible.accessibleId
//                    val accessible = AccessibleList[0]
//                    tvAccessibleItem.text = accessible.title
                }
            }
        }


//       /* for (i in 0 until ModeList.size) {
//            Log.e("settingPref", "modeid list =${ModeList[i].modeId} & pref=${repo.pref.mode_id}")
//            if (ModeList[i].modeId.equals(repo.pref.mode_id)) {
//                tvModeItem.text = ModeList[i].title
//                mode_id = ModeList[i].modeId
//            }
//        }
//        for (i in 0 until MusicTypeList.size) {
//            Log.e("settingPref", "musicId list =${MusicTypeList[i].musicId} & pref=${repo.pref.music_id}")
//            if (MusicTypeList[i].musicId.equals(repo.pref.music_id)) {
//                tvMusicItem.text = MusicTypeList[i].title
//                music_id = MusicTypeList[i].musicId
//            }
//        }
//        for (i in 0 until AccessibleList.size) {
//            Log.e("settingPref", "musicId list =${AccessibleList[i].accessibleId} & pref=${repo.pref.accessible_id}")
//            if (AccessibleList[i].accessibleId.equals(repo.pref.accessible_id)) {
//                tvAccessibleItem.text = AccessibleList[i].title
//                accessible_id = AccessibleList[i].accessibleId
//            }
//        }
//
//        var prgrs = 0
//        if (!repo.pref.temperature.isNullOrEmpty()) {
//            prgrs = repo.pref.temperature.toInt()
//        }
//        seeknarTemperature.progress = prgrs
//        temperature = "${prgrs}"*/
    }

    fun updatePreference(parameterName: String, values: String) {

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        var ProfileMap = HashMap<String, String>()
        ProfileMap.put(ApiService.parameters.code, "${repo.pref.languageCode}")
        ProfileMap.put(ApiService.parameters.user_id, "${repo.pref.USER_ID}")
        ProfileMap.put(ApiService.parameters.user_type, "${Constants.customer}")
        ProfileMap.put(ApiService.parameters.temperature, "${temperature}")
        ProfileMap.put(ApiService.parameters.mode_id, mode_id)
        ProfileMap.put(ApiService.parameters.music_id, music_id)
        ProfileMap.put(ApiService.parameters.accessible_id, accessible_id)


        repo.api.profileAPI(ProfileMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetProfileApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        //showLoading()
                    }

                    override fun onNext(response: Response<GetProfileApiResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    // msgDialog("Profile updated successfully")
                                    // getProfileAPICall(false)

                                    repo.pref.mode_id = response.body()!!.payload.preferences.modeId
                                    repo.pref.music_id = response.body()!!.payload.preferences.musicId
                                    repo.pref.accessible_id = response.body()!!.payload.preferences.accessibleId
                                    if (!response.body()!!.payload.preferences.temperature.isNullOrEmpty()) {
                                        repo.pref.temperature = response.body()!!.payload.preferences.temperature
                                    } else {
                                        repo.pref.temperature = "0"
                                    }

                                }
                            }
                        } else {
                            // hideLoading()
                            //baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        // hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    //////////////////////////////////////////////////////////////////////////////////
    var viewPagerVehicleList: ViewPager? = null
    var tvNoVehicle: TextView? = null
    var adapter: VehicleListPagerAdapter? = null
    var defaultSelectedPage = 0
    private fun setVehicleList() {

        // vehicelList.addAll(getVehicleList())
        adapter = VehicleListPagerAdapter(baseActivity!!, vehicleLists)
        tabVehicleList.setupWithViewPager(viewPagerVehicleList)
        viewPagerVehicleList!!.adapter = adapter
        viewPagerVehicleList!!.setPadding(40, 0, 40, 0)
        viewPagerVehicleList!!.pageMargin = 20
        Log.e("onPageSelected", "defaultSelectedPage=$defaultSelectedPage")
        selectedVehicle(defaultSelectedPage)
        viewPagerVehicleList!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                Log.e("onPageSelected", "onPageSelected=$i")
                defaultSelectedPage = i
                selectedVehicle(i)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })


    }

    var oribinalfare = 0.0
    var vehicle_type_id: kotlin.String? = "0"
    var vehicle_name: kotlin.String? = "vehicle_name"
    var vehicle_total_distance = "0"
    var vehicle_total_duration = "0"
    var vehicle_formatted_distance = "0"
    var vehicle_formatted_duration = "0"
    var vehicle_total = ""

    var total_price = "total_price"
    var amount_pay = "amount_pay"

    var fareModelRequest = GetVehicleListApiResponse.Payload.FareInfo()

    private fun selectedVehicle(position: Int) {
//        vehicle_type_id = vehicleList.get(position).getVehicleTypeId()
        vehicle_type_id = "" + vehicleLists[position].vehicleId
        vehicle_name = "" + vehicleLists[position].title

        vehicle_total_distance = "" + vehicleLists[position].totalDistance
        vehicle_total_duration = "" + vehicleLists[position].totalDuration
        vehicle_formatted_distance = "" + vehicleLists[position].formattedDistance
        vehicle_formatted_duration = "" + vehicleLists[position].formattedDuration
        vehicle_total = "" + vehicleLists[position].totalFare
        fareModelRequest = vehicleLists[position].fareInfo

        Log.e("onPageSelected", "vehicle_type_id=$vehicle_type_id")
        Log.e("onPageSelected", "name=" + vehicleLists[position].title)
        total_price = "${vehicleLists[position].totalFare}"
        Log.e("onPageSelected", "total_price=$total_price")
        amount_pay = "${vehicleLists[position].totalFare}"
        Log.e("onPageSelected", "amount_pay=$amount_pay")
        //val aDouble: Double = vehicleList.get(0).getDistance().toDouble() / 1000
//        if (vehicleList.get(position).getTotalPrice().length() === 2) {
//            oribinalfare = vehicleList.get(position).getTotalPrice().substring(1, vehicleList.get(position).getTotalPrice().length()).toDouble()
//        } else {
//            oribinalfare = vehicleList.get(position).getTotalPrice().substring(2, vehicleList.get(position).getTotalPrice().length()).toDouble()
//        }

    }


    var vehicelList = arrayListOf<VehicleModel>()
    fun getVehicleList(): ArrayList<VehicleModel> {

        var list = arrayListOf<VehicleModel>()
        list.add(VehicleModel("Small Car", "Chevrolet", "4", "11"))
        list.add(VehicleModel("Sedan", "Honda", "4", "24"))
        list.add(VehicleModel("SUV", "Hundai", "6", "30"))
        list.add(VehicleModel("Hatchback", "Toyota", "4", "18"))

        return list
    }


    var ModeList = arrayListOf<PreferenceListApiResponse.Payload.Mode>()
    var MusicTypeList = arrayListOf<PreferenceListApiResponse.Payload.Music>()
    var AccessibleList = arrayListOf<PreferenceListApiResponse.Payload.Accessible>()
    fun spinnerData() {
        ModeList.clear()
        var modess = PreferenceListApiResponse.Payload.Mode("None", "None")
        ModeList.add(modess)
        ModeList.addAll(preferenceLists!!.payload.modes)

        MusicTypeList.clear()
        var MusicTypes = PreferenceListApiResponse.Payload.Music("None", "None")
        MusicTypeList.add(MusicTypes)
        MusicTypeList.addAll(preferenceLists!!.payload.musics)

        AccessibleList.clear()
        var Accessibles = PreferenceListApiResponse.Payload.Accessible("None", "None")
        AccessibleList.add(Accessibles)
        AccessibleList.addAll(preferenceLists!!.payload.accessibles)

        //////////////////////////////////////////////////////////////////////////////
//        ModeList.clear()
//        ModeList.add("Select")
//        ModeList.add("Chatty")
//        ModeList.add("Peaceful")
//
//        MusicTypeList.clear()
//        MusicTypeList.add("Select")
//        MusicTypeList.add("Easy Listening")
//        MusicTypeList.add("Listening")
//        MusicTypeList.add("Hip Hop")
//        MusicTypeList.add("r & b")
//        MusicTypeList.add("pop")
//        MusicTypeList.add("jazz")
//        MusicTypeList.add("talk")
//        MusicTypeList.add("Talk Radio")
//
//        AccessibleList.clear()
//        //AccessibleList.add("Select Accessible Type")
//        AccessibleList.add("Wheel Chair")
//        AccessibleList.add("Toddler Car Seat")

    }


    /////////////////////////////////////////////////////////////////////////////////////////////

    var cardLists = arrayListOf<AddCardApiResponse.Payload>()
    var cardNumber = ""
    var cardId = ""
    var cardType = ""
    var cardExpDate = ""
    fun getCardAPICall(isLoading: Boolean) {
        //rvCardList
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getCardAPI(repo.pref.languageCode, repo.pref.USER_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<AddCardApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<AddCardApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        if (response.body()!!.payload.size != 0) {
                                            cardLists.clear()
                                            cardLists.addAll(response.body()!!.payload)

                                            viewCard.visibility = View.VISIBLE
                                            llNoCardLayout.visibility = View.GONE
                                            llCardDetailLayout.visibility = View.VISIBLE
                                            tvPreferences.visibility = View.VISIBLE

                                            for (i in 0 until cardLists.size) {
                                                if (cardLists[i].isDefault) {
                                                    cardNumber = cardLists[i].last4
                                                    cardId = cardLists[i].id
                                                    cardType = cardLists[i].brand
                                                    cardExpDate = "${cardLists[i].expMonth}/${cardLists[i].expYear}"

//                                                    tvCardNo.text = "**** **** **** ${cardNumber}"
                                                    tvCardNo.text = "****** ${cardNumber}"
                                                    tvDebitCardExpiryDate.text = "Exp :- ${cardExpDate}"

                                                    if (cardLists[i].brand.contains(Constants.MasterCard, ignoreCase = true)) {
                                                        imgv_CreditCardBooking.setImageResource(R.drawable.ic_mastercard)
                                                    } else if (cardLists[i].brand.contains(Constants.Visa, ignoreCase = true)) {
                                                        imgv_CreditCardBooking.setImageResource(R.drawable.visa)
                                                    } else if (cardLists[i].brand.contains(Constants.Discover, ignoreCase = true)) {
                                                        imgv_CreditCardBooking.setImageResource(R.drawable.discover)
                                                    } else if (cardLists[i].brand.contains(Constants.American_Express, ignoreCase = true)) {
                                                        imgv_CreditCardBooking.setImageResource(R.drawable.american_express)
                                                    } else {
                                                        imgv_CreditCardBooking.setImageResource(R.drawable.ic_credit_card)
                                                    }

                                                }
                                            }

                                        } else {
                                            llNoCardLayout.visibility = View.VISIBLE
                                            tvPreferences.visibility = View.VISIBLE
                                            viewCard.visibility = View.VISIBLE
                                            llCardDetailLayout.visibility = View.GONE
                                        }
                                    } else {
                                        llNoCardLayout.visibility = View.VISIBLE
                                        tvPreferences.visibility = View.VISIBLE
                                        viewCard.visibility = View.VISIBLE
                                        llCardDetailLayout.visibility = View.GONE
                                    }
                                    //baseActivity!!.onBackPressed()
                                } else {
                                    llNoCardLayout.visibility = View.VISIBLE
                                    tvPreferences.visibility = View.VISIBLE
                                    viewCard.visibility = View.VISIBLE
                                    llCardDetailLayout.visibility = View.GONE
                                }
                            } else {
                                llNoCardLayout.visibility = View.VISIBLE
                                tvPreferences.visibility = View.VISIBLE
                                viewCard.visibility = View.VISIBLE
                                llCardDetailLayout.visibility = View.GONE
                            }
                        } else {
                            llNoCardLayout.visibility = View.VISIBLE
                            tvPreferences.visibility = View.VISIBLE
                            viewCard.visibility = View.VISIBLE
                            llCardDetailLayout.visibility = View.GONE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    /////////////////////////////////////////////////////////////////////////////////////////////

    var vehicleLists = arrayListOf<GetVehicleListApiResponse.Payload>()
    fun getVehicleAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getVehicleAPI(repo.pref.languageCode, repo.pref.USER_ID, pickupAddress,
                dropoffAddress, pickupLat, pickupLong, DropOffLat, DropOffLong)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetVehicleListApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<GetVehicleListApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        if (response.body()!!.payload.size != 0) {
                                            vehicleLists.clear()
                                            vehicleLists.addAll(response.body()!!.payload)
                                            setVehicleList()
                                            tvOrderConfirmBTN.isEnabled = true
                                            tvOrderConfirmBTN!!.visibility = View.VISIBLE
                                            tvNoVehicle!!.visibility = View.GONE

                                            viewPagerVehicleList!!.visibility = View.VISIBLE
                                        } else {
                                            tvOrderConfirmBTN.isEnabled = false
                                            tvNoVehicle!!.visibility = View.VISIBLE
                                            viewPagerVehicleList!!.visibility = View.GONE
                                        }
                                    } else {
                                        tvOrderConfirmBTN.isEnabled = false
                                        tvNoVehicle!!.visibility = View.VISIBLE
                                        viewPagerVehicleList!!.visibility = View.GONE
                                    }
                                    //baseActivity!!.onBackPressed()
                                }
                            }
                        } else {
                            tvOrderConfirmBTN.isEnabled = false
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    var preferenceLists: PreferenceListApiResponse? = null
    fun getPreferencesAPICall(isLoading: Boolean) {

        temperature = "0"
        mode_id = ""
        music_id = ""
        accessible_id = ""

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getPreferenceAPI(repo.pref.languageCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<PreferenceListApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<PreferenceListApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        preferenceLists = response.body()
                                        dialogPreferences()

                                    } else {

                                    }
                                    //baseActivity!!.onBackPressed()
                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private fun dialogRejectJob() {
        val builder = AlertDialog.Builder(baseActivity!!)
        builder.setTitle("Cancel")
        builder.setMessage("Are you sure you want to cancel this trip?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            rejectJobAPICall(true, Constants.action_reject)
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

    fun rejectJobAPICall(isLoading: Boolean, action: String) {
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
                                rlWaitingLayout.visibility = View.GONE
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
    }

}