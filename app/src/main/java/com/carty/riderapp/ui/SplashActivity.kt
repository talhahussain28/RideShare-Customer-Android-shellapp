package com.carty.riderapp.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseActivity
import com.carty.riderapp.utils.RepoModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SpalshFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SplashActivity : BaseActivity() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var llGetStarted: RelativeLayout? = null
    var tvSignIn: TextView? = null
    var tvSignUp: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        repo.pref.languageCode = "EN"

        llGetStarted = findViewById(R.id.llGetStarted)
        tvSignIn = findViewById(R.id.tvSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)


        isnetAvailable()

    }

    private fun initiliase() {

        Handler().postDelayed({

            if (repo.pref.isUserLogin) {
                var intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
//                val animFadeOut: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
//                llSplash.startAnimation(animFadeOut)
//                llSplash.visibility = View.GONE
//            val animFadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
//            llGetStarted.startAnimation(animFadeIn)
               if (llGetStarted!=null)
               {
                   llGetStarted!!.visibility = View.VISIBLE
                   tvSignIn!!.isEnabled = true
                   tvSignUp!!.isEnabled = true
               }
            }
        }, 2000)

        tvSignIn!!.setOnClickListener {
            //replaceFragment(LoginFragment(), true, false, R.id.spalch_container)
            /*var intent = Intent(this@SplashActivity, LoginActivity::class.java)
            intent.putExtra("actionType","SignIn")
            startActivity(intent)
            finish()*/
        }

        tvSignUp!!.setOnClickListener {
            //replaceFragment(LoginFragment(), true, false, R.id.spalch_container)
          /*  var intent = Intent(this@SplashActivity, LoginActivity::class.java)
            intent.putExtra("actionType","SignUp")
            startActivity(intent)
            finish()*/
        }

    }

    fun isnetAvailable(): Boolean {

        val connectivityManager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        var status = false
        status = activeNetwork?.isConnectedOrConnecting == true

        if (!status) {
            //Toast.makeText(ctx,"No Internet Connection",Toast.LENGTH_SHORT).show();
            DialogInternetError(this, getString(R.string.INTERNET_CONNECTION_ISSUE))
        } else {
            initiliase()
        }

        return status
    }

    var repoModel: RepoModel? = null
    var errorDialog: Dialog? = null
    fun DialogInternetError(context: Context, msg: String?) {
        if (errorDialog != null && errorDialog!!.isShowing) {
            errorDialog!!.dismiss()
        }
        repoModel = RepoModel(context)
        errorDialog = android.app.Dialog(context)
        errorDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        errorDialog!!.setContentView(R.layout.dialog_ok)
        errorDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        errorDialog!!.setCancelable(false)

        val txt_dialog_msg = errorDialog!!.findViewById<TextView>(R.id.tvMsgInfo)
        val txt_dialog_ok = errorDialog!!.findViewById<TextView>(R.id.btOk)
        txt_dialog_ok.visibility = View.VISIBLE
        txt_dialog_msg.text = msg
        //txt_dialog_ok.setText(repoModel.getLabelPref().getValue(PrefKeys.BTN_DIALOG_OK));

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
        txt_dialog_ok.setOnClickListener {
            errorDialog!!.dismiss()
            isnetAvailable()
        }
        val window = errorDialog!!.window
        val wlp = window!!.attributes
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wlp.windowAnimations = R.style.MaterialDialogSheet
        wlp.gravity = Gravity.TOP
        window.attributes = wlp
        if (!(context as AppCompatActivity).isFinishing) errorDialog!!.show()
    }


//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                SplashFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_spalsh, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        Handler().postDelayed({
//
//            val animFadeOut: Animation = AnimationUtils.loadAnimation(baseActivity!!, R.anim.fade_out)
//            llSplash.startAnimation(animFadeOut)
//            llSplash.visibility = View.GONE
//            val animFadeIn: Animation = AnimationUtils.loadAnimation(baseActivity!!, R.anim.fade_in)
//            llGetStarted.startAnimation(animFadeIn)
//            llGetStarted.visibility = View.VISIBLE
//
//
//        }, 2000)
//
//
//        rlBottom.setOnClickListener {
//            replaceFragment(LoginFragment(), true, false)
//        }
//
//    }


}


/////////////////////////////////////////////////////////////////////////////////////


//fun isnetAvailable(): Boolean {
//
//    val connectivityManager =
//            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//    var status = false
//    status = activeNetwork?.isConnectedOrConnecting == true
//
//    if (!status) {
//        //Toast.makeText(ctx,"No Internet Connection",Toast.LENGTH_SHORT).show();
//        DialogInternetError(this, getString(R.string.INTERNET_CONNECTION_ISSUE))
//    } else {
//        initiliase()
//    }
//
//    return status
//}
//
//var repoModel: RepoModel? = null
//var errorDialog: Dialog? = null
//fun DialogInternetError(context: Context, msg: String?) {
//    if (errorDialog != null && errorDialog!!.isShowing) {
//        errorDialog!!.dismiss()
//    }
//    repoModel = RepoModel(context)
//    errorDialog = android.app.Dialog(context)
//    errorDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//    errorDialog!!.setContentView(R.layout.dialog_message)
//    errorDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
//    errorDialog!!.setCancelable(false)
//
//    val txt_dialog_msg = errorDialog!!.findViewById<TextView>(R.id.txt_dialog_msg)
//    val txt_dialog_ok = errorDialog!!.findViewById<TextView>(R.id.txt_dialog_ok)
//    txt_dialog_msg.text = msg
//    //txt_dialog_ok.setText(repoModel.getLabelPref().getValue(PrefKeys.BTN_DIALOG_OK));
//
////        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
////        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
//    txt_dialog_ok.setOnClickListener {
//        errorDialog!!.dismiss()
//        isnetAvailable()
//    }
//    val window = errorDialog!!.window
//    val wlp = window!!.attributes
//    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    wlp.windowAnimations = R.style.DialogAnimation
//    wlp.gravity = Gravity.CENTER
//    window.attributes = wlp
//    if (!(context as AppCompatActivity).isFinishing) errorDialog!!.show()
//}