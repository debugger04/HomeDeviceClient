package com.example.homedeviceclient.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailBundlingActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Bundling
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.NumberFormat

class BundlingAdapter (val bundlings: ArrayList<Bundling>): RecyclerView.Adapter<BundlingAdapter.BundlingViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/bundling_images/"
    class BundlingViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgProd: ImageView = v.findViewById(R.id.imgView)
        val txtNama: TextView = v.findViewById(R.id.txtProductName)
        val txtHarga: TextView = v.findViewById(R.id.txtProductPrice)
        val btnDetail: ImageButton = v.findViewById(R.id.btnDetailPrd)
        val btnAddTocart: Button = v.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BundlingAdapter.BundlingViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_home_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: BundlingViewHolder, position: Int) {
        val b = bundlings[position]
        holder.btnAddTocart.visibility = View.GONE
        Picasso.get().load(uri+b.foto).into(holder.imgProd)
        holder.txtNama.text = b.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(b.harga)
        holder.txtHarga.text = "Rp."+formattedNumber
        holder.txtHarga.setTextColor(Color.parseColor("#FA4932"))
        holder.btnDetail.setOnClickListener {
            val intent = Intent(holder.v.context, DetailBundlingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("B1_ID", b.id.toString())
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bundlings.size
    }
}