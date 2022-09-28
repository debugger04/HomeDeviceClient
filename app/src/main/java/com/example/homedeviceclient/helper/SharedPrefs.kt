package com.example.homedeviceclient.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.homedeviceclient.model.Alamat
import com.example.homedeviceclient.model.KodePromo
import com.example.homedeviceclient.model.Promo
import com.example.homedeviceclient.model.User
import com.google.gson.Gson

class SharedPrefs(activity: Activity) {
    val login = "login"
    val pref = "MAIN_PREF"
    val user = "user"
    val alamat = "alamat"
    val promo = "promo"
    val tipepromo = "type"
    val kodepromo = "kode"
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

    fun setUser(value: User?) {
        val data = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser(): User? {
        val data = sp.getString(user, null) ?: return null
        val json = Gson().fromJson<User>(data, User::class.java)
        return json
    }

    fun setAlamat(value: Alamat?) {
        val data = Gson().toJson(value, Alamat::class.java)
        sp.edit().putString(alamat, data).apply()
    }

    fun getAlamat(): Alamat? {
        val data = sp.getString(alamat,null) ?: return null
        val json = Gson().fromJson<Alamat>(data, Alamat::class.java)
        return json
    }

    fun setPromo(value: Promo?) {
        val data = Gson().toJson(value, Promo::class.java)
        sp.edit().putString(promo, data).apply()
    }

    fun getPromo(): Promo? {
        val data = sp.getString(promo,null) ?: return null
        val json = Gson().fromJson<Promo>(data, Promo::class.java)
        return json
    }

    fun setTipePromo(value: Int?) {
        val data = value
        sp.edit().putString(tipepromo, data.toString()).apply()
    }

    fun getTipePromo(): Int? {
        val data = sp.getString(tipepromo, null) ?: return null
        return data.toInt()
    }

    fun setVoucher(value: KodePromo?) {
        val data = Gson().toJson(value, KodePromo::class.java)
        sp.edit().putString(kodepromo, data).apply()
    }

    fun getVoucher(): KodePromo? {
        val data = sp.getString(kodepromo, null) ?: return null
        val json = Gson().fromJson<KodePromo>(data, KodePromo::class.java)
        return json
    }
}