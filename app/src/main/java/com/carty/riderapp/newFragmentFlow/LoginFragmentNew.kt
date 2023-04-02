package com.carty.riderapp.newFragmentFlow

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carty.riderapp.R
import com.carty.riderapp.responsesNew.LoginApiResponse
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.utils.Permission_Runtime
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login_new.*
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragmentNew.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragmentNew : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var apiInterface: ApiService? = null

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragmentNew().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

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
        return inflater.inflate(R.layout.fragment_login_new, container, false)
    }

    var mobileNumber = ""
    var mobileCountryCode = ""
    var isCheckboxLoginChecked = true
    var email = ""
    var password = ""

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // apiInterface = APIClient().client!!.create(ApiService::class.java)

        Permission_Runtime.askForPermission(Permission_Runtime.PERMISSION_ALL, baseActivity!!)

        /*  isCheckboxLoginChecked = checkboxLogin.isChecked
          Permission_Runtime.askForPermission(Permission_Runtime.PERMISSION_ALL, baseActivity!!)*/

//        ccp.setDialogTextColor(ContextCompat.getColor(baseActivity!!, R.color.white))
 /*       ccp.contentColor = (ContextCompat.getColor(baseActivity!!, R.color.white))
        ccp.setCcpClickable(false)*/

       // setMobileFormat()

        btnNext.setOnClickListener {
            loginAPi()

           /* mobileNumber = edtMobileNumberLogin.text.toString()
            mobileCountryCode = ccp.selectedCountryCodeWithPlus
            isCheckboxLoginChecked = checkboxLogin.isChecked
            fullName = fullNameLogin.text.toString()
            password = appCompatEditTextPassword.text.toString()

            mobileNumber = mobileNumber.replace("(", "")
            mobileNumber = mobileNumber.replace(")", "")
            mobileNumber = mobileNumber.replace(" ", "")
            mobileNumber = mobileNumber.replace("-", "")

            if (fullName.isNullOrEmpty()) {
                msgDialog("Please Enter Full Name")
            } else if (password.isNullOrEmpty()) {
                msgDialog("Please enter Password")
            } else if (mobileNumber.length < 10) {
                msgDialog("Please enter valid mobile number")
            }else if (!isCheckboxLoginChecked) {
                msgDialog("Please accept terms and conditions!")
            } else {
                //for (i in 0 until mobileNumber.length) {

                //  }

                if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
                    loginApiCall()
                }
            }*/

        }
        signUpTV.setOnClickListener {
//            replaceFragment(RegisterFragment(), true, false, R.id.login_container)
//            replaceFragment(RegisterFragment(), false, false)
            replaceFragment(RegisterFragment2(), false, true,R.id.login_container)
        }

       /* tvTermsLogin.setOnClickListener {
//            replaceFragment(WebViewFragment(), true, false, containerViewId = R.id.login_container)
            replaceFragment(WebViewFragment(), false, false)
        }*/
    }

//    fun loginApiCall() {
//        val deviceName = Build.MODEL
//
//        repo.api.signInAPI(repo.pref.languageCode, mobileCountryCode, mobileNumber, repo.pref.one_signal_player_id, deviceName,
//            Constants.android, Constants.customer)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<Response<SignInApiResponse>> {
//                override fun onComplete() {
//
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                    showLoading()
//                }
//
//                override fun onNext(response: Response<SignInApiResponse>) {
//                    hideLoading()
//
//                    if (response.isSuccessful) {
//                        if (response.code() == 200) {
//                            if (response.body() != null) {
//                                repo.pref.USER_ID = response.body()!!.payload.customerId
//                                /*replaceFragment(PhoneVerifyFragment.newInstance(Constants.Login, "${response.body()!!.payload.otp}", ""),
//                                        true, false, R.id.login_container)*/
//
//
//                                var verifyPhone = PhoneVerifyFragment()
//                                var bundle = Bundle()
//                                bundle.putString("from", Constants.Login)
//                                bundle.putString("mobileNumber", mobileNumber)
//                                bundle.putString("mobileCountryCode", mobileCountryCode)
//                                bundle.putString("otp", "${response.body()!!.payload.otp}")
//                                verifyPhone.arguments = bundle
////                                    replaceFragment(verifyPhone, true, false, R.id.login_container)
//                                replaceFragment(verifyPhone, true, false)
//
////                                    /*replaceFragment(PhoneVerifyFragment.newInstance(Constants.Login, "${response.body()!!.payload.otp}", ""),
////                                            true, false, R.id.spalch_container)*/
//                            }
//                        }
//                    } else {
//                        baseActivity!!.showApiResponseERROR(response.errorBody())
//                    }
//
//                    //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
//                }
//
//                override fun onError(e: Throwable) {
//                    hideLoading()
//                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
//                }
//
//            })
//    }

    private fun loginAPi(){
        repo.api.signInAPINew("admin0101","+92","455555533322","imran.mushtaq62442@mailinator.com","customer")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<LoginApiResponse>> {
                override fun onComplete() {
                    Toast.makeText(context, "talha", Toast.LENGTH_SHORT).show()

                }

                override fun onSubscribe(d: Disposable) {
                  //  Toast.makeText(context, "hussain", Toast.LENGTH_SHORT).show()
                    showLoading()
                }

                override fun onNext(response: Response<LoginApiResponse>) {
                    hideLoading()

                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "ok  ", Toast.LENGTH_SHORT).show()
                            if (response.body() != null) {
                                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
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

  /*  private fun loginAPi(){
        val call3: Call<LoginApiResponse?>? = apiInterface?.signInAPINew("admin0101","+92","455555533322","imran.mushtaq62442@mailinator.com","customer")
        call3?.enqueue(object : Callback<LoginApiResponse?> {
            override fun onResponse(
                call: Call<LoginApiResponse?>,
                response: Response<LoginApiResponse?>
            ) {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<LoginApiResponse?>, t: Throwable) {
                Toast.makeText(context, "Faliure", Toast.LENGTH_SHORT).show()
            }

        })
    }*/

//    private fun setMobileFormat() {
//        edtMobileNumberLogin.addTextChangedListener(CommonMethods.onTextChangedListener(edtMobileNumberLogin))
//
//
//        //////////////////////////////////////////////////
////        edtMobileNumberLogin.addTextChangedListener(object : TextWatcher {
////            var beforeLength = 0
////            var digitsValue = ""
////            override fun afterTextChanged(s: Editable?) {
////                edtMobileNumberLogin.removeTextChangedListener(this)
////                try {
////                    var numbres = edtMobileNumberLogin.text.toString()
////                    Log.e("setMobileFormat", "111111 - numbres = $numbres")
////                    //numbres = numbres.replace("()".toRegex(), "")
////                    numbres = numbres.replace("-".toRegex(), "")
////                    numbres = numbres.replace(" ".toRegex(), "")
////                    Log.e("setMobileFormat", "222222 - numbres = $numbres && length = ${numbres.length}")
////
////                    var firstThreeNum = ""
////                    var secondThreeNum = ""
////                    var thirdThreeNum = ""
//////                    if (numbres.length < 4 && numbres.length > 2) {
////                    if (numbres.length == 3 && beforeTextChangedSize < 3) {
////                        firstThreeNum = "(${numbres[0]}${numbres[1]}${numbres[2]}) "
////                        edtMobileNumberLogin.setText("${firstThreeNum}")
////                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
////                    }
//////                    if (numbres.length < 7 && numbres.length > 5) {
////                    if (numbres.length == 9 || numbres.length == 10) {
////                        firstThreeNum = "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
////                        if (numbres.length == 9) {
////                            secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
////                        }else{
////                            secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}-${numbres[9]}"
////                        }
////
////                        //edtMobileNumberLogin.setText("${secondThreeNum}")
////                        edtMobileNumberLogin.setText("${firstThreeNum}${secondThreeNum}")
////                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
////                    }
////
////
////                    if (numbres.length == 12) {
////                        firstThreeNum = "${numbres[0]}${numbres[1]}${numbres[2]}${numbres[3]}${numbres[4]}"
////                        secondThreeNum = " ${numbres[5]}${numbres[6]}${numbres[7]}"
////                        thirdThreeNum = "-${numbres[8]}${numbres[9]}${numbres[10]}${numbres[11]}"
////                        edtMobileNumberLogin.setText("${firstThreeNum}${secondThreeNum}${thirdThreeNum}")
////                        edtMobileNumberLogin.setSelection(edtMobileNumberLogin.text.toString().length)
////                    }
////
////
////                    Log.e("setMobileFormat", "3333333 - numbres = ${edtMobileNumberLogin.text.toString()} && length = ${edtMobileNumberLogin.text.toString().length}")
////                    Log.e("setMobileFormat", "==================================================================")
////                } catch (e: Exception) {
////                    Log.e("setMobileFormat", "4444444 - Exception = ${e.localizedMessage}")
////                    e.printStackTrace()
////                }
////                edtMobileNumberLogin.addTextChangedListener(this)
////            }
////
////            var beforeTextChangedSize = 0
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////                beforeTextChangedSize = edtMobileNumberLogin.text.toString().length
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////
////
////            }
////
////        })
//    }


}