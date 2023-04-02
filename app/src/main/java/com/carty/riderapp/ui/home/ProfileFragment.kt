package com.carty.riderapp.ui.home

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.carty.riderapp.R
import com.carty.riderapp.common.CommonMethods
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.rest.ApiService
import com.carty.riderapp.ui.MainActivity
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.order_place.response.PreferenceListApiResponse
import com.carty.riderapp.ui.home.response.GetProfileApiResponse
import com.carty.riderapp.ui.home.response.GetSettingApiResponse
import com.carty.riderapp.ui.signin_up.response.SignInApiResponse
import com.carty.riderapp.utils.ImagePicker
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.spinnerGender
import kotlinx.android.synthetic.main.normal_toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var picker: ImagePicker = ImagePicker(repo)

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onResume() {
        super.onResume()
        // baseActivity!!.setToolbar("Profile",true)
        setToolbar()
    }

    private fun setToolbar() {

        imgIconBack.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )
        frNotification.visibility = View.VISIBLE
        tvNotificationCount.visibility = View.GONE
        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_log_out))

        imgIconBack.setOnClickListener { baseActivity!!.onBackPressed() }
        imgNotification.setOnClickListener {

            dialogLogout()
            }


        tvTitle.text = "Profile"

    }

    var isProfilePickChanged = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileAPICall(true)

        ccpMobProfile.contentColor = ContextCompat.getColor(baseActivity!!, R.color.grayText)
        ccpMobProfile.setTextColor(R.color.grayText)
        ccpMobProfile.setDefaultCountryUsingNameCode("US")
        ccpMobProfile.setCcpClickable(false)

        edtMobielNumberProfile.addTextChangedListener(CommonMethods.onTextChangedListener(edtMobielNumberProfile));

        genterList = spinnerGenderData()

        var spinnerGenderAdapter = ArrayAdapter(baseActivity!!, R.layout.spinner_dropdown_item, genterList)
        spinnerGender.adapter = spinnerGenderAdapter

        tvGenderItemProfile.setOnClickListener {
            spinnerGender.performClick()
        }

        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                if (position > 0) {

                    val genderSelected = genterList[position]

/*                    if (genderSelected.equals("Male",ignoreCase = true))
                    {
                        tvGenderItemProfile.text = "Male"
                    }if (genderSelected.equals("Female",ignoreCase = true))
                    {
                        tvGenderItemProfile.text = "Female"
                    }else{
                        tvGenderItemProfile.text = genderSelected
                    }*/
                    tvGenderItemProfile.text = genderSelected
                    gender = genderSelected
//                        getStateListAPI(country.countryId)

                } else {
//                    resetState()
//                    resetCity()
                }
            }
        }

        ivProfileEdit.setOnClickListener {
            if (picker.isAdded) {
                return@setOnClickListener
            }
            picker.show(baseActivity!!.supportFragmentManager, "")
        }

        picker.setListener(object : ImagePicker.onUpdate {
            override fun set(imagePath: String?) {
                userPhoto = imagePath!!
                val file = File(imagePath!!)
                Log.e("file", "" + file)
                val compressFile = baseActivity!!.compressImage(file.absolutePath)
                Log.e("compressFile", compressFile)
                isProfilePickChanged = true
                GlobalMethods().loadImagesWithProgressbar(
                        imagePath,
                        imgvUserProfile,
                        R.drawable.ic_user,
                        pbLoadingEditProfile,
                        300,
                        300
                )


//                if (baseActivity!!.isInternetAvailable(
//                                repo.labelPref.getValue(PrefKeys.INFO_MSG_LANG_PLEASE_CHECK_INTERNET_CONNECTION)
//                        )
//                ) {
//                    uploadPhotoAPI(File(compressFile))
//                }
            }
        })
        tvUpdateProfileBTN.setOnClickListener {

            fullName = edtFullNameProfile.text.toString()
            email = edtEmailProfile.text.toString()
            mobileNumber = edtMobielNumberProfile.text.toString()
            mobileCountryCode = ccpMobProfile.selectedCountryCodeWithPlus

            mobileNumber = mobileNumber.replace("(", "")
            mobileNumber = mobileNumber.replace(")", "")
            mobileNumber = mobileNumber.replace(" ", "")
            mobileNumber = mobileNumber.replace("-", "")

            if (fullName.isNullOrEmpty()) {
                msgDialog("Please enter full name")
            } else if (email.isNullOrEmpty()) {
                msgDialog("Please enter email address")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                msgDialog("Please enter valid email address")
            }else if (mobileCountryCode.isNullOrEmpty()) {
                msgDialog("Please select mobile country code")
            } else if (mobileNumber.isNullOrEmpty()) {
                msgDialog("Please enter mobile number")
            } else if (mobileNumber.length < 10) {
                msgDialog("Please enter valid mobile number")
            } else if (gender.isNullOrEmpty() || gender.equals("Gender", ignoreCase = true)) {
                msgDialog("Please select gender")
            } else {
                if (isProfilePickChanged) {
                    selectedImageFile = File(userPhoto)
                    if (selectedImageFile == null) {
                        msgDialog("Please select profile image")
                    } else {
                        updateProfileApiCall()
                    }
                } else {
                    updateProfileApiCallWitouImage()
                }
            }


        }

    }


    fun logoutApiCall() {
        repo.api.logoutAPI(repo.pref.languageCode,Constants.logout,repo.pref.USER_ID,Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetSettingApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<GetSettingApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                baseActivity!!.logoutAccount()


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

    private fun dialogLogout() {
        val builder = AlertDialog.Builder(baseActivity!!)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to Logout?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            /*  Toast.makeText(baseActivity!!,
                      "Yes", Toast.LENGTH_SHORT).show()*/

            logoutApiCall()

           /* var languageCode = repo.pref.languageCode

            repo.pref.sharedPref.clearSharedPreference()

            repo.pref.languageCode = languageCode
            //replaceFragment(LoginFragment(), false, true)

            var intent = Intent(baseActivity!!, LoginActivity::class.java)
            startActivity(intent)
            baseActivity!!.finish()*/

        }

        builder.setNegativeButton("No") { dialog, which ->
            /*Toast.makeText(baseActivity!!,
                    "No", Toast.LENGTH_SHORT).show()*/
        }

//        builder.setNeutralButton("Maybe") { dialog, which ->
//            Toast.makeText(baseActivity!!,
//                    "Maybe", Toast.LENGTH_SHORT).show()
//        }
        builder.show()
    }

    var genterList = arrayListOf<String>()
    fun spinnerGenderData(): ArrayList<String> {
        var arr = ArrayList<String>()

        arr.add("Gender")
        arr.add("Male")
        arr.add("Female")
        return arr
    }


    /////////////////////////////////////////////////////////////////////////////////////////////

    var preferenceLists: PreferenceListApiResponse? = null
    fun getProfileAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getProfileAPI(repo.pref.languageCode, repo.pref.USER_ID, Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetProfileApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<GetProfileApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {

                                        edtFullNameProfile.setText(response.body()!!.payload.name)
                                        edtEmailProfile.setText(response.body()!!.payload.email)
                                        var tempNumber = response.body()!!.payload.mobile
                                        var convertedNumber = ""
                                        if (tempNumber.length >= 10) {
                                            convertedNumber = ""
                                            convertedNumber =
                                                    "(${tempNumber[0]}${tempNumber[1]}${tempNumber[2]}) ${tempNumber[3]}" +
                                                            "${tempNumber[4]}${tempNumber[5]}-${tempNumber[6]}${tempNumber[7]}${tempNumber[8]}" +
                                                            "${tempNumber[9]}"
//
                                        }
                                        edtMobielNumberProfile.setText(convertedNumber)

                                        repo.pref.mode_id = response.body()!!.payload.preferences.modeId
                                        repo.pref.music_id = response.body()!!.payload.preferences.musicId
                                        repo.pref.accessible_id = response.body()!!.payload.preferences.accessibleId
                                        if (!response.body()!!.payload.preferences.temperature.isNullOrEmpty()){
                                            repo.pref.temperature = response.body()!!.payload.preferences.temperature
                                        }else{
                                            repo.pref.temperature = "0"
                                        }

                                        gender = response.body()!!.payload.gender
                                        if (gender.equals("Male", ignoreCase = true)) {
                                            tvGenderItemProfile.text = "Male"
                                        } else {
                                            tvGenderItemProfile.text = "Female"
                                        }

                                        //tvGenderItemProfile.text = response.body()!!.payload.gender



                                        repo.pref.userName = (response.body()!!.payload.name)
                                        repo.pref.email = (response.body()!!.payload.email)
                                        repo.pref.mobile = (response.body()!!.payload.mobile)
                                        repo.pref.mobile_country_code = (response.body()!!.payload.mobileCountryCode)
                                        repo.pref.gender = (response.body()!!.payload.gender)


                                        if (!response.body()!!.payload.profilePicture.isNullOrEmpty()) {
                                            userPhoto = ""
                                            userPhoto = response.body()!!.payload.profilePicture
                                            repo.pref.profile_photo = userPhoto
                                            GlobalMethods().loadImagesWithProgressbar(
                                                    response.body()!!.payload.profilePicture,
                                                    imgvUserProfile,
                                                    R.drawable.ic_user,
                                                    pbLoadingEditProfile,
                                                    300,
                                                    300
                                            )

                                        }
                                        (activity as MainActivity)!!.updateProfile()
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
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }
    ///////////////////////////////////////////////////////////////////////////////////////

    var fullName = ""
    var mobileNumber = ""
    var mobileCountryCode = ""
    var email = ""
    var userPhoto = ""
    var gender = ""

    var selectedImageFile: File? = null
    fun updateProfileApiCall() {

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        /*var imageRequestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)*/

        var codeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.languageCode)
        var userIdBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.USER_ID)
        var nameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), fullName)
        var emailBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
//        var mobileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileNumber)
//        var countrycodeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobileCountryCode)
        var genderBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), gender)
        var userTypeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), Constants.customer)

//        selectedImageFile = File(userPhoto)
        var imageRequestBody: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedImageFile!!)
        var imageMultipart = MultipartBody.Part.createFormData(
                "${ApiService.parameters.profile_picture}",
                selectedImageFile!!.absolutePath,
                imageRequestBody
        )

        repo.api.profileAPI(codeBody, userIdBody, nameBody, emailBody, genderBody, userTypeBody, imageMultipart)
//        repo.api.emialMobileExistCheckAPI_(repo.pref.languageCode, fullName, email, mobileCountryCode, mobileNumber, Constants.customer)
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
                                    msgDialog("Profile updated successfully")
                                    getProfileAPICall(false)
                                }
                            }
                        } else {
                            hideLoading()

                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }
                        // replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    ////////////////////////////////////////////////////////////////////////////////////

    fun updateProfileApiCallWitouImage() {

        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        var ProfileMap = HashMap<String, String>()
        ProfileMap.put("code", "${repo.pref.languageCode}")
        ProfileMap.put("user_id", "${repo.pref.USER_ID}")
        ProfileMap.put("name", "${fullName}")
//        ProfileMap.put("mobile_country_code", "${mobileCountryCode}")
//        ProfileMap.put("mobile", "${mobileNumber}")
        ProfileMap.put("gender", "${gender}")
        ProfileMap.put("email", "${email}")
        ProfileMap.put("user_type", "${Constants.customer}")
//        ProfileMap.put("${ApiService.parameters.profile_picture}", "${userPhoto}")
//        ProfileMap.put("gender", "${gender}")

        repo.api.profileAPI(ProfileMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<GetProfileApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<GetProfileApiResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    msgDialog("Profile updated successfully")
                                    getProfileAPICall(false)
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

}