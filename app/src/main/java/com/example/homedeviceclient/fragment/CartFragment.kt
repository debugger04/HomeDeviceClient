package com.example.homedeviceclient.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.CheckoutActivity
import com.example.homedeviceclient.LoginActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.TukarTambahDetailActivity
import com.example.homedeviceclient.adapter.CartAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    var listCart:ArrayList<Product> = ArrayList()
    var v:View ?= null
    lateinit var cartView: RecyclerView
    lateinit var sp: SharedPrefs
    lateinit var ln1:LinearLayout
    lateinit var ln2:LinearLayout
    lateinit var txtJumlah:TextView
    lateinit var adapter:CartAdapter
    lateinit var btnCheckout:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cart, container, false)
        sp = SharedPrefs(requireActivity())
        cartView = view.findViewById(R.id.cartView)
        ln1 = view.findViewById(R.id.ln1)
        ln2 = view.findViewById(R.id.ln2)
        txtJumlah = view.findViewById(R.id.txtSub)
        btnCheckout = view.findViewById(R.id.btn_Checkout)
        if (sp.getStatusLogin()) {
            getCart()
        }

        btnCheckout.setOnClickListener {
            if (sp.getStatusLogin()) {
                val intent = Intent(requireActivity(), CheckoutActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        getCart()
        hitungTotal()
    }

    private fun getCart() {
        var user = sp.getUser()
        if (user != null) {
            ApiConfig.instanceRetrofit.getCart(user.email).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        ln2.visibility = View.GONE
                        ln1.visibility = View.VISIBLE
                        listCart = response.products
                        updateList(user.email)
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val formattedNumber: String = formatter.format(response.total)
                        txtJumlah.text = "Rp."+formattedNumber
                    } else {
                        ln2.visibility = View.VISIBLE
                        ln1.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(activity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            ln2.visibility = View.VISIBLE
            ln1.visibility = View.GONE
        }
    }

    fun updateList(email: String) {
        val layout = LinearLayoutManager(activity)
        layout.orientation = LinearLayoutManager.VERTICAL
        cartView.layoutManager = layout
        cartView.setHasFixedSize(true)
        adapter = CartAdapter(listCart, email, object: CartAdapter.Listeners {
            override fun onUpdate() {
                hitungTotal()
            }

            override fun onDelete(position: Int) {
                hitungTotal()
                listCart.removeAt(position)
                adapter.notifyDataSetChanged()
                if (listCart.size < 1) {
                    ln2.visibility = View.VISIBLE
                    ln1.visibility = View.GONE
                }
            }

        })
        cartView.adapter = adapter
    }

    fun hitungTotal() {
        var user = sp.getUser()
        if (user != null) {
            ApiConfig.instanceRetrofit.totalCart(user.email).enqueue(object :
                Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    val response = response.body()!!
                    if(response.code == 200) {
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val formattedNumber: String = formatter.format(response.total)
                        txtJumlah.text = "Rp."+formattedNumber
                    } else {
                        Toast.makeText(activity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(activity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}