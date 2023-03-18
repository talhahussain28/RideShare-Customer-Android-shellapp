package com.herride.customer.ui.home.setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.herride.customer.R
import com.herride.customer.common.Constants
import com.herride.customer.rest.ApiService
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.address.AddAddressFragment
import com.herride.customer.ui.home.notification.NotificationFragment
import com.herride.customer.ui.home.order_place.response.PreferenceListApiResponse
import com.herride.customer.ui.home.response.GetProfileApiResponse
import com.herride.customer.ui.home.setting.adapter.AddressListAdapter
import com.herride.customer.ui.home.setting.response.AddressGetAddDeleteApiResponse
import com.herride.customer.ui.widgets.NDSpinner
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

class SettingFragment : BaseFragment() {

    var isWheel: Boolean = true
    var isCar: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()

    }

    var isSpinnerClick = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        mode_id
//        music_id
//        accessible_id
//        temperature

        initView()


    }


    private fun initView() {

        imgWheel.setOnClickListener {
            repo.pref.isWheelChair = true
            setSwitched()
            accessible_id = wheelchair_Id
            updatePreference("", "")
        }

        imgCar.setOnClickListener {
            repo.pref.isWheelChair = false
            setSwitched()
            accessible_id = carSeat_Id
            updatePreference("", "")
            /* if (isCar) {
                 isCar = false
                 imgCar.setImageResource(R.drawable.ic_switch_off)
             } else {
                 isCar = true
                 imgCar.setImageResource(R.drawable.ic_switch)
             }*/
        }

        llHome.setOnClickListener {
            /*if (tvAddHomeSetting.text.toString().isNullOrEmpty()) {
            }*/
            addUpdateAddress(Constants.home, homeAddressId, "", "${tvAddHomeSetting.text.toString()}", homeAddressLat, homeAddressLong)
        }
        llWork.setOnClickListener {
            /*if (tvAddWorkSetting.text.toString().isNullOrEmpty()) {
            }*/
            addUpdateAddress(Constants.work, workAddressId, "", "${tvAddWorkSetting.text.toString()}", workAddressLat, workAddressLong)
            // replaceFragment(AddAddressFragment.newInstance(Constants.work, "${tvAddWorkSetting.text.toString()}"), true)
        }
        llOther.setOnClickListener {
            addUpdateAddress(Constants.other, otherAddressId, "${tvAddOtherLBL.text.toString()}", "${tvAddOther.text.toString()}", otherAddressLat, otherAddressLong)
        }

        llAddMoreAddress.setOnClickListener {
            addUpdateAddress(Constants.other, "", "", "", "", "")
        }

        tvPREFERENCESBTN.setOnClickListener {
            dialogPreferences()
        }

        rvEmergencyContact.setOnClickListener {
            replaceFragment(EmergencyContactFragment(), true)
        }

        tvTempLow.text = "0\u2109"
        tvTempHigh.text = "100\u2109"
        tvTemperatureCount.text = "0\u2109"

        tvModeItemSetting.setOnClickListener {
            isSpinnerClick = true
            spinnerModeSetting.performClick()
        }

        tvMusicItemSetting.setOnClickListener {
            isSpinnerClick = true
            spinnerMusicSetting.performClick()
        }

        /* imgAddHomeSettingDelete.setOnClickListener {
             getDeleteAddressAPICall(Constants.delete, Constants.home)
         }
         imgAddWorkSettingDelete.setOnClickListener {
             getDeleteAddressAPICall(Constants.delete, Constants.work)
         }*/

        getDeleteAddressAPICall(Constants.get, Constants.none)
        //getPreferencesAPICall(true)

        seeknarTemperature!!.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                tvTemperatureCount.text = "${progress}℉"
                temperature = "${progress}"

                updatePreference("", "")
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
//        tvSetMorePreferences.setOnClickListener {
//
//            dialogPreferences()
//
//        }

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


        var spinnerModeAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, ModeList)
        spinnerModeSetting.adapter = spinnerModeAdapter
        var spinnerMusicAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, MusicTypeList)
        spinnerMusicSetting.adapter = spinnerMusicAdapter

        spinnerModeSetting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                    val mode = ModeList[position]
                    tvModeItemSetting.text = mode.title
                    modeId = mode.modeId
                    if (mode.title.equals("none", ignoreCase = true)) {
                        modeId = ""
                    }
                    isSpinnerClick = false
                    updatePreference("", "")
//                        getStateListAPI(country.countryId)

                } else {
//                    resetState()
//                    resetCity()
                }
            }
        }

        spinnerMusicSetting.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                    val mode = MusicTypeList[position]

                    tvMusicItemSetting.text = mode.title
                    musicId = mode.musicId
                    if (mode.title.equals("none", ignoreCase = true)) {
                        musicId = ""
                    }
                    isSpinnerClick = false
                    updatePreference("", "")
//                        getStateListAPI(country.countryId)

                } else {
//                    resetState()
//                    resetCity()
                }
            }
        }

        for (i in 0 until ModeList.size) {
            Log.e("settingPref", "modeid list =${ModeList[i].modeId} & pref=${repo.pref.mode_id}")
            if (ModeList[i].modeId.equals(repo.pref.mode_id)) {
                tvModeItemSetting.text = ModeList[i].title
                modeId = ModeList[i].modeId
            }
        }
        for (i in 0 until MusicTypeList.size) {
            Log.e("settingPref", "musicId list =${MusicTypeList[i].musicId} & pref=${repo.pref.music_id}")
            if (MusicTypeList[i].musicId.equals(repo.pref.music_id)) {
                tvMusicItemSetting.text = MusicTypeList[i].title
                musicId = MusicTypeList[i].musicId
            }
        }

        Log.e("settingPref", "accessible_id  pref=${repo.pref.accessible_id}")
        Log.e("settingPref", "temperature  pref=${repo.pref.temperature}")

        accessible_id = repo.pref.accessible_id
        if (accessible_id.equals(carSeat_Id, ignoreCase = true)) {
            repo.pref.isWheelChair = false
            setSwitched()
        } else {
            repo.pref.isWheelChair = true
            setSwitched()
        }
        var prgrs = 0
        if (!repo.pref.temperature.isNullOrEmpty()) {
            prgrs = repo.pref.temperature.toInt()
        }

        seeknarTemperature.progress = prgrs
        temperature = "${prgrs}"
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

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
        tvTitle.text = "Settings"

    }

    private fun addUpdateAddress(addType: String, addressId: String, title: String, address: String, latitude: String, longitude: String) {
        var addressFragment = AddAddressFragment()
        var bundle = Bundle()
        bundle.putString("addressType", "${addType}")
        bundle.putString("title", "${title}")
        bundle.putString("addressId", "${addressId}")
        bundle.putString("${ApiService.parameters.address}", "${address}")
        bundle.putString("${ApiService.parameters.latitude}", latitude)
        bundle.putString("${ApiService.parameters.longitude}", longitude)
        addressFragment.arguments = bundle

//            replaceFragment(AddAddressFragment.newInstance(Constants.home, "${tvAddWorkSetting.text.toString()}"), true)
        replaceFragment(addressFragment, true)
    }

    fun setSwitched() {
        Log.e("settingPref", "isWheelChair  pref=${repo.pref.isWheelChair}")
        if (!repo.pref.isWheelChair) {
            imgWheel.setImageResource(R.drawable.ic_switch_off)
            imgCar.setImageResource(R.drawable.ic_switch)
        } else {

            imgWheel.setImageResource(R.drawable.ic_switch)
            imgCar.setImageResource(R.drawable.ic_switch_off)
        }
    }

    var wheelchair_Id = "ASB160578035680"
    var carSeat_Id = "ASB160578036515"
    /////////////////////////////////////////////////////////////////////////////////////////////

    var preferenceLists: PreferenceListApiResponse? = null
    fun getPreferencesAPICall(isLoading: Boolean) {
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
                                        spinnerData()
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
                        spinnerData()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    ///////////////////////////////////////////////////////////////////////////

    var homeAddressId = ""
    var workAddressId = ""
    var otherAddressId = ""
    var otherAddressTitle = ""
    var selectedAddress = ""
    var latitude = ""
    var longitude = ""
    var address_id = ""

    var addressListAdapter: AddressListAdapter? = null
    var addressList = arrayListOf<AddressGetAddDeleteApiResponse.Payload>()
    fun getDeleteAddressAPICall(actionType: String, address_type: String) {

        addressListAdapter = AddressListAdapter(repo, arrayListOf())
        recyclerAddressView.adapter = addressListAdapter

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        var addressMap = HashMap<String, String>()
        addressMap.put("code", "${repo.pref.languageCode}")
        addressMap.put("customer_id", "${repo.pref.USER_ID}")
        addressMap.put("address", "${selectedAddress}")
        addressMap.put("latitude", "${latitude}")
        addressMap.put("longitude", "${longitude}")
        addressMap.put("action", "${actionType}")
        if (actionType.equals(Constants.delete)) {
            addressMap.put("address_type", "${address_type}")
            if (address_type.equals(Constants.home)) {
                addressMap.put("address_id", "${homeAddressId}")
            } else {
                addressMap.put("address_id", "${workAddressId}")
            }
        }


        repo.api.addAddressAPI(addressMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<AddressGetAddDeleteApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<AddressGetAddDeleteApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        if (response.body()!!.payload.size != 0) {

                                            setAddress(response)
                                        } else {
                                            homeViewVisibleGone(false)
                                            workViewVisibleGone(false)
                                            otherViewVisibleGone(false)
                                        }
                                    } else {
                                        homeViewVisibleGone(false)
                                        workViewVisibleGone(false)
                                        otherViewVisibleGone(false)
                                    }


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


    var homeAddressLat = ""
    var homeAddressLong = ""
    var workAddressLat = ""
    var workAddressLong = ""
    var otherAddressLat = ""
    var otherAddressLong = ""

    fun setAddress(response: Response<AddressGetAddDeleteApiResponse>) {
        homeViewVisibleGone(false)
        workViewVisibleGone(false)
        otherViewVisibleGone(false)
        addressList.clear()
        for (i in 0 until response.body()!!.payload.size) {

            if (response.body()!!.payload[i].type.equals(Constants.home)) {
                tvAddHomeSetting.text = response.body()!!.payload[i].address
                homeAddressId = response.body()!!.payload[i].addressId
                homeAddressLat = "${response.body()!!.payload[i].latitude}"
                homeAddressLong = "${response.body()!!.payload[i].longitude}"

                homeViewVisibleGone(true)
            } else if (response.body()!!.payload[i].type.equals(Constants.work)) {
                tvAddWorkSetting.text = response.body()!!.payload[i].address
                workAddressId = response.body()!!.payload[i].addressId

                workAddressLat = "${response.body()!!.payload[i].latitude}"
                workAddressLong = "${response.body()!!.payload[i].longitude}"

                workViewVisibleGone(true)
            } else {
                /*tvAddOther.text = response.body()!!.payload[i].address
                otherAddressTitle = response.body()!!.payload[i].title
                otherAddressId = response.body()!!.payload[i].addressId

                otherAddressLat = "${response.body()!!.payload[i].latitude}"
                otherAddressLong = "${response.body()!!.payload[i].longitude}"

                otherViewVisibleGone(true)*/
                addressList.add(response.body()!!.payload[i])
            }
        }
        addressListAdapter!!.refreshAddressList(addressList)


//        if (response.body()!!.payload.size == 1) {
//
//
//        } else if (response.body()!!.payload.size == 2) {
//            tvAddHomeSetting.text = response.body()!!.payload[0].address
//            homeAddressId = response.body()!!.payload[0].addressId
//            homeViewVisibleGone(true)
//
//            tvAddWorkSetting.text = response.body()!!.payload[1].address
//            workAddressId = response.body()!!.payload[1].addressId
//            workViewVisibleGone(true)
//        } else {
//            homeViewVisibleGone(false)
//            workViewVisibleGone(false)
//        }

        addressListAdapter!!.setListener(object : AddressListAdapter.AddressClickListener {
            override fun addressSelect(position: Int, action: String) {

                var paylod = addressList[position]
                addUpdateAddress(Constants.other,
                        paylod.addressId,
                        "${paylod.title}",
                        "${paylod.address}",
                        paylod.latitude,
                        paylod.longitude)
            }
        })
    }

    fun homeViewVisibleGone(isVisible: Boolean) {
        if (isVisible) {
            tvAddHomeSetting.visibility = View.VISIBLE
            imgAddHomeSettingDelete.visibility = View.GONE
            tvAddHomeSettingLBL.text = "Home"
        } else {
            tvAddHomeSettingLBL.text = "Add Home"
            tvAddHomeSetting.visibility = View.GONE
            tvAddHomeSetting.text = ""
            imgAddHomeSettingDelete.visibility = View.GONE
        }

    }

    fun workViewVisibleGone(isVisible: Boolean) {
        if (isVisible) {
            tvAddWorkSetting.visibility = View.VISIBLE
            imgAddWorkSettingDelete.visibility = View.GONE
            tvAddWorkSettingLBL.text = "Work"
        } else {
            tvAddWorkSettingLBL.text = "Add Work"
            tvAddWorkSetting.visibility = View.GONE
            tvAddWorkSetting.text = ""
            imgAddWorkSettingDelete.visibility = View.GONE
        }

    }

    fun otherViewVisibleGone(isVisible: Boolean) {
        if (isVisible) {
            tvAddOther.visibility = View.VISIBLE
            imgAddOtherSettingDelete.visibility = View.GONE
            tvAddOtherLBL.text = otherAddressTitle
        } else {
            tvAddOtherLBL.text = "Add Other"
            tvAddOther.visibility = View.GONE
            tvAddOther.text = ""
            imgAddOtherSettingDelete.visibility = View.GONE
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////

    var dialogPreferences: Dialog? = null

    var temperature = "0"
    var modeId = "0"
    var musicId = "0"
    var accessible_id = "0"



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
        getPreferencesAPICall(true)


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
            updatePreference("", "")
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
                    modeId = mode.modeId
                    if (mode.title.equals("none", ignoreCase = true)) {
                        modeId = ""
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

                    musicId = music.musicId
                    if (music.title.equals("none", ignoreCase = true)) {
                        musicId = ""
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
                Log.e("SpinnerSelection", "spinnerAccessible-> onItemSelected $position")
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



        for (i in 0 until ModeList.size) {
            Log.e("settingPref", "modeid list =${ModeList[i].modeId} & pref=${repo.pref.mode_id}")
            if (ModeList[i].modeId.equals(repo.pref.mode_id)) {
                tvModeItem.text = ModeList[i].title
                modeId = ModeList[i].modeId
            }
        }
        for (i in 0 until MusicTypeList.size) {
            Log.e("settingPref", "musicId list =${MusicTypeList[i].musicId} & pref=${repo.pref.music_id}")
            if (MusicTypeList[i].musicId.equals(repo.pref.music_id)) {
                tvMusicItem.text = MusicTypeList[i].title
                musicId = MusicTypeList[i].musicId
            }
        }
        for (i in 0 until AccessibleList.size) {
            Log.e("settingPref", "musicId list =${AccessibleList[i].accessibleId} & pref=${repo.pref.accessible_id}")
            if (AccessibleList[i].accessibleId.equals(repo.pref.accessible_id)) {
                tvAccessibleItem.text = AccessibleList[i].title
                accessible_id = AccessibleList[i].accessibleId
            }
        }

        var prgrs = 0
        if (!repo.pref.temperature.isNullOrEmpty()) {
            prgrs = repo.pref.temperature.toInt()
        }

        seeknarTemperature.progress = prgrs
        temperature = "${prgrs}"
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
        ProfileMap.put(ApiService.parameters.mode_id, modeId)
        ProfileMap.put(ApiService.parameters.music_id, musicId)
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
                                    if (!response.body()!!.payload.preferences.temperature.isNullOrEmpty()){
                                        repo.pref.temperature = response.body()!!.payload.preferences.temperature
                                    }else{
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


}


//
//var dialogPreferences: Dialog? = null
//fun dialogPreferences() {
//    dialogPreferences = Dialog(baseActivity!!)
//    dialogPreferences!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    dialogPreferences!!.setCancelable(false)
//    dialogPreferences!!.setContentView(R.layout.dialog_preferences)
//    dialogPreferences!!.window!!.setBackgroundDrawableResource(R.color.transparent)
//    dialogPreferences!!.window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//    )
//    if (!baseActivity!!.isFinishing) {
//        dialogPreferences!!.show()
//    }
//    spinnerData()
//
//    var seeknarTemperature = dialogPreferences!!.findViewById<SeekBar>(R.id.seeknarTemperature)
//    var imgv_dialog_close = dialogPreferences!!.findViewById<ImageView>(R.id.imgv_dialog_close)
//    var dVerify_btnSubmit = dialogPreferences!!.findViewById<TextView>(R.id.dVerify_btnSubmit)
//    var tvModeItem = dialogPreferences!!.findViewById<TextView>(R.id.tvModeItem)
//    var tvMusicItem = dialogPreferences!!.findViewById<TextView>(R.id.tvMusicItem)
//    var tvAccessibleItem = dialogPreferences!!.findViewById<TextView>(R.id.tvAccessibleItem)
//    var tvTemperatureCount = dialogPreferences!!.findViewById<TextView>(R.id.tvTemperatureCount)
//
//    var spinnerMode = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerMode)
//    var spinnerMusic = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerMusic)
//    var spinnerAccessible = dialogPreferences!!.findViewById<NDSpinner>(R.id.spinnerAccessible)
//    var llAccessible = dialogPreferences!!.findViewById<LinearLayout>(R.id.llAccessible)
//
//
//    llAccessible.visibility = View.GONE
//
//    var spinnerModeAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, ModeList)
//    spinnerMode.adapter = spinnerModeAdapter
//    var spinnerMusicAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, MusicTypeList)
//    spinnerMusic.adapter = spinnerMusicAdapter
//    var spinnerAccessibleAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, AccessibleList)
//    spinnerAccessible.adapter = spinnerAccessibleAdapter
//
//
//
//
//
//    imgv_dialog_close.setOnClickListener {
//        dialogPreferences!!.dismiss()
//    }
//    tvModeItem.setOnClickListener {
//        spinnerMode.performClick()
//    }
//    tvMusicItem.setOnClickListener {
//        spinnerMusic.performClick()
//    }
//    tvAccessibleItem.setOnClickListener {
//        spinnerAccessible.performClick()
//    }
//
//    dVerify_btnSubmit.setOnClickListener {
//        dialogPreferences!!.dismiss()
//    }
//
//    seeknarTemperature!!.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//        override fun onProgressChanged(seek: SeekBar,
//                                       progress: Int, fromUser: Boolean) {
//            // write custom code for progress is changed
//            tvTemperatureCount.text = "${progress}"
//        }
//
//        override fun onStartTrackingTouch(seek: SeekBar) {
//            // write custom code for progress is started
//        }
//
//        override fun onStopTrackingTouch(seek: SeekBar) {
//            // write custom code for progress is stopped
////                Toast.makeText(baseActivity!!,
////                        "Progress is: " + seek.progress + "%",
////                        Toast.LENGTH_SHORT).show()
//        }
//    })
//    spinnerMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            Log.e("TAG", "Country-> onNothingSelected")
//        }
//
//        override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//        ) {
//            Log.e("TAG", "Country-> onItemSelected $position")
//            if (position > 0) {
//
//                val mode = ModeList[position]
//                tvModeItem.text = mode.title
////                        getStateListAPI(country.countryId)
//
//            } else {
////                    resetState()
////                    resetCity()
//            }
//        }
//    }
//
//    spinnerMusic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            Log.e("TAG", "Country-> onNothingSelected")
//        }
//
//        override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//        ) {
//            Log.e("TAG", "Country-> onItemSelected $position")
//            if (position > 0) {
//
//                val mode = MusicTypeList[position]
//
//                tvMusicItem.text = mode.title
////                        getStateListAPI(country.countryId)
//
//            } else {
////                    resetState()
////                    resetCity()
//            }
//        }
//    }
//
//    spinnerAccessible.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//        override fun onNothingSelected(parent: AdapterView<*>?) {
//            Log.e("TAG", "Country-> onNothingSelected")
//        }
//
//        override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//        ) {
//            Log.e("TAG", "Country-> onItemSelected $position")
//            if (position > 0) {
//
//                val mode = AccessibleList[position]
//
//                tvAccessibleItem.text = mode.title
////                        getStateListAPI(country.countryId)
//
//            } else {
////                    resetState()
////                    resetCity()
//            }
//        }
//    }
//}
