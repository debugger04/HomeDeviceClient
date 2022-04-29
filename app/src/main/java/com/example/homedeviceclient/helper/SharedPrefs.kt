package com.example.homedeviceclient.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(activity: Activity) {
    val login = "login"
    val pref = "MAIN_PREF"
    val sp:SharedPreferences
    init {
        sp = activity.getSharedPreferences(pref, Context.MODE_PRIVATE)
    }

    fun setLogin(status: Boolean) {
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp.getBoolean(login, false)
    }
}