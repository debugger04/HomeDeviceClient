package com.example.homedeviceclient.helper

import com.example.homedeviceclient.model.Alamat
import com.example.homedeviceclient.model.LogWallet
import com.example.homedeviceclient.model.User
import com.example.homedeviceclient.model.Wallet

class ResponseModel {
    var code = 0
    lateinit var msg:String
    var user = User()
    var alamats:ArrayList<Alamat> = ArrayList()
    var alamat = Alamat()
    var wallet = Wallet()
    var logwallets:ArrayList<LogWallet> = ArrayList()
}