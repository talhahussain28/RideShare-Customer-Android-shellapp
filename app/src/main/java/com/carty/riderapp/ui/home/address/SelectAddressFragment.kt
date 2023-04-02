package com.carty.riderapp.ui.home.address

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.LocationSelectionType
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.setting.adapter.AddressListAdapter
import com.carty.riderapp.ui.home.setting.response.AddressGetAddDeleteApiResponse
import com.carty.riderapp.ui.widgets.PlacesAutoCompleteAdapter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_select_address.*
import kotlinx.android.synthetic.main.fragment_select_address.llOther
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectAddressFragment : BaseFragment(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        PlacesAutoCompleteAdapter.ClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_address, container, false)
    }

    override fun onPause() {
        super.onPause()
        baseActivity!!.hideKeyboard()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SelectAddressFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private lateinit var myAutoPlaceAdapter: PlacesAutoCompleteAdapter
    private lateinit var locationSelectionType: LocationSelectionType
    var typeFilter: TypeFilter? = null


    var tvAddHomeLBL: TextView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAddHomeLBL = view.findViewById(R.id.tvAddHomeLBL)

        Places.initialize(baseActivity!!, resources.getString(R.string.map_key))

        locationSelectionType = LocationSelectionType.CURRENT

        Log.e("Select Address", "Select Address Frag")
        //imgv_ClearAddress.visibility = View.INVISIBLE

        getDeleteAddressAPICall(Constants.get, Constants.none)

        edtSearchAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedAddress = ""
                if (edtSearchAddress.text.toString().length > 0) {
                    imgv_ClearAddress.visibility = View.VISIBLE
                } else {
                    imgv_ClearAddress.visibility = View.INVISIBLE
                }

                Log.e("tvDriveAddress", "${edtSearchAddress.text.toString()}")
                Log.e("tvDriveAddressLength", "${edtSearchAddress.text.toString().length}")

                if (edtSearchAddress.text.toString().length > 0 && edtSearchAddress.text.toString().length < 20) {
                    myAutoPlaceAdapter.filter.filter(edtSearchAddress.text.toString())
                    locationSelection_recyclerView.visibility = View.VISIBLE
                } else {
                    locationSelection_recyclerView.visibility = View.GONE
                }
            }

        })

        imgv_ClearAddress.setOnClickListener {
            edtSearchAddress.setText("")
            locationSelection_recyclerView.visibility = View.GONE
        }

        imgAddHomeDelete.setOnClickListener {
            getDeleteAddressAPICall(Constants.delete, Constants.home)
//            tvAddHome.text = ""
//            tvAddHome.visibility = View.GONE
//            imgAddHomeDelete.visibility = View.GONE
        }

        imgAddWorkDelete.setOnClickListener {
            getDeleteAddressAPICall(Constants.delete, Constants.work)
        }

        llOther.setOnClickListener {
/*            if (tvAddWorkSetting.text.toString().isNullOrEmpty()) {

            }*/
            // replaceFragment(AddAddressFragment.newInstance(Constants.other, ""), true)
        }

        tvAddressDone.setOnClickListener {

            if (!selectedAddress.isNullOrEmpty()) {
                val intent = Intent(context, SelectAddressFragment::class.java)
                intent.putExtra("address", edtSearchAddress.text.toString())
                intent.putExtra("addressLatitude", "${latitude}")
                intent.putExtra("addressLongitude", "${longitude}")
                if (targetFragment != null) {
                    targetFragment!!.onActivityResult(
                            targetRequestCode,
                            Activity.RESULT_OK, intent
                    )
                    baseActivity!!.onBackPressed()
                }
            } else {
                Toast.makeText(baseActivity!!, "Please select address!", Toast.LENGTH_SHORT).show()
            }

        }

        llHomeAddress.setOnClickListener {

            if (tvAddHome.text.toString().isNullOrEmpty()) {
//                addFragment(AddAddressFragment.newInstance(Constants.home, ""), true)
                replaceFragment(AddAddressFragment.newInstance(Constants.home, ""), true)
            } else {
                selectedAddress = tvAddHome.text.toString()
                if (!selectedAddress.isNullOrEmpty()) {
                    val intent = Intent(context, SelectAddressFragment::class.java)
                    intent.putExtra("address", tvAddHome.text.toString())
                    intent.putExtra("addressLatitude", "${pickupLat}")
                    intent.putExtra("addressLongitude", "${pickupLong}")
                    if (targetFragment != null) {
                        targetFragment!!.onActivityResult(
                                targetRequestCode,
                                Activity.RESULT_OK, intent
                        )
                        baseActivity!!.onBackPressed()
                    }
                } else {
                    Toast.makeText(baseActivity!!, "Please select address!", Toast.LENGTH_SHORT).show()
                }
            }

        }

        llWorkAddress.setOnClickListener {
            if (tvAddWork.text.toString().isNullOrEmpty()) {
//                addFragment(AddAddressFragment.newInstance(Constants.work, ""), true)
                replaceFragment(AddAddressFragment.newInstance(Constants.work, ""), true)
            } else {
                selectedAddress = tvAddWork.text.toString()
                if (!selectedAddress.isNullOrEmpty()) {
                    val intent = Intent(context, SelectAddressFragment::class.java)
                    intent.putExtra("address", tvAddWork.text.toString())
                    intent.putExtra("addressLatitude", "${DropOffLat}")
                    intent.putExtra("addressLongitude", "${DropOffLong}")

                    if (targetFragment != null) {
                        targetFragment!!.onActivityResult(
                                targetRequestCode,
                                Activity.RESULT_OK, intent
                        )
                        baseActivity!!.onBackPressed()
                    }
                } else {
                    Toast.makeText(baseActivity!!, "Please select address!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        llOther.setOnClickListener {
            if (tvAddOtherSelect.text.toString().isNullOrEmpty()) {
//                addFragment(AddAddressFragment.newInstance(Constants.work, ""), true)
                replaceFragment(AddAddressFragment.newInstance(Constants.other, ""), true)
            } else {
                selectedAddress = tvAddOtherSelect.text.toString()
                if (!selectedAddress.isNullOrEmpty()) {
                    val intent = Intent(context, SelectAddressFragment::class.java)
                    intent.putExtra("address", tvAddOtherSelect.text.toString())
                    intent.putExtra("addressLatitude", "${OtherOffLat}")
                    intent.putExtra("addressLongitude", "${OtherOffLong}")

                    if (targetFragment != null) {
                        targetFragment!!.onActivityResult(
                                targetRequestCode,
                                Activity.RESULT_OK, intent
                        )
                        baseActivity!!.onBackPressed()
                    }
                } else {
                    Toast.makeText(baseActivity!!, "Please select address!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setPlaceAutoCompleteAdapter()
    }


    private fun addUpdateAddress(addType: String, addressId: String, title: String, address: String, latitude: String, longitude: String) {
        var addressFragment = AddAddressFragment()
        var bundle = Bundle()
        bundle.putString("addressType", "${addType}")
        bundle.putString("title", "${title}")
        bundle.putString("${ApiService.parameters.address}", "${address}")
        bundle.putString("${ApiService.parameters.latitude}", latitude)
        bundle.putString("${ApiService.parameters.longitude}", longitude)
        addressFragment.arguments = bundle

//            replaceFragment(AddAddressFragment.newInstance(Constants.home, "${tvAddWorkSetting.text.toString()}"), true)
        replaceFragment(addressFragment, true)
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
        imgNotification.visibility = View.GONE
        tvNotificationCount.visibility = View.GONE
        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_log_out))
        tvTitle.text = "Select Address"

    }


    @SuppressLint("NewApi")
    fun setPlaceAutoCompleteAdapter() {
        myAutoPlaceAdapter = PlacesAutoCompleteAdapter(
                baseActivity!!,
                locationSelectionType, typeFilter, baseActivity!!.repo.pref.mobile_country_code
        )

        //myAutoPlaceAdapter.setListener(this)

        myAutoPlaceAdapter.setClickListener(this)

        locationSelection_recyclerView.layoutManager =
                LinearLayoutManager(baseActivity!!, LinearLayoutManager.VERTICAL, false)
        locationSelection_recyclerView.adapter = myAutoPlaceAdapter
        val itemDecoration =
                DividerItemDecoration(baseActivity!!.applicationContext, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(
                baseActivity!!.resources.getDrawable(R.drawable.divider, baseActivity!!.theme)
        )
        locationSelection_recyclerView.addItemDecoration(itemDecoration)
        myAutoPlaceAdapter.notifyDataSetChanged()


//        binding.fBasicInformationEdtCompanyLocation.onTextChanged {
//
//            if (!isFromGetProfile) {
//                if (it.length > 0 && it.length < 20) {
//                    myAutoPlaceAdapter.filter.filter(it.toString())
//                    binding.locationSelectionRecyclerView.visibility = View.VISIBLE
//                } else {
//                    binding.locationSelectionRecyclerView.visibility = View.GONE
//                }
//            } else {
//                binding.locationSelectionRecyclerView.visibility = View.GONE
//            }
//
//
//        }

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    var selectedAddress = ""
    var latitude = ""
    var longitude = ""
    var address_id = ""
    override fun click(place: Place?) {
        Log.e("geteradddress", "${place!!.address}")
        try {
            var add = place!!.address
            if (!add.isNullOrEmpty()) {


                imgv_ClearAddress.visibility = View.VISIBLE
                edtSearchAddress.setText("${place!!.address}")
                selectedAddress = "${place!!.address}"

                latitude = "${place!!.latLng!!.latitude}"
                longitude = "${place!!.latLng!!.longitude}"

                baseActivity!!.hideKeyboard()
                selectedAddress = "${place!!.address}"
                var latLngs = place!!.latLng

//                repo.pref.selectedAddressLat = "${latLngs!!.latitude}"
//                repo.pref.selectedAddressLong = "${latLngs!!.longitude}"
//                repo.pref.selectedAddress = selectedAddress

                locationSelection_recyclerView.visibility = View.GONE
//                latitudes = "${latLngs!!.latitude}"
//                longitudes = "${latLngs!!.longitude}"

                val intent = Intent(context, SelectAddressFragment::class.java)
                intent.putExtra("address", edtSearchAddress.text.toString())
                intent.putExtra("addressLatitude", "${latitude}")
                intent.putExtra("addressLongitude", "${longitude}")
                if (targetFragment != null) {
                    targetFragment!!.onActivityResult(
                            targetRequestCode,
                            Activity.RESULT_OK, intent
                    )
                    baseActivity!!.onBackPressed()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var homeAddressId = ""
    var workAddressId = ""

    var addressListAdapter: AddressListAdapter? = null
    var addressList = arrayListOf<AddressGetAddDeleteApiResponse.Payload>()

    fun getDeleteAddressAPICall(actionType: String, address_type: String) {

        addressListAdapter = AddressListAdapter(repo, arrayListOf())
        recyclerAddressViewSelect.adapter = addressListAdapter

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


    var pickupLat = ""
    var pickupLong = ""
    var DropOffLat = ""
    var DropOffLong = ""
    var otherAddressTitle = ""
    var otherAddressId = ""
    var OtherOffLat = ""
    var OtherOffLong = ""

    fun setAddress(response: Response<AddressGetAddDeleteApiResponse>) {
        homeViewVisibleGone(false)
        workViewVisibleGone(false)
        otherViewVisibleGone(false)

        addressList.clear()

        for (i in 0 until response.body()!!.payload.size) {
            if (response.body()!!.payload[i].type.equals(Constants.home)) {
                tvAddHome.text = response.body()!!.payload[i].address
                homeAddressId = response.body()!!.payload[i].addressId
                pickupLat = "${response.body()!!.payload[i].latitude}"
                pickupLong = "${response.body()!!.payload[i].longitude}"

                homeViewVisibleGone(true)
            } else if (response.body()!!.payload[i].type.equals(Constants.work)) {
                tvAddWork.text = response.body()!!.payload[i].address
                workAddressId = response.body()!!.payload[i].addressId

                DropOffLat = "${response.body()!!.payload[i].latitude}"
                DropOffLong = "${response.body()!!.payload[i].longitude}"

                workViewVisibleGone(true)
            } else {
                /*tvAddOtherSelect.text = response.body()!!.payload[i].address
                otherAddressTitle= response.body()!!.payload[i].title
                otherAddressId = response.body()!!.payload[i].addressId

                OtherOffLat = "${response.body()!!.payload[i].latitude}"
                OtherOffLong = "${response.body()!!.payload[i].longitude}"

                otherViewVisibleGone(true)*/
                addressList.add(response.body()!!.payload[i])
            }
        }
        addressListAdapter!!.refreshAddressList(addressList)


        addressListAdapter!!.setListener(object : AddressListAdapter.AddressClickListener {
            override fun addressSelect(position: Int, action: String) {

                var paylod = addressList[position]

                val intent = Intent(context, SelectAddressFragment::class.java)
                intent.putExtra("address", paylod.address)
                intent.putExtra("addressLatitude", "${paylod.latitude}")
                intent.putExtra("addressLongitude", "${paylod.longitude}")
                if (targetFragment != null) {
                    targetFragment!!.onActivityResult(
                            targetRequestCode,
                            Activity.RESULT_OK, intent
                    )
                    baseActivity!!.onBackPressed()
                }

                /*addUpdateAddress(Constants.other,
                        paylod.addressId,
                        "${paylod.title}",
                        "${paylod.address}",
                        paylod.latitude,
                        paylod.longitude)*/
            }
        })

//        if (response.body()!!.payload.size == 1) {
//
//
//        } else if (response.body()!!.payload.size == 2) {
//            tvAddHome.text = response.body()!!.payload[0].address
//            homeAddressId = response.body()!!.payload[0].addressId
//            homeViewVisibleGone(true)
//
//            tvAddWork.text = response.body()!!.payload[1].address
//            workAddressId = response.body()!!.payload[1].addressId
//            workViewVisibleGone(true)
//        } else {
//            homeViewVisibleGone(false)
//            workViewVisibleGone(false)
//        }

    }

    fun homeViewVisibleGone(isVisible: Boolean) {
        if (tvAddHomeLBL == null) {
            return
        }
        if (isVisible) {
            tvAddHome.visibility = View.VISIBLE
            imgAddHomeDelete.visibility = View.GONE
            tvAddHomeLBL!!.text = "Home"
        } else {
            tvAddHomeLBL!!.text = "Add Home"
            tvAddHome.visibility = View.GONE
            tvAddHome.text = ""
            imgAddHomeDelete.visibility = View.GONE
        }

    }

    fun workViewVisibleGone(isVisible: Boolean) {
        if (isVisible) {
            tvAddWork.visibility = View.VISIBLE
            imgAddWorkDelete.visibility = View.GONE
            tvAddWorkLBL.text = "Work"
        } else {
            tvAddWorkLBL.text = "Add Work"
            tvAddWork.visibility = View.GONE
            tvAddWork.text = ""
            imgAddWorkDelete.visibility = View.GONE
        }

    }


    fun otherViewVisibleGone(isVisible: Boolean) {
        if (isVisible) {
            tvAddOtherSelect.visibility = View.VISIBLE
            imgAddOtherSelectDelete.visibility = View.GONE
            tvAddOtherSelectLBL.text = otherAddressTitle
        } else {
            tvAddOtherSelectLBL.text = "Add Other"
            tvAddOtherSelect.visibility = View.GONE
            tvAddOtherSelect.text = ""
            imgAddOtherSelectDelete.visibility = View.GONE
        }

    }

}