package com.carty.riderapp.newFragmentFlow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.payment_card_setting.response.AddCardApiResponse
import com.google.android.libraries.places.api.model.Place
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_payment_card_add.*
import kotlinx.android.synthetic.main.fragment_payment_setting.*
import kotlinx.android.synthetic.main.fragment_register2.*
import retrofit2.Response


class PaymentCardAddFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var homeView: View? = null
    var card_number: EditText? =null
    var monthYear :EditText? =null
    var cvn_no : EditText? =null
    var billing = ""
    var card_no: String =""
    var months :String =""
    var cvn_n : String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeView =  inflater.inflate(R.layout.fragment_payment_card_add, container, false)
        card_number = homeView!!.findViewById(R.id.cardNumber)
        monthYear = homeView!!.findViewById(R.id.appCompatEditTextcard)
        cvn_no = homeView!!.findViewById(R.id.appCompatEditTextCVN)
        return homeView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        card_no = card_number?.text.toString()

        cvn_n = cvn_no?.text.toString()
       // var card_Number = ""


        var name = ""
        /*card_Number = cardNumber.text.toString()
        monthYear = appCompatEditTextcard.text.toString()
        cvn_no = appCompatEditTextCVN.text.toString()*/
        //
        btnSave.setOnClickListener {
            months = monthYear?.text.toString()
            val cardexpDate2 = months.split("/")
            val month = cardexpDate2[0]
            val year = cardexpDate2[1]

            addCardAPICall(repo.pref.USER_ID,card_number?.text.toString(),monthYear?.text.toString(),"2022",cvn_no?.text.toString())
        }
    }

    fun addCardAPICall(card_name: String, card_number: String, exp_month: String, exp_year: String, cvv: String) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        var cardMap = HashMap<String, String>()
       // cardMap.put("code", "${repo.pref.languageCode}")
        cardMap.put("customer_id", "${repo.pref.USER_ID}")
        cardMap.put("card_name", "${card_name}")
        cardMap.put("card_number", "${card_number}")
        cardMap.put("exp_month", "${exp_month}")
        cardMap.put("exp_year", "${exp_year}")
        cardMap.put("cvc", "${cvv}")

        repo.api.addCardAPI(cardMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<AddCardApiResponse>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    showLoading()
                }

                override fun onNext(response: Response<AddCardApiResponse>) {


                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                msgDialog("Card added successfully")
                              //  dialogAddCard!!.dismiss()
                               // getCardAPICall(false)
                                //baseActivity!!.onBackPressed()
                            }
                        }
                    } else {
                        hideLoading()
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
                                        Toast.makeText(requireContext(), "Cards are Available", Toast.LENGTH_SHORT).show()
                                    }
                                } else {

                                    baseActivity!!.showApiResponseERROR(response.errorBody())
                                }

                                //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                            }
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }

}