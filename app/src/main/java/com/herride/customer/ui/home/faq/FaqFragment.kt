package com.herride.customer.ui.home.faq

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.herride.customer.R
import com.herride.customer.common.Constants
import com.herride.customer.ui.base.BaseFragment
import com.herride.customer.ui.home.faq.adapter.FaqListAdapter
import com.herride.customer.ui.home.faq.response.FaqListApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_order_place.frNotification
import kotlinx.android.synthetic.main.fragment_order_place.imgNotification
import kotlinx.android.synthetic.main.fragment_order_place.tvNotificationCount
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FaqFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FaqFragment().apply {
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
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    var faqListAdapter: FaqListAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiliase()

    }

    private fun initiliase() {
        faqListAdapter = FaqListAdapter(arrayListOf(), repo)
        recyclerFaqList.adapter = faqListAdapter

        getCardAPICall(true)
        setToolbar()
    }


    private fun setToolbar() {

        imgIconBack.setImageDrawable(
                ContextCompat.getDrawable(
                        baseActivity!!,
                        R.drawable.ic_arrow_left
                )
        )
        frNotification.visibility = View.GONE
        tvNotificationCount.visibility = View.GONE
        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_log_out))

        imgIconBack.setOnClickListener { baseActivity!!.onBackPressed() }
        imgNotification.setOnClickListener {


        }

        tvTitle.text = "FAQ"

    }

    var faqList = arrayListOf<FaqListApiResponse.Payload>()
    fun getCardAPICall(isLoading: Boolean) {
        //rvCardList
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }
        repo.api.getFaqAPI(repo.pref.languageCode, Constants.customer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<FaqListApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<FaqListApiResponse>) {
                        hideLoading()

                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        if (response.body()!!.payload.size != 0) {
                                            faqList.clear()
                                            faqList.addAll(response.body()!!.payload)
                                            faqListAdapter!!.refreshList(faqList)
                                            isListShow(true)
                                        } else {
                                            isListShow(false)
                                        }
                                    } else {
                                        isListShow(false)
                                    }

                                } else {
                                    isListShow(false)
                                }
                            } else {
                                isListShow(false)
                            }
                        } else {
                            isListShow(false)
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })

        faqListAdapter!!.setListener(object : FaqListAdapter.FaqSelection {
            override fun itemClick(position: Int, action: String) {
                faqId = faqList[position].helpCategoryId
                replaceFragment(FaqSubFragment.newInstance(faqId, ""), true, false)
            }
        })

    }

    var faqId = ""

    fun isListShow(isShow: Boolean) {
        if (isShow) {
            recyclerFaqList.visibility = View.VISIBLE
        } else {
            recyclerFaqList.visibility = View.GONE
        }

    }

}