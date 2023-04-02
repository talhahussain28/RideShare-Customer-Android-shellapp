package com.educator.customer.onesignalpush

import android.util.Log
import com.google.gson.Gson
import com.onesignal.OSNotification
import com.onesignal.OneSignal
import org.json.JSONObject
import java.lang.Exception

class OneSignalPushReceive : OneSignal.NotificationReceivedHandler {
    override fun notificationReceived(notification: OSNotification?) {

        val data: JSONObject? = notification!!.payload.additionalData
        var customKey: String?= null

        try {
            if (data != null) {
                Log.e(
                        "onesignal_push",
                        "  data additionalData==> " + Gson().toJson(notification.payload.additionalData)
                )
                Log.e("onesignal_push", "  data payload==>" + Gson().toJson(notification.payload))
                Log.e("onesignal_push", "  data notification==>" + Gson().toJson(notification))
                Log.e("onesignal_push", "  data data==>" + Gson().toJson(data))
                customKey = data.optString("customkey", null)
                if (customKey != null) Log.e(
                        "onesignal_push",
                        "customkey set with value: $customKey")

            }
        } catch (e: Exception) {
            Log.e(
                    "onesignal_push",
                    "  data additionalData Exception==> " + e.localizedMessage
            )
        }

    };
}
