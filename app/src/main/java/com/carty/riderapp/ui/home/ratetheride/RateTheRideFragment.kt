package com.carty.riderapp.ui.home.ratetheride

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.notification.NotificationFragment
import com.carty.riderapp.ui.home.ratetheride.response.RateTheRideUnratedApiResponse
import com.carty.riderapp.ui.home.response.ReceiptApiResponse
import com.mikhaellopez.circularimageview.CircularImageView
import com.willy.ratingbar.ScaleRatingBar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_ratetheride.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

class RateTheRideFragment : BaseFragment() {
    lateinit var rateTheRideAdapter: RateTheRideAdapter
    var rateTheRideModel = ArrayList<RateTheRideModel>()

    var comrFrom = "none"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            var bundle = arguments
            //bundle.putString("comeFrom","MainActivity")
            if (bundle!!.containsKey("comeFrom")) {
                comrFrom = "${bundle!!.getString("comeFrom")}"
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ratetheride, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initView()

    }

    var page_no = 1
    var loading = false
    private fun initView() {
        rateTheRideAdapter = RateTheRideAdapter(arrayListOf())
        rvRatesList.adapter = rateTheRideAdapter
        rateTheRideAdapter.setClickListener(object : RateTheRideAdapter.OnSelectRateItem {
            override fun getRateItem(basketItemModel: RateTheRideUnratedApiResponse.Payload, position: Int) {
///*//                var tripList = RateTheRideFragment()
//                 var tripList = RateFragment()
//                var bundle = Bundle()
//                bundle.putString("comeFrom", comrFrom)
//                tripList.arguments = bundle
//                replaceFragment(tripList, false, false)
//                //replaceFragment(RateFragment(), true)*/
                tripId = arrayList[position].tripId
                driverPic = arrayList[position].profilePicture
                driverName = arrayList[position].name
                dialogRating()

            }
        })


        getTheUnratedTripAPICall(true)
        rvRatesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) //check for scroll down
                {
                    var visibleItemCount = rvRatesList.layoutManager!!.childCount
                    var totalItemCount = rvRatesList.layoutManager!!.itemCount
                    var pastVisibleItems =
                            (rvRatesList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            page_no = page_no!! + 1
                            getTheUnratedTripAPICall(true)
                        }
                    }
                }
            }
        })

    }


    override fun onResume() {
        super.onResume()
//        imgIconBack.setOnClickListener {
//            baseActivity!!.drawerOpen()
//        }
//        frNotification.setOnClickListener {
//            replaceFragment(NotificationFragment(), true)
//        }
//        tvTitle.text = "Rate The Ride"
        setToolbar()
    }

    private fun setToolbar() {

        imgIconBack.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )

        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }

        imgNotification.setOnClickListener {
            replaceFragment(NotificationFragment(), true, false)
        }

        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_notifications))
        tvTitle.text = "Rate The Ride"

    }

    /////---------------------------------------------------------------------------

    var arrayList = arrayListOf<RateTheRideUnratedApiResponse.Payload>()
    fun getTheUnratedTripAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getUnratedTripsAPI(
                repo.pref.languageCode,
                "${page_no}",
                repo.pref.USER_ID
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<RateTheRideUnratedApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<RateTheRideUnratedApiResponse>) {
                        hideLoading()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        var payload = response.body()!!.payload
                                        if (page_no == 1) {
                                            arrayList.clear()
                                        }
                                        arrayList.addAll(payload)

                                        loading = arrayList.size > 0

                                        rateTheRideAdapter.updateList(arrayList)

                                        if (arrayList.size != 0) {

                                        }else{
                                            tvNoTripForRateLBL.visibility = View.VISIBLE
                                        }

                                    }else{
                                        tvNoTripForRateLBL.visibility = View.VISIBLE
                                    }
                                    //baseActivity!!.onBackPressed()
                                }else{
                                    tvNoTripForRateLBL.visibility = View.VISIBLE
                                }
                            }else{
                                tvNoTripForRateLBL.visibility = View.VISIBLE
                            }
                        } else {
                            tvNoTripForRateLBL.visibility = View.VISIBLE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        tvNoTripForRateLBL.visibility = View.VISIBLE
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })

    }

    ////////////////////////////////////////////////////////////////////////////////////////
    var dialogRating: Dialog? = null
    var tvUserNameRate: TextView? = null
    var imgUserProfileRate: CircularImageView? = null

    var driverPic = ""
    var driverName = ""

    fun dialogRating() {
        dialogRating = Dialog(baseActivity!!)
        dialogRating!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRating!!.setCancelable(true)
        dialogRating!!.setContentView(R.layout.dialog_rate_view)
        dialogRating!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogRating!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (!baseActivity!!.isFinishing) {
            dialogRating!!.show()
        }

        var tvSubmit = dialogRating!!.findViewById<TextView>(R.id.tvSubmitRate)
        tvUserNameRate = dialogRating!!.findViewById<TextView>(R.id.tvUserNameRate)
        imgUserProfileRate = dialogRating!!.findViewById<CircularImageView>(R.id.imgUserProfileRate)
        var edtReview = dialogRating!!.findViewById<EditText>(R.id.edtReview)
        var ratingbarCategory = dialogRating!!.findViewById<ScaleRatingBar>(R.id.ratingbarCategory)

        tvUserNameRate!!.text = driverName

        GlobalMethods().loadImagesWithNoProgressbar(
                driverPic,
                imgUserProfileRate,
                R.drawable.ic_user,
                300,
                300
        )




        tvSubmit.setOnClickListener {
            rating = "${ratingbarCategory.rating}"
            comment = "${edtReview.text.toString()}"
            updateRateAPICall(true)
            // replaceFragment(HomeFragment(), false, true)
        }


    }

    /////---------------------------------------------------------------------------
    var rating = ""
    var comment = ""
    var tripId = ""

    fun updateRateAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.updateRateAPI(
                repo.pref.languageCode,
                Constants.customer,
                repo.pref.USER_ID,
                tripId,
                rating,
                comment
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<ReceiptApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<ReceiptApiResponse>) {
                        hideLoading()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        var payload = response.body()!!.payload
                                        dialogRating!!.dismiss()
                                        getTheUnratedTripAPICall(true)
                                        //replaceFragment(HomeFragment(), false, true)
                                    }
                                    //baseActivity!!.onBackPressed()
                                }
                            }
                        } else {
                            //baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    private fun setData() {
        rateTheRideModel.clear()
        rateTheRideModel.add(
                RateTheRideModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "19",
                        true
                )
        )
        rateTheRideModel.add(
                RateTheRideModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "Very Good",
                        false
                )
        )
        rateTheRideModel.add(
                RateTheRideModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "Good",
                        false
                )
        )
        rateTheRideModel.add(
                RateTheRideModel(
                        R.drawable.ic_profile_place_holder,
                        "35 Gordon St",
                        "1025 Carnieigh St",
                        "28 Aug,2020-10:20 am",
                        "Excellent",
                        false
                )
        )
    }
}