package com.educator.customer.onesignalpush

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.herride.customer.ui.MainActivity
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal

class OneSignalPushClick(var context: Context) : OneSignal.NotificationOpenedHandler {
    override fun notificationOpened(result: OSNotificationOpenResult?) {

        val data = result!!.notification.payload.additionalData
        val intent = Intent(context, MainActivity::class.java)

        if (result != null) {
            Log.e("onesignal_push", "OneSignalPushClick - resultnotification==" + Gson().toJson(result))
            if (data != null) {
                Log.e("onesignal_push", "OneSignalPushClick - resultnotificationdata==" + Gson().toJson(data))
                var newsID = data.optString("ed_news_id")
                if (data.has("news_id")) {
                    newsID = data.optString("news_id")
                }
                var type = data.optString("type")
                var ed_advertisement_id = data.optString("ed_advertisement_id")
                var ed_advertisement_category_id = data.optString("ed_advertisement_category_id")
                var ed_ticket_id = data.optString("ed_ticket_id")
                var id = data.optString("id")
                intent.putExtra("type", type)
                intent.putExtra("ed_news_id", newsID)
                intent.putExtra("ed_advertisement_id", ed_advertisement_id)
                intent.putExtra("ed_advertisement_category_id", ed_advertisement_category_id)
                intent.putExtra("ed_ticket_id", ed_ticket_id)
                intent.putExtra("id", id)
                intent.putExtra("fromWhere", "push")
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context!!.startActivity(intent)

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
    <application ...>
      <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
    </application>
 */
    }
}