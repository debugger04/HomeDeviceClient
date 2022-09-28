package com.example.homedeviceclient.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.AlamatActivity
import com.example.homedeviceclient.DetailAlamatActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Alamat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAlamatAdapter(val alamats: ArrayList<Alamat>, var id_alamat: Int, var listener: Listeners): RecyclerView.Adapter<ListAlamatAdapter.ListAlamatViewHolder>() {
    class ListAlamatViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama: TextView = v.findViewById(R.id.tv_nama1)
        val txtTelepon: TextView = v.findViewById(R.id.tv_phone1)
        val txtAlamat: TextView = v.findViewById(R.id.tv_alamat1)
        val rd: RadioButton = v.findViewById(R.id.rd_alamat)
        val layout: CardView = v.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListAlamatViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.list_alamat_layout, parent, false)
    )

    override fun onBindViewHolder(holder: ListAlamatViewHolder, position: Int) {
        val alamat = alamats[position]
        holder.txtNama.text = alamat.nama + " - " + alamat.tag
        holder.txtTelepon.text = alamat.no_telp
        holder.txtAlamat.text = alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        if (id_alamat == alamat.id) {
            holder.rd.isChecked = true
        }
        holder.layout.setOnClickListener{
            listener.onClicked(alamat)
        }
        holder.rd.setOnClickListener {
            listener.onClicked(alamat)
        }
    }

    override fun getItemCount(): Int {
        return alamats.size
    }

    interface Listeners{
        fun onClicked(data:Alamat)
    }
}