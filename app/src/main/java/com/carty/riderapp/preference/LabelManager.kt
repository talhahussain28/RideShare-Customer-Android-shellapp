package com.carty.riderapp.preference
//package com.servicely.customer.repository.preference

import android.content.Context

/**
 * This class will help to read/write data into preference.
 */
class LabelManager(context: Context) {

    private val sharedPref: LabelPreference = LabelPreference(context)

    fun setValue(key: String, value: String) {
        sharedPref.saveStringLabel(key, value)
    }

    fun getValue(key: String): String {
        return sharedPref.getStringLabel(key)
    }

    fun clearAllPref(): Boolean {
        return sharedPref.clearSharedPreference()
    }
}