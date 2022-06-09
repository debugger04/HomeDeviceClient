package com.example.homedeviceclient.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.*
import com.example.homedeviceclient.adapter.*
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.model.Category
import com.example.homedeviceclient.model.Merk
import com.example.homedeviceclient.model.Product
import com.example.homedeviceclient.model.ProductLama
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    var listMerk:ArrayList<Merk> = ArrayList()
    var listProdDisc:ArrayList<Product> = ArrayList()
    var listProduct:ArrayList<Product> = ArrayList()
    var listTukarTambah:ArrayList<Product> = ArrayList()
    var listFlagship:ArrayList<ProductLama> = ArrayList()
    var v:View ?= null
    lateinit var merkView: RecyclerView
    lateinit var prodDiscView: RecyclerView
    lateinit var prodView: RecyclerView
    lateinit var tukarView: RecyclerView
    lateinit var flagshipView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        getBrand()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLihatMerk.setOnClickListener {
            val intent = Intent(requireActivity(), AllMerkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btn_home_search.setOnClickListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnLihatProd.setOnClickListener {
            val intent = Intent(requireActivity(), AllProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun getBrand() {
        ApiConfig.instanceRetrofit.getBrand().enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    listMerk = response.brands
                    listProdDisc = response.discProducts
                    listProduct = response.nonDiscProducts
                    listTukarTambah = response.tukarTambahProducts
                    listFlagship = response.flagships
                    updateSlider()
                } else {
                    Toast.makeText(activity, "Kesalahan : "+response.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateSlider() {
        val layout = LinearLayoutManager(activity)
        layout.orientation = LinearLayoutManager.HORIZONTAL
        merkView.layoutManager = layout
        merkView.setHasFixedSize(true)
        merkView.adapter = MerkAdapter(listMerk)

        val layout1 = LinearLayoutManager(activity)
        layout1.orientation = LinearLayoutManager.HORIZONTAL
        prodDiscView.layoutManager = layout1
        prodDiscView.setHasFixedSize(true)
        prodDiscView.adapter = ProductDiscountAdapter(listProdDisc)

        val layout2 = LinearLayoutManager(activity)
        layout2.orientation = LinearLayoutManager.HORIZONTAL
        prodView.layoutManager = layout2
        prodView.setHasFixedSize(true)
        prodView.adapter = ProductAdapter(listProduct)

        val layout3 = LinearLayoutManager(activity)
        layout3.orientation = LinearLayoutManager.HORIZONTAL
        tukarView.layoutManager = layout3
        tukarView.setHasFixedSize(true)
        tukarView.adapter = ProductAdapter(listTukarTambah)

        val layout4 = LinearLayoutManager(activity)
        layout4.orientation = LinearLayoutManager.HORIZONTAL
        flagshipView.layoutManager = layout4
        flagshipView.setHasFixedSize(true)
        flagshipView.adapter = ProductLamaAdapter(listFlagship)
    }

    fun init(view: View) {
        merkView = view.findViewById(R.id.merkView)
        prodDiscView = view.findViewById(R.id.prodDiscView)
        prodView = view.findViewById(R.id.prodView)
        tukarView = view.findViewById(R.id.tukarTambahView)
        flagshipView = view.findViewById(R.id.flagshipView)
    }
}