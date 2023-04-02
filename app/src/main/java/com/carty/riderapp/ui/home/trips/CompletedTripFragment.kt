package com.carty.riderapp.ui.home.trips

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.notification.TripsAdapter
import com.carty.riderapp.ui.home.response.ReceiptApiResponse
import com.carty.riderapp.ui.home.trips.response.TripApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_completed_trip.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompletedTripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompletedTripFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CompletedTripFragment().apply {
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
        return inflater.inflate(R.layout.fragment_completed_trip, container, false)
    }

    var tripsAdapter: TripsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    var page_no = 1
    var loading = false
    private fun initView() {
        tripsAdapter = TripsAdapter(arrayListOf())
        rvTripsList.adapter = tripsAdapter

        tripsAdapter!!.setListener(object : TripsAdapter.TripListClick {
            override fun clickItem(pos: Int, action: String) {

                if (action.equals(Constants.dispute)) {
                    dialogDisputes()
                }
//                else {
//
//                    if (comrFrom.equals("ForRateFragment", ignoreCase = true)) {
//
////                   /* var tripList = RateFragment()
////                    //var tripList = RateTheRideFragment()
////                    var bundle = Bundle()
////                    bundle.putString("comeFrom", comrFrom)
////                    tripList.arguments = bundle
////                    replaceFragment(tripList, true, false)
////                    //replaceFragment(RateTheRideFragment(), true, false)*/
//
//                        dialogRating()
//
//                    } else {
//                        //tvTitle.text = "My Trips"
//                    }
//                }

            }
        })

        TripsListAPICall(true)
        rvTripsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) //check for scroll down
                {
                    var visibleItemCount = rvTripsList.layoutManager!!.childCount
                    var totalItemCount = rvTripsList.layoutManager!!.itemCount
                    var pastVisibleItems =
                            (rvTripsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            page_no = page_no!! + 1
                            TripsListAPICall(true)
                        }
                    }
                }
            }
        })


    }

    var dialogRating: Dialog? = null
    fun dialogRating() {
        dialogRating = Dialog(baseActivity!!)
        dialogRating!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRating!!.setCancelable(false)
        dialogRating!!.setContentView(R.layout.dialog_rate_view)
        dialogRating!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogRating!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (!baseActivity!!.isFinishing) {
            dialogRating!!.show()
        }

        var tvSubmit = dialogRating!!.findViewById<TextView>(R.id.tvSubmit)



        tvSubmit.setOnClickListener {
            dialogRating!!.dismiss()
            // replaceFragment(HomeFragment(), false, true)
        }


    }


    var dialogDispute: Dialog? = null
    fun dialogDisputes() {
        dialogDispute = Dialog(baseActivity!!)
        dialogDispute!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDispute!!.setCancelable(true)
        dialogDispute!!.setContentView(R.layout.dialog_dispute_view)
        dialogDispute!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogDispute!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (!baseActivity!!.isFinishing) {
            dialogDispute!!.show()
        }

        var tvSubmit = dialogDispute!!.findViewById<TextView>(R.id.tvSubmitDispute)
        var edtReview = dialogDispute!!.findViewById<EditText>(R.id.edtReview)



        tvSubmit.setOnClickListener {

            dispute_msg = edtReview.text.toString()
            if (dispute_msg.isNullOrEmpty()) {
                edtReview.requestFocus()
                msgDialog("Please enter dispute message")
            } else {

                updateDisputeAPICall(true)
            }


            // replaceFragment(HomeFragment(), false, true)
        }
    }

    var dialogDisputeMSG: Dialog? = null
    fun dialogDisputeMSGS() {
        dialogDisputeMSG = Dialog(baseActivity!!)
        dialogDisputeMSG!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDisputeMSG!!.setCancelable(true)
        dialogDisputeMSG!!.setContentView(R.layout.dialog_dispute_msg_view)
        dialogDisputeMSG!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialogDisputeMSG!!.window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
        if (!baseActivity!!.isFinishing) {
            dialogDisputeMSG!!.show()
        }

        var imgvCloseMsg = dialogDisputeMSG!!.findViewById<ImageView>(R.id.imgvCloseMsg)
        imgvCloseMsg.setOnClickListener {
            dialogDisputeMSG!!.dismiss()
            // replaceFragment(HomeFragment(), false, true)
        }
    }


    /////---------------------------------------------------------------------------
    var rating = ""
    var comment = ""
    var tripId = ""
    var dispute_msg = ""
    fun updateDisputeAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.updateDisputeAPI(
                repo.pref.languageCode,
                repo.pref.USER_ID,
                tripId,
                dispute_msg
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
                                dialogDispute!!.dismiss()
                                dialogDisputeMSGS()
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
    /////---------------------------------------------------------------------------

    var arrayList = arrayListOf<TripApiResponse.Payload>()
    fun TripsListAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getTripsAPI(
                repo.pref.languageCode,
                "${page_no}",
                Constants.customer,
                Constants.trip_completed,
                repo.pref.USER_ID
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<TripApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<TripApiResponse>) {
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

                                        tripsAdapter!!.updateList(arrayList)

                                        if (arrayList.size == 0) {
                                            tvNoTripLBL.visibility = View.VISIBLE
                                        }

                                    }else{
                                        tvNoTripLBL.visibility = View.VISIBLE
                                    }
                                    //baseActivity!!.onBackPressed()
                                }else{
                                    tvNoTripLBL.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            tvNoTripLBL.visibility = View.VISIBLE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        tvNoTripLBL.visibility = View.VISIBLE
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })

    }

}