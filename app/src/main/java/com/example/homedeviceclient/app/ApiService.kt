package com.example.homedeviceclient.app

import android.text.Editable
import com.example.homedeviceclient.helper.ResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
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
    @POST("user/delete_alamat")
    fun hapusAlamat(
        @Field("id") id :String
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

    @Multipart
    @POST("user/tambah_saldo_wallet")
    fun tambahSaldo(
        @Part("email") email :RequestBody,
        @Part("jumlah") jumlah : RequestBody,
        @Part bukti_transfer :MultipartBody.Part,
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/wallet_ongoing")
    fun getWalletOngoing(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/wallet_ongoing_detail")
    fun getWalletOngoingDetail(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/transaksi")
    fun getTransaksi(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/transaksi_detail")
    fun getTransaksiDetail(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/kode_promo")
    fun getKodePromo(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/detail_kode_promo")
    fun getKodeDetail(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/kode_promo_search")
    fun findKodePromo(
        @Field("kode") kode:String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/klaim_kode_promo")
    fun claimKodePromo(
        @Field("email") email :String,
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/member")
    fun userMember(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/memberships")
    fun getMembership(
        @Field("email") email :String
    ): Call<ResponseModel>

    @GET("kategori")
    fun getCategory(): Call<ResponseModel>

    @GET("home")
    fun getBrand(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("kategori/products")
    fun ktgProducts(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("brand/products")
    fun brandProducts(
        @Field("id") id :String
    ): Call<ResponseModel>

    @GET("brand")
    fun getAllBrand(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("product/search")
    fun searchProducts(
        @Field("nama") nama:String
    ): Call<ResponseModel>

    @GET("product")
    fun getAllProduct(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("kategori/search_product")
    fun searchProductKtg(
        @Field("nama") nama:String,
        @Field("id") id:String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("brand/search_product")
    fun searchProductBrd(
        @Field("nama") nama:String,
        @Field("id") id:String
    ): Call<ResponseModel>

    @GET("flagship")
    fun getAllFlagship(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("flagship/search_product")
    fun searchProductFlg(
        @Field("nama") nama:String
    ): Call<ResponseModel>

    @GET("tukar_tambah")
    fun getAllTukarTambah(): Call<ResponseModel>

    @GET("product/discount")
    fun getAllDisc(): Call<ResponseModel>

    @FormUrlEncoded
    @POST("product/discount_search")
    fun searchProductDc(
        @Field("nama") nama:String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tukar_tambah/search_product")
    fun searchProductTt(
        @Field("nama") nama:String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("product/detail")
    fun productDetails(
        @Field("id") id :String
    ): Call<ResponseModel>

    @Multipart
    @POST("tukar_tambah/store")
    fun pengajuanTukarTambah(
        @Part("email") email :RequestBody,
        @Part("id_produk") id_produk : RequestBody,
        @Part("nama_produk") nama_produk : RequestBody,
        @Part("merk_produk") merk_produk : RequestBody,
        @Part("deskripsi_produk") deskripsi_produk : RequestBody,
        @Part foto :MultipartBody.Part
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/transaksi_t")
    fun getTransaksiT(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/transaksi_t_detail")
    fun getTransaksiTDetail(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tukar_tambah/forum")
    fun getForums(
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tukar_tambah/forum/save")
    fun saveForums(
        @Field("email") email :String,
        @Field("pesan") pesan :String,
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("tukar_tambah/bayar")
    fun tukarTambahBayar(
        @Field("email") email :String,
        @Field("id") id :String,
        @Field("cek") cek :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/cart")
    fun getCart(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/cart/update")
    fun updateCart(
        @Field("email") email :String,
        @Field("id") id :String,
        @Field("jumlah") jumlah :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/cart/total")
    fun totalCart(
        @Field("email") email :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/cart/delete")
    fun deleteCart(
        @Field("email") email :String,
        @Field("id") id :String
    ): Call<ResponseModel>

    @FormUrlEncoded
    @POST("user/cart/store")
    fun addCart(
        @Field("email") email :String,
        @Field("id") id :String
    ): Call<ResponseModel>
}