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
}