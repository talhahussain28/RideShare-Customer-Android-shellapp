package com.carty.riderapp.ui.home.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseFragment
import com.carty.riderapp.ui.home.notification.response.NotificationApiResponse
import com.carty.riderapp.ui.home.notification.response.NotificationDeleteApiResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.normal_toolbar.*
import retrofit2.Response

class NotificationFragment : BaseFragment() {

    lateinit var notificationAdapter: NotificationAdapter
    var notificationModel = ArrayList<NotificationModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onResume() {
        super.onResume()
        frNotification.visibility = View.GONE
        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }
        imgIconBack.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_arrow_left))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setData()
        initView()
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
        imgNotification.setImageDrawable(ContextCompat.getDrawable(baseActivity!!, R.drawable.ic_delete))

        imgIconBack.setOnClickListener { baseActivity!!.onBackPressed() }
        imgNotification.setOnClickListener { dialogDeleteNotification() }

        tvTitle.text = "Notifications"

    }


    private fun dialogDeleteNotification() {
        val builder = AlertDialog.Builder(baseActivity!!)
        builder.setTitle("Delete")
//        builder.setMessage("Are you sure you want delete all notifications?")
        builder.setMessage("Are you sure you want to delete all your notifications?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            deleteNotificationsAPICall(true)
        }

        builder.setNegativeButton("No") { dialog, which ->

        }

        builder.show()
    }


    private fun initView() {
        notificationAdapter = NotificationAdapter(arrayListOf())
        rvNotificationList.adapter = notificationAdapter
        getNotificationListAPICall(true)
    }

    /////---------------------------------------------------------------------------

    var notificationList = arrayListOf<NotificationApiResponse.Payload>()
    fun getNotificationListAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.getNotificationListAPI(repo.pref.languageCode, repo.pref.USER_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<NotificationApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<NotificationApiResponse>) {
                        hideLoading()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    if (response.body()!!.payload != null) {
                                        var payload = response.body()!!.payload
                                        notificationList.clear()
                                        notificationList.addAll(payload)
                                        if (notificationList.size == 0) {
                                            ifRecyclerVisible(false)
                                        } else {
                                            ifRecyclerVisible(true)
                                        }
                                        notificationAdapter.updateList(notificationList)
                                    }else{
                                        ifRecyclerVisible(false)
                                    }
                                    //baseActivity!!.onBackPressed()
                                }else{
                                    ifRecyclerVisible(false)
                                }
                            }else{
                                ifRecyclerVisible(false)
                            }
                        } else {
                            ifRecyclerVisible(false)
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        ifRecyclerVisible(false)
                        hideLoading()
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }

    fun ifRecyclerVisible(isVisible: Boolean){
        if (isVisible){
            frNotification.visibility = View.VISIBLE
            imgNotification.visibility = View.VISIBLE
            rvNotificationList.visibility = View.VISIBLE
            tvNoNotificationLBL.visibility = View.GONE
        }else{
            frNotification.visibility = View.GONE
            imgNotification.visibility = View.GONE
            rvNotificationList.visibility = View.GONE
            tvNoNotificationLBL.visibility = View.VISIBLE
        }
    }

    /////---------------------------------------------------------------------------

    fun deleteNotificationsAPICall(isLoading: Boolean) {
        if (!baseActivity!!.isInternetAvailable(baseActivity!!.getString(R.string.INTERNET_CONNECTION_ISSUE))) {
            return
        }

        repo.api.deleteNotificationListAPI(repo.pref.languageCode, repo.pref.USER_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<NotificationDeleteApiResponse>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        if (isLoading) {
                            showLoading()
                        }
                    }

                    override fun onNext(response: Response<NotificationDeleteApiResponse>) {
                        // hideLoading()
                        if (response.isSuccessful) {
                            if (response.code() == 200) {
                                getNotificationListAPICall(false)
                            }
                        } else {
                            baseActivity!!.showApiResponseERROR(response.errorBody())
                        }

                        //replaceFragment(PhoneVerifyFragment(), true, false, R.id.spalch_container)
                    }

                    override fun onError(e: Throwable) {
                        //hideLoading()
                        getNotificationListAPICall(false)
                        Log.e("loginApiCall", "onError = ${e.localizedMessage}")
                    }

                })
    }


    private fun setData() {
        notificationModel.clear()
        notificationModel.add(
                NotificationModel(
                        "29-SEP-2020",
                        "loren ipsum street loren ipsum street loren ipsum street loren ipsum street"
                )
        )
        notificationModel.add(
                NotificationModel(
                        "29-SEP-2020",
                        "loren ipsum street loren ipsum street loren ipsum street loren ipsum street"
                )
        )
        notificationModel.add(
                NotificationModel(
                        "29-SEP-2020",
                        "loren ipsum street loren ipsum street loren ipsum street loren ipsum street"
                )
        )
    }
}