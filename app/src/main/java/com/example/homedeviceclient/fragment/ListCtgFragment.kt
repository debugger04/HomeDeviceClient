package com.example.homedeviceclient.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homedeviceclient.CheckoutActivity
import com.example.homedeviceclient.ListPromoActivity
import com.example.homedeviceclient.LoginActivity
import com.example.homedeviceclient.R
import com.example.homedeviceclient.adapter.PromoAdapter
import com.example.homedeviceclient.app.ApiConfig
import com.example.homedeviceclient.helper.ResponseModel
import com.example.homedeviceclient.helper.SharedPrefs
import com.example.homedeviceclient.model.Promo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListCtgFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListCtgFragment : Fragment() {
    lateinit var sp: SharedPrefs
    var listPromo:ArrayList<Promo> = ArrayList()
    lateinit var txtKosong: TextView
    lateinit var ctgView: RecyclerView
    var total: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_list_ctg, container, false)
        sp = SharedPrefs(requireActivity())
        txtKosong = view.findViewById(R.id.div_ctgKosong)
        ctgView = view.findViewById(R.id.ctg_view)

        val activity: ListPromoActivity? = activity as ListPromoActivity?
        total = activity!!.totalBelanja()!!

        getPromo()

        return view
    }

    private fun getPromo() {
        if (sp.getUser() == null) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        ApiConfig.instanceRetrofit.checkoutPromo(user.email).enqueue(object :
            Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                val response = response.body()!!
                if(response.code == 200) {
                    txtKosong.visibility = View.GONE
                    listPromo = response.promoc
                    updateList()
                } else {
                    txtKosong.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(activity, "Kesalahan : "+t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateList() {
        var promo = 0
        if (sp.getPromo() == null) {
            promo = 0
        } else {
            val a = sp.getPromo()!!
            promo = a.id
        }
        val layout = LinearLayoutManager(activity)
        layout.orientation = LinearLayoutManager.VERTICAL
        ctgView.setLayoutManager(layout)
        ctgView.setHasFixedSize(true)
        ctgView.adapter = PromoAdapter(listPromo, promo, total, object: PromoAdapter.Listeners{
            override fun onClicked(data: Promo) {
                sp.setPromo(data)
                sp.setTipePromo(0)
                activity?.finish()
            }
        })
    }
}