package com.example.homedeviceclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homedeviceclient.adapter.ForumAdapter
import com.example.homedeviceclient.adapter.TukarTambahAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Forum
import com.example.homedeviceclient.model.TukarTambah
import kotlinx.android.synthetic.main.activity_forum.*
import kotlinx.android.synthetic.main.activity_tukar_tambah.*
import kotlinx.android.synthetic.main.forum_card_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumActivity : AppCompatActivity() {
    lateinit var sp: SharedPrefs
    var id = ""
    var listForum: ArrayList<Forum> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        sp = SharedPrefs(this)

        id = intent.getStringExtra("O1_ID").toString()

        // calling the action bar
        val actionBar: ActionBar = supportActionBar!!

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true)

        getForum()

        btnSendMsg.setOnClickListener {
            var txtMantap:SpannableString = SpannableString.valueOf(txtPesanKirim.text.toString())
            var moda = HtmlCompat.toHtml(txtMantap, TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
            saveForum(moda)
            txtPesanKirim.setText("")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getForum() {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.getForums(id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listForum = response.forums
                    updateList()
                } else {
                    Toast.makeText(this@ForumActivity, response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@ForumActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        val layout = LinearLayoutManager(this)
        layout.orientation = LinearLayoutManager.VERTICAL
        forumView.setLayoutManager(layout)
        forumView.setHasFixedSize(true)
        forumView.adapter = ForumAdapter(listForum)
    }

    fun saveForum(pesan:String) {
        if (sp.getUser() == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.saveForums(user.email, pesan, id).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    getForum()
                } else {
                    Toast.makeText(this@ForumActivity, response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@ForumActivity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}