package com.herride.customer.ui.home.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.herride.customer.R
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.setting.response.EmergencyContactApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_emergency_contact.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

class EmergencyContactFragment : BaseFragment() {

    lateinit var emergencyContactAdapter: EmergencyContactAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false)
    }

    override fun onResume() {
        super.onResume()
        frNotification.visibility = View.GONE
        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }
        imgIconBack.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_arrow_left))
        tvTitle.text = "Emergency Contacts"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setData()
        initView()
    }

    private fun initView() {
        emergencyContactAdapter = EmergencyContactAdapter(arrayListOf())
        rvContactList.adapter = emergencyContactAdapter
        tvAddNewContact.setOnClickListener {
            replaceFragment(AddContactFragment(), true)
        }
        getContactAPICall(true)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    var contactList = arrayListOf<EmergencyContactApiResponse.Payload>()
    fun getContactAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getEmergencyContactAPI(repo.pref.languageCode, repo.pref.USER_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<EmergencyContactApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<EmergencyContactApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        var paylod = response.body()!!.payload
                                        contactList.clear()
                                        contactList.addAll(paylod)
                                        emergencyContactAdapter.updateList(contactList)
                                        if (contactList.size == 0){
                                            tvNoContactLBL.visibility = View.VISIBLE
                                        }
                                    } else {
                                        tvNoContactLBL.visibility = View.VISIBLE
                                    }
                                    //baseActivity!!.onBackPressed()
                                } else {
                                    tvNoContactLBL.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            tvNoContactLBL.visibility = View.VISIBLE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        tvNoContactLBL.visibility = View.VISIBLE
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

//    private fun setData() {
//        contactList.clear()
//        contactList.add(
//                ContactModel(
//                        R.drawable.ic_profile_place_holder,
//                        "JOHN SMITH",
//                        "+1 XXXX-XXXX-XXXX"
//                )
//        )
//        contactList.add(
//                ContactModel(
//                        R.drawable.ic_profile_place_holder,
//                        "JOHN SMITH",
//                        "+1 XXXX-XXXX-XXXX"
//                )
//        )
//        contactList.add(
//                ContactModel(
//                        R.drawable.ic_profile_place_holder,
//                        "JOHN SMITH",
//                        "+1 XXXX-XXXX-XXXX"
//                )
//        )
//    }
}