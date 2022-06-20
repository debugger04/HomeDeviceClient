package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Product
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class CartAdapter (val products: ArrayList<Product>, val email: String, var listener :Listeners): RecyclerView.Adapter<CartAdapter.CartAViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class CartAViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama:TextView = v.findViewById(R.id.txtNamaProdukCart)
        val txtJumlah:TextView = v.findViewById(R.id.txtJml)
        val txtHarga: TextView = v.findViewById(R.id.txtHargaProdukCart)
        val imgProduk: ImageView = v.findViewById(R.id.imgProductCart)
        val btnTambah:Button = v.findViewById(R.id.btnPlus)
        val btnKurang:Button = v.findViewById(R.id.btnMinus)
        val btnHapus:Button = v.findViewById(R.id.btnHps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartAViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.cart_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder:CartAViewHolder, position: Int) {
        val prs = products[position]
        holder.txtNama.text = prs.nama
        var mantap = prs.harga * prs.jumlah
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(mantap)
        holder.txtHarga.text = "Rp."+formattedNumber
        Picasso.get().load(uri+prs.foto).into(holder.imgProduk)
        var jumlah = prs.jumlah
        holder.txtJumlah.text = jumlah.toString()
        holder.btnTambah.setOnClickListener {
            jumlah++
            ApiConfig.instanceRetrofit.updateCart(email, prs.id.toString(), jumlah.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        holder.txtJumlah.text = jumlah.toString()
                        var jadi = jumlah * prs.harga
                        val formattedNumber1: String = formatter.format(jadi)
                        holder.txtHarga.text = "Rp."+formattedNumber1

                        listener.onUpdate()
                    } else {
                        Toast.makeText(holder.v.context, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        holder.btnKurang.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener
            jumlah--
            ApiConfig.instanceRetrofit.updateCart(email, prs.id.toString(), jumlah.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        holder.txtJumlah.text = jumlah.toString()
                        var jadi = jumlah * prs.harga
                        val formattedNumber1: String = formatter.format(jadi)
                        holder.txtHarga.text = "Rp."+formattedNumber1

                        listener.onUpdate()
                    } else {
                        Toast.makeText(holder.v.context, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        holder.btnHapus.setOnClickListener {
            ApiConfig.instanceRetrofit.deleteCart(email, prs.id.toString()).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        listener.onDelete(position)
                    } else {
                        Toast.makeText(holder.v.context, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    interface Listeners {
        fun onUpdate()
        fun onDelete(position: Int)
    }
}