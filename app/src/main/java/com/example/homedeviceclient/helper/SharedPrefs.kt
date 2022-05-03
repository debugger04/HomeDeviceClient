package com.example.homedeviceclient.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.homedeviceclient.model.User
import com.google.gson.Gson

class SharedPrefs(activity: Activity) {
    val login = "login"
    val pref = "MAIN_PREF"
    val user = "user"
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

    fun setUser(value: User) {
        val data = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): User? {
        val data = sp.getString(user, null) ?: return null
        val json = Gson().fromJson<User>(data, User::class.java)
        return json
    }
}