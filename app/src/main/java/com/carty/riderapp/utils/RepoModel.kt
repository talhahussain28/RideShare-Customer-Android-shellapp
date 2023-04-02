package com.carty.riderapp.utils

import android.content.Context
import com.carty.riderapp.preference.LabelManager
import com.carty.riderapp.preference.SessionManager
import com.carty.riderapp.rest.ApiService
import org.koin.core.KoinComponent

class RepoModel(context: Context) : KoinComponent {

    var pref: SessionManager = SessionManager(context)
    var labelPref: LabelManager = LabelManager(context)
    var api = ApiService.create(pref)
    //var appDatabase = DatabaseRepository(AppRoomDatabase.getDatabase(context).dbDao())


}