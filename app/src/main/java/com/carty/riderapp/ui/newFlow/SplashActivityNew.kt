package com.carty.riderapp.ui.newFlow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.carty.riderapp.R
import com.carty.riderapp.newFragmentFlow.MapFragmentNew
import com.carty.riderapp.ui.LoginActivity
import com.carty.riderapp.ui.base.BaseActivity
import com.carty.riderapp.ui.signin_up.SplashFragment


class SplashActivityNew : BaseActivity() {
    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_new)

        handler = Handler()
        handler!!.postDelayed(Runnable {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
           /* if (repo.pref.isUserLogin) {
                replaceFragment(MapFragmentNew(), false, true)

            }
            else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
*/
        }, 3000)
    }
}