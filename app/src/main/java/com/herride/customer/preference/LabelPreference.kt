package com.herride.customer.preference
//package com.servicely.customer.repository.preference

import android.content.Context
import android.content.SharedPreferences

import com.herride.customer.common.Constants

class LabelPreference(mContext: Context) {

    private val prefMode: Int = 0

    private var sharedPref: SharedPreferences =
        mContext.getSharedPreferences(Constants.LABEL_PREFEREANCE_NAME, prefMode)

    fun saveStringLabel(KEY_NAME: String, value: String) {
        sharedPref.edit().putString(KEY_NAME, value).apply()
    }

    fun getStringLabel(KEY_NAME: String): String {
        return sharedPref.getString(KEY_NAME, "")!!
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
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

    fun clearSharedPreference(): Boolean {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        return editor.commit()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }

}