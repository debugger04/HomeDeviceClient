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
}
