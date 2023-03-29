package com.herride.customer.ui

import android.os.Bundle
import com.herride.customer.R
import com.herride.customer.newFragmentFlow.RegisterFragment2
import com.herride.customer.ui.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        replaceFragment(RegisterFragment2(), false, false, R.id.login_container)


      /*  var arg = intent.extras
        if (arg != null) {
            if (arg!!.containsKey("actionType")) {
                var action = arg!!.getString("actionType")
                if (action.equals("SignIn", ignoreCase = true)) {
                    replaceFragment(LoginFragment(), false, false, R.id.login_container)
                }
                if (action.equals("SignUp", ignoreCase = true)) {
                    replaceFragment(RegisterFragment(), false, false, R.id.login_container)
                }
            } else {
                replaceFragment(LoginFragment(), false, true, R.id.login_container)
            }
        } else {
            replaceFragment(LoginFragment(), false, true, R.id.login_container)
        }*/


    }
}