package com.herride.customer.preference
//package com.servicely.customer.repository.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.herride.customer.common.Constants
import com.herride.customer.ui.home.setting.response.LanguageListApiResponse

class SharedPreference(mContext: Context) {

    private val prefMode: Int = 0

    private var sharedPref: SharedPreferences = mContext.getSharedPreferences(Constants.PREF_NAME, prefMode)


    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        editor.commit()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, "")
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueLong(KEY_NAME: String): Long {
        return sharedPref.getLong(KEY_NAME, 0)
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }

      val prefName: String = "la_la_laundry_pref"
      val labelPrefName: String = "label_la_la_laundry_pref"
    private var PRIVATE_MODE = 0

    private var langPref: SharedPreferences =
            mContext.getSharedPreferences( labelPrefName, PRIVATE_MODE)

    fun getLabel(key: String): String {
        return langPref.getString(key, "").toString()
    }


    @SuppressLint("CommitPrefEdits")
    fun initLabel(labelList: List<LanguageListApiResponse.Payload>) {

        val edit = langPref.edit()
//        for (label in labelList) {
//            Log.e(
//                    "languagelabel",
//                    "languagelabel = public static final String " + label.code + " = \"" + label.code + "\"" + ";"
//            )
//            edit.putString(label.code, label.value)
//        }

        edit.apply()
    }

}