package com.carty.riderapp.ui.home.address

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.LocationSelectionType
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.setting.response.AddressGetAddDeleteApiResponse
import com.carty.riderapp.ui.widgets.PlacesAutoCompleteAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.carty.riderapp.rest.ApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_address.*
import kotlinx.android.synthetic.main.fragment_add_address.imgv_ClearAddress
import kotlinx.android.synthetic.main.fragment_select_address.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddAddressFragment : BaseFragment(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        PlacesAutoCompleteAdapter.ClickListener {

    private var addressType: String? = null
    private var addressTitle: String = ""
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddAddressFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addressType = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (arguments != null) {

            var bundle = arguments
            if (bundle!!.containsKey("addressType")) {
                addressType = bundle!!.getString("addressType")
             }
            if (bundle!!.containsKey("title")) {
                addressTitle = "" + bundle!!.getString("title")
             }

            if (bundle!!.containsKey("${ApiService.parameters.address}")) {
                selectedAddress = "" + bundle!!.getString("${ApiService.parameters.address}")
            }

            if (bundle!!.containsKey("addressId")) {
                addressId = "" + bundle!!.getString("addressId")
            }

            if (bundle!!.containsKey("${ApiService.parameters.latitude}")) {
                latitude = "" + bundle!!.getString("${ApiService.parameters.latitude}")
            }
            if (bundle!!.containsKey("${ApiService.parameters.longitude}")) {
                longitude = "" + bundle!!.getString("${ApiService.parameters.longitude}")
            }

        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_address, container, false)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }

    private fun setToolbar() {
        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }
        if (!selectedAddress.isNullOrEmpty())
        {
            frNotification.visibility = View.VISIBLE
            tvNotificationCount.visibility = View.GONE
        }
        imgIconBack.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_arrow_left))

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_delete))

        tvTitle.text = "Add Address"

        imgNotification.setOnClickListener {
            dialogDelete()
        }
    }

    private lateinit var myAutoPlaceAdapter: PlacesAutoCompleteAdapter
    private lateinit var locationSelectionType: LocationSelectionType
    var typeFilter: TypeFilter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Places.initialize(baseActivity!!, resources.getString(R.string.map_key))
        locationSelectionType = LocationSelectionType.CURRENT
        initView()

    }

    private fun dialogDelete() {
        val builder = AlertDialog.Builder(baseActivity!!)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete this address?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->

            getDeleteAddressAPICall(Constants.delete)
        }

        builder.setNegativeButton("No") { dialog, which ->

        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(applicationContext,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }


    private fun initView() {


        if (addressType.equals(Constants.home)) {
            //setHomeWorkSelection(true)
            tvHomeLBL.setText("Home")
            tvHomeLBL.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.grayText))
            tvHomeLBL.isEnabled = false
        } else if (addressType.equals(Constants.work)) {
            tvHomeLBL.setText("Work")
            tvHomeLBL.setTextColor(ContextCompat.getColor(baseActivity!!,R.color.grayText))
            tvHomeLBL.isEnabled = false
            //setHomeWorkSelection(true)
        } else {

            tvHomeLBL.isEnabled = true
            // setHomeWorkSelection(false)
            if (addressType.equals(Constants.other,ignoreCase = true) && !addressTitle.equals("Add Other",ignoreCase = true)) {
                tvHomeLBL.setText("${addressTitle}")
            }else{
                tvHomeLBL.hint = "Title"
            }

        }

        if (!selectedAddress!!.isNullOrEmpty()) {
            edtSelectAddress.setText("${selectedAddress}")
        }



        /*llHome.setOnClickListener {
            setHomeWorkSelection(true)
        }

        llWork.setOnClickListener {
            setHomeWorkSelection(false)
        }*/
        tvSaveAndProceed.setOnClickListener {
            if (addressType.equals(Constants.other) && tvHomeLBL.text.toString().isNullOrEmpty()) {
                msgDialog("Please enter title of address")
            } else if (edtSelectAddress.text.toString().isNullOrEmpty()) {
                msgDialog("Please select address")
            } else if (selectedAddress.isNullOrEmpty()) {
                msgDialog("Please select valid address")
            } else {
                getDeleteAddressAPICall(Constants.add)
            }


        }
        edtSelectAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedAddress = ""
                if (edtSelectAddress.text.toString().length > 0) {
                    imgv_ClearAddress.visibility = View.VISIBLE
                } else {
                    imgv_ClearAddress.visibility = View.INVISIBLE
                }

                Log.e("tvDriveAddress", "${edtSelectAddress.text.toString()}")
                Log.e("tvDriveAddressLength", "${edtSelectAddress.text.toString().length}")

                if (edtSelectAddress.text.toString().length > 0 && edtSelectAddress.text.toString().length < 20) {
                    myAutoPlaceAdapter.filter.filter(edtSelectAddress.text.toString())
                    locationSelection_recyclerView1.visibility = View.VISIBLE
                } else {
                    locationSelection_recyclerView1.visibility = View.GONE
                }
            }

        })

        imgv_ClearAddress.setOnClickListener {
            edtSelectAddress.setText("")
            locationSelection_recyclerView1.visibility = View.GONE
        }

        setPlaceAutoCompleteAdapter()
    }

    fun setHomeWorkSelection(isHomeSelect: Boolean) {
        if (isHomeSelect) {
            addressType = Constants.home
            imgWork.setImageResource(R.drawable.ic_add_work_black)
            imgHome.setImageResource(R.drawable.ic_add_home_blue)
            tvHomeLBL.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.colorPrimary))
            tvWorkLBL.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.color0C0C0C))
        } else {
            addressType = Constants.work
            imgHome.setImageResource(R.drawable.ic_add_home_black)
            imgWork.setImageResource(R.drawable.ic_add_work_blue)
            tvWorkLBL.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.colorPrimary))
            tvHomeLBL.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.color0C0C0C))
        }
    }


    @SuppressLint("NewApi")
    fun setPlaceAutoCompleteAdapter() {
        myAutoPlaceAdapter = PlacesAutoCompleteAdapter(
                baseActivity!!,
                locationSelectionType, typeFilter, baseActivity!!.repo.pref.mobile_country_code
        )

        //myAutoPlaceAdapter.setListener(this)

        myAutoPlaceAdapter.setClickListener(this)

        locationSelection_recyclerView1.layoutManager =
                LinearLayoutManager(baseActivity!!, LinearLayoutManager.VERTICAL, false)
        locationSelection_recyclerView1.adapter = myAutoPlaceAdapter
        val itemDecoration =
                DividerItemDecoration(baseActivity!!.applicationContext, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(
                baseActivity!!.resources.getDrawable(R.drawable.divider, baseActivity!!.theme)
        )
        locationSelection_recyclerView1.addItemDecoration(itemDecoration)
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
    var addressId = ""
    var latitude = ""
    var longitude = ""
    var address_id = ""
    override fun click(place: Place?) {
        Log.e("geteradddress", "${place!!.address}")
        try {
            var add = place!!.address
            if (!add.isNullOrEmpty()) {


                imgv_ClearAddress.visibility = View.VISIBLE
                edtSelectAddress.setText("${place!!.address}")
                selectedAddress = "${place!!.address}"

                latitude = "${place!!.latLng!!.latitude}"
                longitude = "${place!!.latLng!!.longitude}"

                baseActivity!!.hideKeyboard()
                var latLngs = place!!.latLng

//                repo.pref.selectedAddressLat = "${latLngs!!.latitude}"
//                repo.pref.selectedAddressLong = "${latLngs!!.longitude}"
//                repo.pref.selectedAddress = selectedAddress

                locationSelection_recyclerView.visibility = View.GONE
//                latitudes = "${latLngs!!.latitude}"
//                longitudes = "${latLngs!!.longitude}"

                val intent = Intent(context, SelectAddressFragment::class.java)
                intent.putExtra("address", edtSelectAddress.text.toString())
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


    fun getDeleteAddressAPICall(actionType: String) {

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        var addressMap = HashMap<String, String>()
        addressMap.put("code", "${repo.pref.languageCode}")
        addressMap.put("customer_id", "${repo.pref.USER_ID}")
        addressMap.put("action", "${actionType}")
        addressMap.put("address", "${selectedAddress}")
        addressMap.put("latitude", "${latitude}")
        addressMap.put("longitude", "${longitude}")
        addressMap.put("address_type", "${addressType!!}")
        if (actionType.equals(Constants.delete)) {
            //addressMap.put("address_type", "${address_type}")
            addressMap.put("address_id", "${addressId}")
        }

        //addressMap.put("address_id", "${address_id}")
        if (addressType.equals(Constants.other)) {
            addressMap.put("title", "${tvHomeLBL.text.toString()}")
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
                                   // msgDialog("Address added successfully")
                                    baseActivity!!.onBackPressed()
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

}