package com.carty.riderapp.ui.home.driver_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.carty.riderapp.R
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.driver_details.response.DriverDetailApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_driver_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverDetailFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DriverDetailFragment().apply {
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
            driverId = "${it.getString(ARG_PARAM1)}"
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_detail, container, false)
    }

    var commentAdapter: DriverCommentAdapter? = null
    var vehicle_name = "none"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (arguments != null) {
            var bundle = arguments
            if (bundle!!.containsKey("vehicle_name")) {
                vehicle_name = "${bundle!!.getString("vehicle_name")}"
                tvVehicleDetails.text = "ST3751 - ${vehicle_name}"
            }
        }


        imgvBackDriverDetail.setOnClickListener {
            baseActivity!!.onBackPressed()
        }


        getDriverDetailAPICall(true)

    }

    /////---------------------------------------------------------------------------
    var driverId = ""
    var commentsList = arrayListOf<DriverDetailApiResponse.Payload.Comment>()
    fun getDriverDetailAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getDriverDetailAPI(repo.pref.languageCode, driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<DriverDetailApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<DriverDetailApiResponse>) {
                        hideLoading()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        var payload = response.body()!!.payload

                                        llDetailView.visibility = View.VISIBLE
                                        tvDriverName.text = "${payload.name}"
                                        tvVehicleDetails.text = "${payload.vehicleName} - ${payload.vehiclePlateNumber}"
                                        tvTripsCount.text = "${payload.totalTrips}"
                                        ratingbarDriverDetail.rating = payload.rating.toFloat()

                                        if (payload.experience.equals("0", ignoreCase = true)) {

                                            tvExperience.text = "<1"
                                        } else {
                                            tvExperience.text = "${payload.experience}"
                                        }
                                        GlobalMethods().loadImagesWithProgressbar(
                                                payload.profilePicture,
                                                imgvDriverDetail,
                                                R.drawable.ic_user,
                                                pbLoadingEditProfile,
                                                300,
                                                300
                                        )


                                        commentsList.addAll(payload.comments)

                                        Log.e("getDriverDetailApi","commentsList.size = ${Gson().toJson(commentsList)}")
                                        Log.e("getDriverDetailApi","commentsList.size = ${commentsList.size}")

                                        if (commentsList.size!=0){
                                            commentAdapter = DriverCommentAdapter(repo, commentsList)
                                            rvComments.adapter = commentAdapter
                                            //commentAdapter!!.updateList(commentsList)
                                        }else{
                                            tvComments.visibility = View.GONE
                                        }
                                    }else{
                                        llDetailView.visibility = View.GONE
                                    }
                                    //baseActivity!!.onBackPressed()
                                }else{
                                    llDetailView.visibility = View.GONE
                                }
                            }else{
                                llDetailView.visibility = View.GONE
                            }
                        } else {
                            llDetailView.visibility = View.GONE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        llDetailView.visibility = View.GONE
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


}