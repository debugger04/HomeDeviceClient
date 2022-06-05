package com.example.homedeviceclient.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.AlamatActivity
import com.example.homedeviceclient.DetailAlamatActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Alamat
import kotlinx.android.synthetic.main.activity_detail_alamat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlamatAdapter(val alamats: ArrayList<Alamat>): RecyclerView.Adapter<AlamatAdapter.AlamatViewHolder>() {
    class AlamatViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama:TextView = v.findViewById(R.id.txtNama)
        val txtTelepon:TextView = v.findViewById(R.id.txtTanggals)
        val txtAlamat:TextView = v.findViewById(R.id.txtAlamat)
        val btnUbah: Button = v.findViewById(R.id.btnUbahAlamat)
        val btnDelete: TextView = v.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AlamatViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.alamat_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: AlamatViewHolder, position: Int) {
        val alamat = alamats[position]
        holder.txtNama.text = alamat.nama + " - " + alamat.tag
        holder.txtTelepon.text = alamat.no_telp
        holder.txtAlamat.text = alamat.jalan + ", RT." + alamat.rt + "/RW." + alamat.rw + ", " + alamat.kecamatan + ", " + alamat.kab_kota + ", " + alamat.provinsi
        holder.btnUbah.setOnClickListener {
            val intent = Intent(holder.v.context, DetailAlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("ALAMAT_ID", alamat.id.toString())
            holder.v.context.startActivity(intent)
        }
        holder.btnDelete.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(holder.v.context)
            builder.setCancelable(true)
            builder.setTitle("Hapus Alamat")
            builder.setMessage("Yakin untuk menghapus alamat ?")
            builder.setPositiveButton("Hapus",
                DialogInterface.OnClickListener { dialog, id ->
                    ApiConfig.instanceRetrofit.hapusAlamat(alamat.id.toString()).enqueue(object :
                        Callback<ResponseModel> {
                        override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                            dialog.cancel()
                            if (response.body()!!.code == 200) {
                                val intent = Intent(holder.v.context, AlamatActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                holder.v.context.startActivity(intent)
                                Toast.makeText(holder.v.context, "Alamat telah dihapus!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(holder.v.context, "Kesalahan : "+response.body()!!.msg, Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                            dialog.cancel()
                            Toast.makeText(holder.v.context, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                })
            builder.setNegativeButton("Batal",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return alamats.size
    }
}