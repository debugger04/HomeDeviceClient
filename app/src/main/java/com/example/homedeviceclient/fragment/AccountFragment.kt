package com.example.homedeviceclient.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.homedeviceclient.*
import com.example.homedeviceclient.helper.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_account.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var btnLogout:Button
    lateinit var btnGantiPass:Button
    lateinit var btnDetailProf:Button
    lateinit var btnDetailAlamat:Button
    lateinit var btnDetailWall:Button
    lateinit var btnTransaksi:Button
    lateinit var btnKode:Button
    lateinit var btnMember:Button
    lateinit var txtNama:TextView
    lateinit var txtEmail:TextView
    lateinit var sp:SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_account, container, false)
        // Inflate the layout for this fragment
        btnLogout = view.findViewById(R.id.btn_logout)
        btnGantiPass = view.findViewById(R.id.btnGantiPassword)
        btnDetailProf = view.findViewById(R.id.btnDetailProfil)
        btnDetailAlamat = view.findViewById(R.id.btnDetailAlamat)
        btnDetailWall = view.findViewById(R.id.btnDetailWallet)
        btnTransaksi = view.findViewById(R.id.btnTransaksi)
        btnKode = view.findViewById(R.id.btnDetailCode)
        btnMember = view.findViewById(R.id.btnDetailMember)
        txtNama = view.findViewById(R.id.txtNama)
        txtEmail = view.findViewById(R.id.txtEmail)
        sp = SharedPrefs(requireActivity())

        btnLogout.setOnClickListener {
            sp.setLogin(false)
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnGantiPass.setOnClickListener {
            val intent = Intent(requireActivity(), GantiPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnDetailProf.setOnClickListener {
            val intent = Intent(requireActivity(), DetailUserActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnDetailAlamat.setOnClickListener {
            val intent = Intent(requireActivity(), AlamatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnDetailWall.setOnClickListener {
            val intent = Intent(requireActivity(), WalletActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnTransaksi.setOnClickListener {
            val intent = Intent(requireActivity(), TransaksiActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnKode.setOnClickListener {
            val intent = Intent(requireActivity(), KodePromoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnMember.setOnClickListener {
            val intent = Intent(requireActivity(), MembershipActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        setData()

        return view
    }

    private fun setData() {
        if (sp.getUser() == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        txtNama.text = user.name
        txtEmail.text = user.email
    }

}