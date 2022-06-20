package com.example.homedeviceclient.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.R
import com.example.homedeviceclient.model.Forum

class ForumAdapter (val forums: ArrayList<Forum>): RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {
    class ForumViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val txtEmail: TextView = v.findViewById(R.id.txtEmailUser)
        val txtPesan: TextView = v.findViewById(R.id.txtPesan)
        val cardChat: CardView = v.findViewById(R.id.cardForum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ForumViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.forum_card_layout, parent, false)
    )

    override fun onBindViewHolder(holder:ForumViewHolder, position: Int) {
        val forum = forums[position]
        holder.txtEmail.text = forum.users_email
        holder.txtPesan.text = HtmlCompat.fromHtml(forum.message, HtmlCompat.FROM_HTML_MODE_LEGACY)
        if (forum.roles == "customer") {
            holder.cardChat.setCardBackgroundColor(Color.parseColor("#afe9eb"))
        }
    }

    override fun getItemCount(): Int {
        return forums.size
    }
}