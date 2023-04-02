package com.carty.riderapp

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.educator.customer.onesignalpush.OneSignalPushClick
import com.educator.customer.onesignalpush.OneSignalPushReceive
import com.google.firebase.FirebaseApp
import com.carty.riderapp.utils.RepoModel
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Created by AhmedEltaher
 */

open class App : MultiDexApplication() , OSSubscriptionObserver {

//    @Inject
//    lateinit var androidInjector: DispatchingAndroidInjector<Any>
//
//    override fun androidInjector(): AndroidInjector<Any> = androidInjector
var _appContext: Context? = null
    private val ONESIGNAL_APP_ID = "6d0d3f74-34e7-4516-9576-888f00eb43be"
    override fun onCreate() {
        super.onCreate()
        _appContext = this
        context = applicationContext
        FirebaseApp.initializeApp(this)
        val myModules = module {
            single { RepoModel(this@App) }
        }

       // OneSignal.addSubscriptionObserver(this);

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.NONE);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(OneSignalPushReceive())
                .setNotificationOpenedHandler(OneSignalPushClick(this))
                .init()

        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()
            modules(myModules)
        }
     //   initDagger()
    }

//    open fun initDagger() {
//        DaggerAppComponent.builder().build().inject(this)
//    }

    companion object {
        lateinit var context: Context
    }

    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {
        Log.e(
                "one_signal_player",
                "onOSSubscriptionChanged"
        )
        if (!stateChanges!!.getFrom().getSubscribed() &&
                stateChanges!!.getTo().getSubscribed()) {
            /*AlertDialog.Builder(this)
                    .setMessage("You've successfully subscribed to push notifications!")
                    .show()*/
            // get player ID
            stateChanges.to.userId
            Log.e(
                    "one_signal_player",
                    "Debug - player_id - onOSPermissionChanged==>$stateChanges"
            )
        }
    }
}
