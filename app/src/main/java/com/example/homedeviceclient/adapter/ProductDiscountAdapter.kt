package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailProductActivity
import com.example.homedeviceclient.LoginActivity
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

class ProductDiscountAdapter(val prods: ArrayList<Product>, val email: String?): RecyclerView.Adapter<ProductDiscountAdapter.ProductDiscountViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/product_images/"
    class ProductDiscountViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgProd: ImageView = v.findViewById(R.id.imgView)
        val txtNama: TextView = v.findViewById(R.id.txtProductName)
        val txtHarga: TextView = v.findViewById(R.id.txtProductPrice)
        val btnDetail: ImageButton = v.findViewById(R.id.btnDetail)
        val btnAddTocart: Button = v.findViewById(R.id.btnAddToCart)
        val txtDiskon: TextView = v.findViewById(R.id.txtDiskon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductDiscountViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_discount_home_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ProductDiscountViewHolder, position: Int) {
        val prod = prods[position]
        Picasso.get().load(uri+prod.foto).into(holder.imgProd)
        holder.txtNama.text = prod.nama
        val final = prod.harga - (prod.harga * prod.nilai / 100)
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(final)
        holder.txtHarga.text = "Rp."+formattedNumber
        holder.txtDiskon.text = prod.nilai.toString()+"%"
        holder.btnDetail.setOnClickListener {
            val intent = Intent(holder.v.context, DetailProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("P7_ID", prod.id.toString())
            holder.v.context.startActivity(intent)
        }
        holder.btnAddTocart.setOnClickListener {
            if (email != null) {
                ApiConfig.instanceRetrofit.addCart(email, prod.id.toString()).enqueue(object :
                    Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                        val response = response.body()!!
                        if(response.code == 200) {
                            Toast.makeText(holder.v.context, "Berhasil tambah ke keranjang", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(holder.v.context, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                        Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                val intent = Intent(holder.v.context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                holder.v.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return prods.size
    }
}