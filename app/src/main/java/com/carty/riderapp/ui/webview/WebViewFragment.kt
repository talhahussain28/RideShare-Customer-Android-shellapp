package com.carty.driver.ui.webview

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_web_view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebViewFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WebViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgIconBack.setOnClickListener {
            baseActivity!!.onBackPressed()
        }
        if (param1.equals(Constants.TERMS_CONDITIONS)) {
            tvTitle.text = "${getString(R.string.Terms_and_Condition)}"
            url = repo.pref.TERMS_CONDITIONS
        } else {
            url = repo.pref.PRIVACY_POLICY
            tvTitle.text = "${getString(R.string.Privacy_Policy)}"
        }
        loadWebView()

    }

    var url = ""

    private fun loadWebView() {
        //val title = arguments?.getString("title")
        // url = arguments?.getString("url")
//        if (title == null) return
//        setToolbar(title,true)
        webView.webViewClient = MyWebClient()
        //webView.loadUrl("www.google.com")
        webView.clearCache(true)
        webView.settings.javaScriptEnabled = true
        webView.getSettings().setBuiltInZoomControls(true)
        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        if (param1.equals(Constants.TERMS_CONDITIONS)) {
            webView.loadUrl(url!!)
        } else {

        }
        webView.setWebViewClient(object : WebViewClient() {

            override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
            ) {

                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                //_progressState.value = ProgressState.SHOW
                showLoading()
                //Utils.progressDialog(ConstantStore.KEY_Pleasewait, AboutUsActivity.this);
            }

            override fun onPageFinished(view: WebView, url: String) {
//                _progressState.value = ProgressState.HIDE
                hideLoading()
            }
        })

        //webView.loadUrl("file:///android_asset/test.html");
    }

    inner class MyWebClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            //showProgressDialog()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            // dismissProgressDialog()
        }

        override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            Log.e(tag, "onReceivedError $error")
        }

    }


}