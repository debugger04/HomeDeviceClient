package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.DetailCategoryActivity
import com.example.homedeviceclient.DetailMerkActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Merk
import com.squareup.picasso.Picasso

class MerkAdapter(val merks: ArrayList<Merk>): RecyclerView.Adapter<MerkAdapter.MerkViewHolder>() {
    var uri = "http://192.168.0.104/ta/tugasakhir/public/brand_images/"
    class MerkViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val imgMerk:ImageView = v.findViewById(R.id.imageMerk)
        val btnMerk:Button = v.findViewById(R.id.btnNamaMerk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MerkViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.merk_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: MerkAdapter.MerkViewHolder, position: Int) {
        val merk = merks[position]
        holder.btnMerk.text = merk.nama
        Picasso.get().load(uri+merk.gambar).into(holder.imgMerk)
        holder.btnMerk.setOnClickListener {
            val intent = Intent(holder.v.context, DetailMerkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("M_ID", merk.id.toString())
            holder.v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return merks.size
    }
}