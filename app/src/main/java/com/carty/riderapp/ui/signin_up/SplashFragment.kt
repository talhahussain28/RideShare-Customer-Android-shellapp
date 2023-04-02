package com.carty.riderapp.ui.signin_up

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.HomeFragment
import com.carty.riderapp.ui.home.response.GetSettingApiResponse
import com.carty.riderapp.utils.Permission_Runtime
import com.carty.riderapp.utils.RepoModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class SplashFragment : BaseFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    var rlBottom: RelativeLayout? = null
    var tvSignIn: TextView? = null
    var tvSignUp: TextView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo.pref.languageCode = "EN"

        rlBottom = view.findViewById(R.id.rlBottom)
        tvSignIn = view.findViewById(R.id.tvSignIn)
        tvSignUp = view.findViewById(R.id.tvSignUp)

        isnetAvailable()


    }


    private fun initView() {
        getSettingCall()
        Log.e("splashScreen", "is user login = ${repo.pref.isUserLogin}")
        Handler().postDelayed({

            if (repo.pref.isUserLogin) {
                replaceFragment(HomeFragment(), false, true)
            } else {
                Permission_Runtime.askForPermission(Permission_Runtime.PERMISSION_ALL, baseActivity!!)
                if (rlBottom != null) {

                    rlBottom!!.visibility = View.VISIBLE
                    tvSignIn!!.isEnabled = true
                    tvSignUp!!.isEnabled = true
                }
            }
        }, 1000)


        tvSignIn!!.setOnClickListener(this)
        tvSignUp!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvSignIn -> {
                replaceFragment(LoginFragment(), false,true)
            }

            R.id.tvSignUp -> {
                replaceFragment(RegisterFragment(), false,true)
            }
        }
    }


    fun isnetAvailable(): Boolean {

        val connectivityManager =
            baseActivity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        var status = false
        status = activeNetwork?.isConnectedOrConnecting == true

        if (!status) {
            //Toast.makeText(ctx,"No Internet Connection",Toast.LENGTH_SHORT).show();
            DialogInternetError(baseActivity!!, getString(R.string.INTERNET_CONNECTION_ISSUE))
        } else {
            initView()
        }

        return status
    }

    var repoModel: RepoModel? = null
    var errorDialog: Dialog? = null
    fun DialogInternetError(context: Context, msg: String?) {
        if (errorDialog != null && errorDialog!!.isShowing) {
            errorDialog!!.dismiss()
        }
        repoModel = RepoModel(context)
        errorDialog = android.app.Dialog(context)
        errorDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        errorDialog!!.setContentView(R.layout.dialog_ok)
        errorDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        errorDialog!!.setCancelable(false)

        val txt_dialog_msg = errorDialog!!.findViewById<TextView>(R.id.tvMsgInfo)
        val txt_dialog_ok = errorDialog!!.findViewById<TextView>(R.id.btOk)
        txt_dialog_ok.visibility = View.VISIBLE
        txt_dialog_msg.text = msg
        //txt_dialog_ok.setText(repoModel.getLabelPref().getValue(PrefKeys.BTN_DIALOG_OK));

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
        txt_dialog_ok.setOnClickListener {
            errorDialog!!.dismiss()
            isnetAvailable()
        }
        val window = errorDialog!!.window
        val wlp = window!!.attributes
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wlp.windowAnimations = R.style.MaterialDialogSheet
        wlp.gravity = Gravity.TOP
        window.attributes = wlp
        if (!(context as AppCompatActivity).isFinishing) errorDialog!!.show()
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
                                    if (!response.body()!!.payload.callUs.isNullOrEmpty()){
                                        repo.pref.call_us = response.body()!!.payload.callUs
                                    }
                                    if (!response.body()!!.payload.supportEmail.isNullOrEmpty()){
                                        repo.pref.support_email = response.body()!!.payload.supportEmail
                                    }
                                    if (!response.body()!!.payload.defaultCurrency.isNullOrEmpty()){
                                        repo.pref.defaultCurrency = response.body()!!.payload.defaultCurrency
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