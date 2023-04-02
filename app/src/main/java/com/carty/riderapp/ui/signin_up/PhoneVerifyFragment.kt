package com.carty.riderapp.ui.signin_up

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.HomeFragment
import com.carty.riderapp.ui.home.response.FirebaseUserData
import com.carty.riderapp.ui.signin_up.response.PhoneVerifyOtpApiResponse
import com.carty.riderapp.ui.signin_up.response.ResendOtpApiResponse
import com.carty.riderapp.ui.signin_up.response.SignInApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_otp_verify.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM0 = "from1"
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OtpVerifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneVerifyFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var from: String? = null
    private var otp: String? = null
    private var verifyMobileId: String? = null

    companion object {
        @JvmStatic
        fun newInstance(from1: String, param1: String, param2: String) =
                PhoneVerifyFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM0, from1)
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    var fullName = ""
    var mobileNumber = ""
    var mobileCountryCode = ""
    var email = ""
    var userPhoto = ""
    var gender = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getString(ARG_PARAM0)
            otp = it.getString(ARG_PARAM1)
            verifyMobileId = it.getString(ARG_PARAM2)
        }


        if (arguments != null) {
            var bundle = arguments

            if (bundle!!.containsKey("from")) {
                from = bundle!!.getString("from")
            }
            if (bundle!!.containsKey("fullName")) {
                fullName = "${bundle!!.getString("fullName")}"
            }
            if (bundle!!.containsKey("mobileNumber")) {
                mobileNumber = "${bundle!!.getString("mobileNumber")}"
            }
            if (bundle!!.containsKey("mobileCountryCode")) {
                mobileCountryCode = "${bundle!!.getString("mobileCountryCode")}"
            }
            if (bundle!!.containsKey("otp")) {
                otp = "${bundle!!.getString("otp")}"
            }
            if (bundle!!.containsKey("verifyMobileId")) {
                verifyMobileId = "${bundle!!.getString("verifyMobileId")}"
            }
            if (bundle!!.containsKey("gender")) {
                gender = "${bundle!!.getString("gender")}"
            }
            if (bundle!!.containsKey("email")) {
                email = "${bundle!!.getString("email")}"
            }
            if (bundle!!.containsKey("userPhoto")) {
                userPhoto = "${bundle!!.getString("userPhoto")}"
            }


        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp_verify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textChangeEvent()

        tvOtpVerify.setOnClickListener {

            var otp = "${edt_no_1.text.toString()}${edt_no_2.text.toString()}${edt_no_3.text.toString()}${edt_no_4.text.toString()}"

            if (otp.isNullOrEmpty()) {
                msgDialog("Please enter otp")
            } else if (otp.length < 4) {
                msgDialog("Please enter valid 4 digit otp")
            } else {
                if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
                    if (from.equals(Constants.Register)) {
                        verifyRegisterOtpApiCall()
                    } else {
                        verifyOtpApiCall()
                    }
                }

            }

        }

        var mobileNumberLength = mobileNumber.length
        tvVerificationNote.text = ("We have sent you an access code via SMS for mobile number verification at \n+1 XXX-XXX-XX${mobileNumber[mobileNumberLength - 2]}${mobileNumber[mobileNumberLength - 1]}")

        if (!otp.isNullOrEmpty() && otp!!.length > 3) {
            edt_no_1.setText("${otp!!.get(0)}")
            edt_no_2.setText("${otp!!.get(1)}")
            edt_no_3.setText("${otp!!.get(2)}")
            edt_no_4.setText("${otp!!.get(3)}")
        }
        imgvBackReg.setOnClickListener { baseActivity!!.onBackPressed() }
        resetTheTimer()
        tvResendOtpIn.setOnClickListener(View.OnClickListener {
            /*  if (baseActivity!!.isInternetAvailable(
                              repo.labelPref.getValue(PrefKeys.INFO_MSG_LANG_PLEASE_CHECK_INTERNET_CONNECTION)
                      )
              ) */
            if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
                if (!isOtpSended) {
                    if (from.equals(Constants.Register)) {
                        resendRegisterOtpApiCall()
                    } else {
                        resendOtpApiCall()
                    }


                }
            }

        })
    }

    fun verifyRegisterOtpApiCall() {
        repo.api.verifyRegisterOtpAPI(repo.pref.languageCode, verifyMobileId!!, otp!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<SignInApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<SignInApiResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    //repo.pref.USER_ID = response.body()!!.payload.customerId

                                    if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
                                        registerApiCall()
                                    }
                                }
                            }
                        } else {
                            hideLoading()
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }


                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    var selectedImageFile: File? = null
    fun registerApiCall() {

        /*var imageRequestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)*/

        mobileNumber = mobileNumber.replace("(", "")
        mobileNumber = mobileNumber.replace(")", "")
        mobileNumber = mobileNumber.replace(" ", "")
        mobileNumber = mobileNumber.replace("-", "")

        val deviceName = Build.MODEL

        var codeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.languageCode)
        var nameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fullName)
        var emailBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        var mobileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileNumber)
        var countrycodeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileCountryCode)
        var userTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), Constants.customer)
        var genderBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), gender)
        var playerIdBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), repo.pref.one_signal_player_id)
        var deviceNameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), deviceName)
        var deviceTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), Constants.android)
        var registerTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "email")


        selectedImageFile = File(userPhoto)
        var imageRequestBody: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedImageFile!!)
        var imageMultipart = MultipartBody.Part.createFormData(
                "profile_picture",
                selectedImageFile!!.absolutePath,
                imageRequestBody
        )

//        var nameBody = RequestBody.create(MediaType.parse("multipart/form-data"),ApiService.parameters.name)
//        var emailBody = RequestBody.create(MediaType.parse("multipart/form-data"),ApiService.parameters.email)
//        var mobileBody = RequestBody.create(MediaType.parse("multipart/form-data"),ApiService.parameters.mobile)
//        var countrycodeBody = RequestBody.create(MediaType.parse("multipart/form-data"),ApiService.parameters.mobile_country_code)
//        var typeBody = RequestBody.create(MediaType.parse("multipart/form-data"),ApiService.parameters.user_type)

        repo.api.signUpAPI(codeBody, nameBody, emailBody, countrycodeBody, mobileBody, genderBody,
                playerIdBody, deviceNameBody, deviceTypeBody, userTypeBody, imageMultipart)
//        repo.api.emialMobileExistCheckAPI_(repo.pref.languageCode, fullName, email, mobileCountryCode, mobileNumber, Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<SignInApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        //  showLoading()
                    }

                    override fun onNext(response: Response<SignInApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (!response.body()!!.payload.customerId.isNullOrEmpty()) {
                                        repo.pref.USER_ID = response.body()!!.payload.customerId
                                    }

/*                                    var intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()*/
                                    repo.pref.isUserLogin = true
                                    repo.pref.availability_status = Constants.active
                                    setFirebaseData()
                                    //replaceFragment(HomeFragment(), false, true)
//                                    replaceFragment(EnableLocationFragment(), true, false, R.id.spalch_container)
//                                    replaceFragment(PhoneVerifyFragment.newInstance(Constants.Register,"${response.body()!!.payload.otp}", "${verifyMobileId}"), true, false, R.id.spalch_container)
                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    ///////////////////////////////////////////////////////////////////
    var user_type = ""
    var user_id = ""
    fun verifyOtpApiCall() {
        user_id = repo.pref.USER_ID
        user_type = Constants.customer
        repo.api.verifyOtpAPI(repo.pref.languageCode, user_type, user_id, otp!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<PhoneVerifyOtpApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<PhoneVerifyOtpApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (!response.body()!!.payload.customerId.isNullOrEmpty()) {
                                        repo.pref.USER_ID = response.body()!!.payload.customerId
                                    }
                                    repo.pref.availability_status = response.body()!!.payload.status
//                                    replaceFragment(EnableLocationFragment(), true, false, R.id.spalch_container)
                                    /*var intent = Intent(activity, MainActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish()*/
                                    repo.pref.isUserLogin = true

                                    setFirebaseData()


                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    private fun setFirebaseData() {
        val database = FirebaseDatabase.getInstance().reference
        val deviceId =
                Settings.Secure.getString(baseActivity!!.contentResolver, Settings.Secure.ANDROID_ID)
        repo.pref.deviceId = deviceId
        val fbUserData = FirebaseUserData(
                deviceId,
                Constants.android,
                "${repo.pref.availability_status}"
        )

        //OneSignal.setSubscription(true)

        database.child("${Constants.devices}/${Constants.devices_Cusomer}")
                .child(repo.pref.USER_ID)
                .setValue(fbUserData)

        baseActivity!!.userSessionListener()

        replaceFragment(HomeFragment(), false, true)
    }


    fun resendRegisterOtpApiCall() {
        // {"error":null,"payload":{"otp":2859},"status":200}
        repo.api.resendRegisterOtpAPI(repo.pref.languageCode, verifyMobileId!!, fullName!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<SignInApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<SignInApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (!response.body()!!.payload.customerId.isNullOrEmpty()) {
                                        repo.pref.USER_ID = response.body()!!.payload.customerId
                                    }
                                    msgDialog("OTP has been resent successfully")
                                    otp = "${response.body()!!.payload.otp}"
                                    if (!otp.isNullOrEmpty() && otp!!.length > 3) {
                                        edt_no_1.setText("${otp!!.get(0)}")
                                        edt_no_2.setText("${otp!!.get(1)}")
                                        edt_no_3.setText("${otp!!.get(2)}")
                                        edt_no_4.setText("${otp!!.get(3)}")
                                    }

                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }


                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    fun resendOtpApiCall() {
        repo.api.resendOtpAPI(repo.pref.languageCode, Constants.customer, repo.pref.USER_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<ResendOtpApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<ResendOtpApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    msgDialog("OTP has been resent successfully")
                                    otp = "${response.body()!!.payload.otp}"
                                    if (!otp.isNullOrEmpty() && otp!!.length > 3) {
                                        edt_no_1.setText("${otp!!.get(0)}")
                                        edt_no_2.setText("${otp!!.get(1)}")
                                        edt_no_3.setText("${otp!!.get(2)}")
                                        edt_no_4.setText("${otp!!.get(3)}")
                                    }

                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }


                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    var isOtpSended = false
    lateinit var countdownTimer: CountDownTimer
    fun resetTheTimer() {

        return

        isOtpSended = true
        tvDontReceiveOtp.visibility = View.VISIBLE

        countdownTimer = object : CountDownTimer(Constants.OTP_COUNTDOWN_TIMER, 1000) {
            override fun onFinish() {
                try {
                    /*tvResendOtpIn!!.text =
                            if (!repo.labelPref.getValue(PrefKeys.BTN_OTP_RESEND).isNullOrEmpty())
                                repo.labelPref.getValue(PrefKeys.BTN_OTP_RESEND) else "Resend OTP"*/
                    tvResendOtpIn!!.text = "Resend OTP"
                    tvDontReceiveOtp.visibility = View.VISIBLE
                    isOtpSended = false
                } catch (e: Exception) {
                    Log.e("countdownTimer", "11 Exception = ${e.message}")
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                try {
                    tvDontReceiveOtp.visibility = View.VISIBLE
                    /*  tvResendOtpIn.text =
                              if (!repo.labelPref.getValue(PrefKeys.LBL_OTP_RESEND_IN)
                                              .isNullOrEmpty()
                              )
                                  " ${repo.labelPref.getValue(PrefKeys.LBL_OTP_RESEND_IN)} 00:${millisUntilFinished / 1000}"
                              else "OTP Resend In 00:${millisUntilFinished / 1000}"*/
                    tvResendOtpIn.text =
                            "OTP Resend In 00:${millisUntilFinished / 1000}"

                    Log.e("countdownTimer", "${millisUntilFinished / 1000}")
                } catch (e: Exception) {
                    Log.e("countdownTimer", "22 Exception = ${e.message}")
                }

            }
        }

        countdownTimer.start()


//        Timer("resendOtp",false)
//            .schedule(TimeUnit.SECONDS.toMillis(10)){
//                //Log.e("resetTheTimer")
//            }
    }


    ///////////////////////////////////////////////////
    private fun textChangeEvent() {
        edt_no_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0) {
                    edt_no_2.requestFocus()
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (charSequence.length > 0 && charSequence.length == 1) {
                    edt_no_2.requestFocus()
                } else if (charSequence.length == 0) {
                    edt_no_1.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        edt_no_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0) {
                    edt_no_3.requestFocus()
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (charSequence.length > 0 && charSequence.length == 1) {
                    edt_no_3.requestFocus()
                } else if (charSequence.length == 0) {
                    edt_no_1.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        edt_no_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0) {
                    edt_no_4.requestFocus()
                }
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (charSequence.length > 0 && charSequence.length == 1) {
                    edt_no_4.requestFocus()
                } else if (charSequence.length == 0) {
                    edt_no_2.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        edt_no_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                if (charSequence.length > 0 && charSequence.length == 1) {
                    val imm = baseActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(edt_no_4.windowToken, 0)
                } else if (charSequence.length == 0) {
                    edt_no_3.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }

}