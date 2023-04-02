package com.carty.riderapp.ui.home.trips

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.notification.CancelledTripsAdapter
import com.carty.riderapp.ui.home.trips.response.TripApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cancelled_trip.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CancelledTripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CancelledTripFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CancelledTripFragment().apply {
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
        return inflater.inflate(R.layout.fragment_cancelled_trip, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    var page_no = 1
    var loading = false
    var tripsAdapter: CancelledTripsAdapter? = null
    private fun initView() {
        tripsAdapter = CancelledTripsAdapter(arrayListOf())
        rvCancelledTripsList.adapter = tripsAdapter

        tripsAdapter!!.setListener(object : CancelledTripsAdapter.TripListClick {
            override fun clickItem(pos: Int, action: String) {

                if (action.equals(Constants.dispute)) {

                }
            }
        })

        TripsListAPICall(true)
        rvCancelledTripsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) //check for scroll down
                {
                    var visibleItemCount = rvCancelledTripsList.layoutManager!!.childCount
                    var totalItemCount = rvCancelledTripsList.layoutManager!!.itemCount
                    var pastVisibleItems =
                            (rvCancelledTripsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
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
                Constants.trip_canceled,
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
                                        if (page_no == 1){
                                            arrayList.clear()
                                        }
                                        arrayList.addAll(payload)

                                        loading = arrayList.size > 0

                                        tripsAdapter!!.updateList(arrayList)

                                        if (arrayList.size == 0) {
                                            tvNoCancelledTripLBL.visibility = View.VISIBLE
                                        }

                                    }else{
                                        tvNoCancelledTripLBL.visibility = View.VISIBLE
                                    }
                                    //baseActivity!!.onBackPressed()
                                }else{
                                    tvNoCancelledTripLBL.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            tvNoCancelledTripLBL.visibility = View.VISIBLE
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        tvNoCancelledTripLBL.visibility = View.VISIBLE
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })

    }
}