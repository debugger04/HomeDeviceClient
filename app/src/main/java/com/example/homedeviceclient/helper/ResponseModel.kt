package com.example.homedeviceclient.helper

import com.example.homedeviceclient.model.*

class ResponseModel {
    var code = 0
    lateinit var msg:String
    var user = User()
    var alamats:ArrayList<Alamat> = ArrayList()
    var alamat = Alamat()
    var wallet = Wallet()
    var logwallets:ArrayList<LogWallet> = ArrayList()
    var walletreqs:ArrayList<WalletRequest> = ArrayList()
    var walletreq = WalletRequest()
    var transaksis:ArrayList<Transaksi> = ArrayList()
    var transaksi = Transaksi()
    var tdtotal = 0
    var detailtransaksi:ArrayList<TransaksiDetail> = ArrayList()
    var kodepromo:ArrayList<KodePromo> = ArrayList()
    var kode = KodePromo()
    var member = Membership()
    var nextmember = Membership()
    var membersettings = MembershipSettings()
    var memberships:ArrayList<Membership> = ArrayList()
    var member_id = 0
    var kategoris:ArrayList<Category> = ArrayList()
    var brands:ArrayList<Merk> = ArrayList()
    var discProducts:ArrayList<Product> = ArrayList()
    var nonDiscProducts:ArrayList<Product> = ArrayList()
    var tukarTambahProducts:ArrayList<Product> = ArrayList()
    var products:ArrayList<Product> = ArrayList()
    var flagships:ArrayList<ProductLama> = ArrayList()
    var flagship = ProductLama()
    var product = Product()
    var brand = Merk()
    var nilai = 0
    var tukartambahs:ArrayList<TukarTambah> = ArrayList()
    var tukartambah = TukarTambah()
    var tukartambahdetail = TukarTambahDetail()
    var forums:ArrayList<Forum> = ArrayList()
    var total = 0

}
