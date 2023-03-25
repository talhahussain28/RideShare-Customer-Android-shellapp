package com.herride.customer.utils

import android.content.Context
import com.herride.customer.preference.LabelManager
import com.herride.customer.preference.SessionManager
import com.herride.customer.rest.ApiService
import org.koin.core.KoinComponent

class RepoModel(context: Context) : KoinComponent {

    var pref: SessionManager = SessionManager(context)
    var labelPref: LabelManager = LabelManager(context)
    var api = ApiService.create(pref)
    //var appDatabase = DatabaseRepository(AppRoomDatabase.getDatabase(context).dbDao())


}