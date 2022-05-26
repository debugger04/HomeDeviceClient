package com.example.homedeviceclient.app

import com.example.homedeviceclient.helper.ResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") nama :String,
        @Field("email") email :String,
        @Field("password") pass :String,
        @Field("bod") bod :String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email :String,
        @Field("password") pass :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/gantipassword")
    fun gantiPassword(
        @Field("email") email :String,
        @Field("oldPassword") oldPass :String,
        @Field("newPassword") newPass :String,
        @Field("uNewPassword") uNewPass :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/detailprofil")
    fun detailProfil(
        @Field("email") email :String,
        @Field("name") nama :String,
        @Field("bod") bod :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/alamat")
    fun getAlamat(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/tambah_alamat")
    fun addAlamat(
        @Field("email") email :String,
        @Field("nama") nama :String,
        @Field("tag") tag :String,
        @Field("jalan") jalan :String,
        @Field("rt") rt :String,
        @Field("rw") rw :String,
        @Field("kecamatan") kecamatan :String,
        @Field("kabupaten") kabupaten :String,
        @Field("provinsi") provinsi :String,
        @Field("nomor_telepon") nomor_telepon :String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/detail_alamat")
    fun getAlamatDetail(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/update_alamat")
    fun updateAlamat(
        @Field("id") id :String,
        @Field("email") email :String,
        @Field("nama") nama :String,
        @Field("tag") tag :String,
        @Field("jalan") jalan :String,
        @Field("rt") rt :String,
        @Field("rw") rw :String,
        @Field("kecamatan") kecamatan :String,
        @Field("kabupaten") kabupaten :String,
        @Field("provinsi") provinsi :String,
        @Field("nomor_telepon") nomor_telepon :String,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/wallet")
    fun getWallet(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/wallet_history")
    fun getLogWallet(
        @Field("email") email :String
    ): Call<ResponseModel>
}