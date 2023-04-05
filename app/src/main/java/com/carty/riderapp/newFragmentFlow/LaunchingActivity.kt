package com.carty.riderapp.newFragmentFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carty.riderapp.R
import com.carty.riderapp.ui.base.BaseActivity
import com.carty.riderapp.ui.signin_up.SplashFragment

class LaunchingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launching)
        replaceFragment(SplashFragmentNewFlow(), false, true, R.id.login_container)
    }
}