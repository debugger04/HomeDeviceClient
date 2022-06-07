package com.example.homedeviceclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Membership
import java.text.DecimalFormat
import java.text.NumberFormat

class MembershipAdapter(val memberships: ArrayList<Membership>, val tipe:String, val member_id:Int): RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder>() {
    class MembershipViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtNama:TextView = v.findViewById(R.id.txtLevelMembership)
        val txtManfaat:TextView = v.findViewById(R.id.txtManfaatMembership)
        val txtPoin:TextView = v.findViewById(R.id.txtPoinMembership)
        val imgSelected:ImageView = v.findViewById(R.id.imgSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MembershipViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.membership_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder: MembershipViewHolder, position: Int) {
        val membership = memberships[position]
        holder.txtNama.text = membership.nama
        val formatter: NumberFormat = DecimalFormat("#,###")
        val formattedNumber: String = formatter.format(membership.minimal_poin)
        holder.txtPoin.text = formattedNumber
        if (tipe == "DC") {
            holder.txtManfaat.text = "Diskon "+membership.potongan+"% untuk setiap transaksi."
        } else {
            holder.txtManfaat.text = "Cashback koin "+membership.potongan+"% untuk setiap transaksi."
        }
        if (membership.id == member_id) {
            holder.imgSelected.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return memberships.size
    }
}