package com.example.homedeviceclient.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Category

class CategoryAdapter(val kategoris: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val btnKtg:Button = v.findViewById(R.id.btn_go_ktg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.category_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder:CategoryViewHolder, position: Int) {
        val kategori = kategoris[position]
        holder.btnKtg.text = kategori.nama
    }

    override fun getItemCount(): Int {
        return kategoris.size
    }
}