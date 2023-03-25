package com.herride.customer.ui.home.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.herride.customer.R
import com.herride.customer.common.CommonMethods
import com.herride.customer.common.GlobalMethods
import com.herride.customer.rest.ApiService
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.setting.response.EmergencyContactApiResponse
import com.herride.customer.utils.ImagePicker
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_contact.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class AddContactFragment : BaseFragment() {

    var picker: ImagePicker = ImagePicker(repo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact, container, false)
    }

    var userPhoto = ""
    override fun onResume() {
        super.onResume()
        frNotification.visibility = View.GONE
        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }
        imgIconBack.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_arrow_left))
        tvTitle.text = "Add Contact"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ccpAddContact.contentColor = ContextCompat.getColor(baseActivity!!, R.color.black)
        ccpAddContact.setTextColor(R.color.black)

        edtMobielNumber.addTextChangedListener(CommonMethods.onTextChangedListener(edtMobielNumber))

        imgvContactImage.setOnClickListener {
            if (picker.isAdded) {
                return@setOnClickListener
            }
            picker.show(baseActivity!!.supportFragmentManager, "")
        }
        tvSave.setOnClickListener {
            //baseActivity!!.onBackPressed()
            name = edtFullName.text.toString()
            mobile_country_code = ccpAddContact.selectedCountryCodeWithPlus
            mobile = edtMobielNumber.text.toString()

            mobile = mobile.replace("(", "")
            mobile = mobile.replace(")", "")
            mobile = mobile.replace(" ", "")
            mobile = mobile.replace("-", "")

            if (userPhoto.isNullOrEmpty()) {
                msgDialog("Please select contact photo")
            } else if (name.isNullOrEmpty()) {
                msgDialog("Please enter full name")
            } else if (mobile_country_code.isNullOrEmpty()) {
                msgDialog("Please select mobile country code")
            } else if (mobile.isNullOrEmpty()) {
                msgDialog("Please enter mobile number")
            } else if (mobile.length < 10) {
                msgDialog("Please enter valid mobile number")
            } else {
                addContactAPICall()
            }
        }

        picker.setListener(object : ImagePicker.onUpdate {
            override fun set(imagePath: String?) {
                userPhoto = imagePath!!
                val file = File(imagePath!!)
                Log.e("file", "" + file)
                val compressFile = baseActivity!!.compressImage(file.absolutePath)
                Log.e("compressFile", compressFile)
                //isProfilePickChanged = true
                GlobalMethods().loadImagesWithProgressbar(
                        imagePath,
                        imgvContactPic,
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
    }

    var name = ""
    var mobile_country_code = ""
    var mobile = ""
    fun addContactAPICall() {

        if (!isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

///*        var contactMap = HashMap<String, String>()
//        contactMap.put(ApiService.parameters.code, "${repo.pref.languageCode}")
//        contactMap.put(ApiService.parameters.customer_id, "${repo.pref.USER_ID}")
//        contactMap.put(ApiService.parameters.name, "${name}")
//        contactMap.put(ApiService.parameters.mobile_country_code, "${mobile_country_code}")
//        contactMap.put(ApiService.parameters.mobile, "${mobile}")*/

        var codeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.languageCode)
        var userIdBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "" + repo.pref.USER_ID)
        var nameBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
        var mobileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobile)
        var countrycodeBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), mobile_country_code)
        var selectedImageFile = File(userPhoto!!)
        var imageRequestBody: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), selectedImageFile!!)
        var imageMultipart = MultipartBody.Part.createFormData(
                "${ApiService.parameters.profile_picture}",
                selectedImageFile!!.absolutePath,
                imageRequestBody
        )



        repo.api.addEmergencyContactAPI(codeBody, userIdBody, nameBody, mobileBody, countrycodeBody, imageMultipart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<EmergencyContactApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        showLoading()
                    }

                    override fun onNext(response: Response<EmergencyContactApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {

                                    // msgDialog("Address added successfully")
                                    baseActivity!!.onBackPressed()

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