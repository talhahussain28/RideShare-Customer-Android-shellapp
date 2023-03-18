package com.herride.customer.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.herride.customer.R
import com.herride.customer.common.Constants
import com.herride.customer.common.GlobalMethods
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.response.ReceiptApiResponse
import com.mikhaellopez.circularimageview.CircularImageView
import com.willy.ratingbar.ScaleRatingBar
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_receipt.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [ReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceiptFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var tripId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            tripId = "${it.getString(ARG_PARAM1)}"
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
                ReceiptFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                        putString(ARG_PARAM3, param3)
                    }
                }
    }

    var tvTotalFare: TextView? = null
    var tvTotalFareTwo: TextView? = null
    var tvBaseFare: TextView? = null
    var tvDistance: TextView? = null
    var tvPerMin: TextView? = null
    var tvDistanceLBL: TextView? = null
    var tvPerMinLBL: TextView? = null
    var tvBookingId: TextView? = null
    var tvRideDate: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotalFare = view.findViewById(R.id.tvTotalFare)
        tvTotalFareTwo = view.findViewById(R.id.tvTotalFareTwo)
        tvBaseFare = view.findViewById(R.id.tvBaseFare)
        tvDistance = view.findViewById(R.id.tvDistance)
        tvPerMin = view.findViewById(R.id.tvPerMin)
        tvDistanceLBL = view.findViewById(R.id.tvDistanceLBL)
        tvPerMinLBL = view.findViewById(R.id.tvPerMinLBL)
        tvBookingId = view.findViewById(R.id.tvBookingId)
        tvRideDate = view.findViewById(R.id.tvRideDate)

        getReceiptAPICall(true)
        imgIconHomeReceipt.setOnClickListener {

            replaceFragment(HomeFragment(), false, true)

        }
        tvSubmitReceipt.setOnClickListener {

            //replaceFragment(RateFragment(), false, true)
            dialogRating()
        }
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

        var tvSubmit = dialogRating!!.findViewById<TextView>(R.id.tvSubmitRate)
        var tvUserName = dialogRating!!.findViewById<TextView>(R.id.tvUserNameRate)
        var imgUserProfile = dialogRating!!.findViewById<CircularImageView>(R.id.imgUserProfileRate)
        var ratingbarCategory = dialogRating!!.findViewById<ScaleRatingBar>(R.id.ratingbarCategory)
        var edtReview = dialogRating!!.findViewById<EditText>(R.id.edtReview)
        tvUserName.text = param2

        GlobalMethods().loadImagesWithProgressbar(
                param3,
                imgUserProfile,
                R.drawable.ic_user,
                pbLoadingEditProfile,
                300,
                300
        )

        tvSubmit.setOnClickListener {
            dialogRating!!.dismiss()

            rating = "${ratingbarCategory.rating}"
            comment = "${edtReview.text.toString()}"

            updateRateAPICall(true)
//            replaceFragment(HomeFragment(), false, true)
        }


    }

    /////---------------------------------------------------------------------------
    fun getReceiptAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getReceiptJobAPI(repo.pref.languageCode, tripId)
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
                                        tvTotalFare
                                        tvTotalFare!!.text = "${Constants.currency_symbol}${String.format("%.2f", payload.total)}"
                                        tvTotalFareTwo!!.text = "${Constants.currency_symbol}${String.format("%.2f", payload.total)}"
                                        tvBaseFare!!.text = "${Constants.currency_symbol}${String.format("%.2f", payload.baseFare)}"
                                        tvDistance!!.text = "${Constants.currency_symbol}${String.format("%.2f", payload.distanceFare)}"
                                        tvPerMin!!.text = "${Constants.currency_symbol}${String.format("%.2f", payload.durationFare)}"

                                        tvDistanceLBL!!.text = "Distance(${payload.formattedDistance})"
                                        tvPerMinLBL!!.text = "Duration(${payload.formattedDuration})"
                                        tvBookingId!!.text = "${payload.tripId}"
                                        tvRideDate!!.text = "${payload.tripDate}"

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

    /////---------------------------------------------------------------------------
    var rating = ""
    var comment = ""
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
                                        replaceFragment(HomeFragment(), false, true)
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

}