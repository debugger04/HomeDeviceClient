package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Alamat
import com.example.homedeviceclient.model.Promo
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.NumberFormat

class PromoAdapter(val promos: ArrayList<Promo>, var id_promo: Int, val totalBelanja: Int, var listener: Listeners): RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {
    class PromoViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama: TextView = v.findViewById(R.id.tv_namaPromo)
        val txtMaxCut: TextView = v.findViewById(R.id.tv_maxCut)
        val txtMinSpend: TextView = v.findViewById(R.id.tv_minSpend)
        val txtMasa: TextView = v.findViewById(R.id.tv_berlaku)
        val rd: RadioButton = v.findViewById(R.id.rd_alamat)
        val layout: CardView = v.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PromoViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.list_promo_layout, parent, false)
    )

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val p = promos[position]
        var tipe = ""
        if (p.tipe == "CB") {
            tipe = "Cashback"
        } else {
            tipe = "Discount"
        }
        holder.txtNama.text = p.nama+" "+tipe+" "+p.nilai+"%"
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(p.maximum_cut)
        val formattedNumber2: String = formatter.format(p.minimum_spend)
        holder.txtMaxCut.text = "Potongan Maksimal Rp."+formattedNumber
        holder.txtMinSpend.text = "Minimal Belanja Rp."+formattedNumber2
        holder.txtMasa.text = "Berlaku Hingga "+p.masa_berlaku
        if (id_promo == p.id) {
            holder.rd.isChecked = true
        }
        holder.layout.setOnClickListener{
            if (totalBelanja < p.minimum_spend) {
                Toast.makeText(holder.v.context, "Jumlah pembelian belum mencapai minimal belanja", Toast.LENGTH_SHORT).show()
            } else {
                listener.onClicked(p)
            }
        }
        holder.rd.setOnClickListener {
            if (totalBelanja < p.minimum_spend) {
                Toast.makeText(holder.v.context, "Jumlah pembelian belum mencapai minimal belanja", Toast.LENGTH_SHORT).show()
            } else {
                listener.onClicked(p)
            }
        }
    }

    override fun getItemCount(): Int {
        return promos.size
    }

    interface Listeners{
        fun onClicked(data: Promo)
    }
}