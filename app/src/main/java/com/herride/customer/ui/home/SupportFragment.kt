package com.herride.customer.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.herride.customer.R
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.response.GetSettingApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_support.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SupportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SupportFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SupportFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    var tvNumberLBL: TextView? = null
    var tvEmailIdLBL: TextView? = null

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
        return inflater.inflate(R.layout.fragment_support, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvNumberLBL = view.findViewById(R.id.tvNumberLBL)
        tvEmailIdLBL = view.findViewById(R.id.tvEmailIdLBL)

        setToolbar()
        getSettingCall()
        tvNumberLBL!!.text = "${repo.pref.call_us}"
        tvEmailIdLBL!!.text = "${repo.pref.support_email}"

        imgvCall.setOnClickListener {
            placeCall(repo.pref.call_us)
        }
        imgvEmail.setOnClickListener {
            try {

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.setData(Uri.parse("mailto:")) // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("${repo.pref.support_email}"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback")
                startActivity(intent)
            } catch (ex: Exception) {
                Log.e("tvCallUsOnEmail","Exception = ${ex.localizedMessage}")

            }
        }

    }

    private fun placeCall(number: String) {
        baseActivity!!.permissionCheckingOne(
                baseActivity!!.CALL_PHONE,
                isForceFullyPermission = true,
                isCalling = true,
                contactNumber = number

        )
    }

    private fun setToolbar() {

        imgIconBack.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )
        frNotification.visibility = View.GONE
        tvNotificationCount.visibility = View.GONE
        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_log_out))

        imgIconBack.setOnClickListener { baseActivity!!.onBackPressed() }
        imgNotification.setOnClickListener {


        }

        tvTitle.text = "Support"

    }

    fun getSettingCall() {
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
                                        if (tvNumberLBL!=null){
                                            tvNumberLBL!!.text = "${repo.pref.call_us}"
                                        }
                                        if (tvEmailIdLBL!=null){
                                            tvEmailIdLBL!!.text = "${repo.pref.support_email}"
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

}