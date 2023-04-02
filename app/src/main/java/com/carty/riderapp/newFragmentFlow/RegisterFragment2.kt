package com.carty.riderapp.newFragmentFlow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.carty.riderapp.R
import com.carty.riderapp.common.CommonMethods
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.model.States
import com.carty.riderapp.responsesNew.SignUpResponseNew
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.utils.ImagePicker
import com.carty.driver.ui.webview.WebViewFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register2.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class RegisterFragment2 : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment2().apply {
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
        //commented by MTH
        //  getCMSApiCall()
    }

    var picker: ImagePicker = ImagePicker(repo)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register2, container, false)
    }


    var fullName = ""
    var mobileNumber = ""
    var password = ""
    var mobileCountryCode = "+92"
    var email = ""
    var userPhoto = ""
    var state = ""
    var isCheckboxRegisterChecked = false
    var isSpinnerClick = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUsPhoneNumberFormater()

        setStatesSpinnerAdapter()
        setCitySpinnerAdapter()

        btnNext.setOnClickListener {
            //isCheckboxRegisterChecked = checkboxRegister.isChecked
            fullName = full_name.text.toString()
            email = email_textView.text.toString()
            password = password_textView.text.toString()
            mobileNumber = phone_number.text.toString()
            mobileNumber = phone_number.text.toString()

            mobileNumber = mobileNumber.replace("(", "")
            mobileNumber = mobileNumber.replace(")", "")
            mobileNumber = mobileNumber.replace(" ", "")
            mobileNumber = mobileNumber.replace("-", "")

            // mobileCountryCode = ccpRegister.selectedCountryCodeWithPlus
            //MTH
            /*       if (selectedImageFile == null) {
                msgDialog("Please select profile picture")
            } else if (fullName.isNullOrEmpty()) {
                msgDialog("Please enter full name")
            } else if (email.isNullOrEmpty()) {
                msgDialog("Please enter email address")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                msgDialog("Please enter valid email address")
            } *//*else if (mobileCountryCode.isNullOrEmpty()) {
                msgDialog("Please select mobile country code")
            }*//* else if (mobileNumber.isNullOrEmpty()) {
                msgDialog("Please enter mobile number")
            } else if (mobileNumber.length < 10) {
                msgDialog("Please enter valid mobile number")
            } else if (state.isNullOrEmpty() || state.equals("state", ignoreCase = true)) {
                msgDialog("Please select gender")
            } else if (!isCheckboxRegisterChecked) {
                msgDialog("Please accept terms and conditions!")
            } else {
                if (baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
                    //emailMobileExistCheckApiCall()
                }
            }*/
            sigup()

        }
        terms_conditions.setOnClickListener {
//            replaceFragment(WebViewFragment.newInstance("${Constants.TERMS_CONDITIONS}",""), true, false, containerViewId = R.id.login_container)
            replaceFragment(
                WebViewFragment.newInstance("${Constants.TERMS_CONDITIONS}", ""),
                true,
                false
            )
        }
        signInTV.setOnClickListener {
            //   replaceFragment(LoginFragment(), false, true, containerViewId = R.id.main_content_login)
            replaceFragment(LoginFragmentNew(), false, true, R.id.login_container)
        }

       /* stateList = spinnerGenderData()

        var spinnerGenderAdapter =
            ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, stateList)
        spinnerGender.adapter = spinnerGenderAdapter
*/

        /*spinnerStates.setOnClickListener {
            setStatesSpinnerAdapter()
        }*/

        /*   imgvBackReg.setOnClickListener {

            baseActivity!!.onBackPressed()
        }*/

/*
        spinnerStates.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    isSpinnerClick = false
                    val mState = stateList[position]
                    tvGenderItem.text = mState
                    state = mState.toLowerCase()
//                        getStateListAPI(country.countryId)

                } else {
//                    resetState()
//                    resetCity()
                }
            }
        }

*/

        cardViewImageSelection.setOnClickListener {
            if (picker.isAdded) {
                return@setOnClickListener
            }
            picker.show(baseActivity!!.supportFragmentManager, "")
        }
        appCompatCheckBox2.setOnClickListener {
            if (isCheckboxRegisterChecked) {
                isCheckboxRegisterChecked = false
                //checkboxRegister.setImageResource(R.drawable.ic_unchecked_checkbox)
            } else {
                // checkboxRegister.setImageResource(R.drawable.ic_checked_checkbox)
                isCheckboxRegisterChecked = true

            }
        }



        picker.setListener { imagePath ->
            userPhoto = imagePath!!
            val file = File(imagePath!!)
            selectedImageFile = File(imagePath!!)
            Log.e("file", "" + file)
            val compressFile = baseActivity!!.compressImage(file.absolutePath)
            Log.e("compressFile", compressFile)
            if (!imagePath.isNullOrEmpty()) {
                GlobalMethods().loadImagesWithProgressbar(
                    imagePath,
                    imageViewBonus,
                    R.drawable.ic_user,
                    pbLoadingEditRegister,
                    300,
                    300
                )
            }

            //                if (baseActivity!!.isInternetAvailable(
            //                                repo.labelPref.getValue(PrefKeys.INFO_MSG_LANG_PLEASE_CHECK_INTERNET_CONNECTION)
            //                        )
            //                ) {
            //                    uploadPhotoAPI(File(compressFile))
            //                }
        }


    }


    var selectedImageFile: File? = null
    /* fun emailMobileExistCheckApiCall() {

        *//*var imageRequestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)*//*



        var codeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.languageCode)
        var nameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fullName)
        var emailBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        var mobileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileNumber)
        var countrycodeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileCountryCode)
        var userTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), Constants.customer)
        var registerTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "email")

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

//        repo.api.signUpAPI(codeBody, nameBody, emailBody, countrycodeBody, mobileBody, userTypeBody, imageMultipart)
        repo.api.emialMobileExistCheckAPI_(repo.pref.languageCode, fullName, email, mobileCountryCode, mobileNumber, Constants.customer)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<EmailMObileExistApiResponse>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    showLoading()
                }

                override fun onNext(response: Response<EmailMObileExistApiResponse>) {
                    hideLoading()

                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                var verifyMobileId = "${response.body()!!.payload.verifyMobileId}"
                                var otp = "${response.body()!!.payload.otp}"

                                var verifyPhone = PhoneVerifyFragment()
                                var bundle = Bundle()
                                bundle.putString("from", Constants.Register)
                                bundle.putString("fullName", fullName)
                                bundle.putString("mobileNumber", mobileNumber)
                                bundle.putString("mobileCountryCode", mobileCountryCode)
                                bundle.putString("otp", otp)
                                bundle.putString("verifyMobileId", verifyMobileId)
                                bundle.putString("gender", state)
                                bundle.putString("email", email)
                                bundle.putString("userPhoto", userPhoto)
                                verifyPhone.arguments = bundle

                                replaceFragment(verifyPhone, true, false)
//                                    replaceFragment(verifyPhone, true, false, R.id.login_container)
//                                    replaceFragment(PhoneVerifyFragment.newInstance(Constants.Register,"${response.body()!!.payload.otp}", "${verifyMobileId}"), true, false, R.id.spalch_container)
                            }
                        }
                    } else {
                        baseActivity!!.showApiResponseERROR(response.errorBody())
                    }
                    // replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                }

            })
    }*/
//commented by MTH
    /*fun getCMSApiCall() {

        repo.api.getCMSAPI(repo.pref.languageCode, Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetCMSApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        // showLoading()
                    }

                    override fun onNext(response: Response<GetCMSApiResponse>) {
                        //hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    for (i in response.body()!!.payload) {
                                        if (i.code.equals(Constants.PRIVACY_POLICY)) {
                                            repo.pref.PRIVACY_POLICY = i.link
                                        }
                                        if (i.code.equals(Constants.TERMS_CONDITIONS)) {
                                            repo.pref.TERMS_CONDITIONS = i.link
                                        }
                                    }
                                }
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }
                        // replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }*/
//commented by MTH

    private fun setUsPhoneNumberFormater() {
        phone_number.addTextChangedListener(CommonMethods.onTextChangedListener(phone_number));
    }

    private fun sigup() {
        var nameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fullName)
        var emailBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        var mobileBody =RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "um3123123123")
        var passwordBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)
        var countrycodeBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileCountryCode)
        var userTypeBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), Constants.customer)
        var genderBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "male")
        var stateBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Sindh")
        var cityBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "Karachi")

        repo.api.signUpAPINew(
            nameBody,
            emailBody,
            countrycodeBody,
            mobileBody,
            passwordBody,
            genderBody,
            cityBody,
            stateBody,
            userTypeBody
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<SignUpResponseNew>> {
                override fun onSubscribe(d: Disposable) {
                    Toast.makeText(context, d.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onComplete() {
                    Toast.makeText(context, "complete", Toast.LENGTH_SHORT).show()
                }

                override fun onNext(response: Response<SignUpResponseNew>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "Succcess", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            })
    }


    var stateList = arrayListOf<String>()
    fun spinnerGenderData(): ArrayList<String> {
        var arr = ArrayList<String>()

        //arr.add("Gender")
        arr.add("Male")
        arr.add("Female")
        return arr
    }

    fun getStates(): Set<String> {
        return getValue()?.data?.keys ?: setOf()
    }

    fun getCitiesByState(state: String): List<String> {
        return getValue()?.data?.let {
            it[state]
        } ?: listOf()
    }

    private fun getValue(): States? {
        val inputStream = context?.resources?.openRawResource(R.raw.states_and_cities)
        val jsonString = inputStream?.bufferedReader().use { it?.readText() }
        val gson = Gson()
        return gson.fromJson(jsonString, States::class.java)
    }

    private fun setStatesSpinnerAdapter() {
        val states = getStates()
        ArrayAdapter(
            baseActivity!!, android.R.layout.simple_spinner_item, states.toTypedArray()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStates.adapter = adapter

            spinnerStates.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        setCitySpinnerAdapter()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
        }
    }

    private fun setCitySpinnerAdapter() {
        val state = spinnerStates.selectedItem.toString()
        val cities = getCitiesByState(state)
        val adapter = ArrayAdapter(
            baseActivity!!, android.R.layout.simple_spinner_item, cities.toTypedArray()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapter

    }
}