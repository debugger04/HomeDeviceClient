package com.example.homedeviceclient.helper

import com.example.homedeviceclient.model.User

class ResponseModel {
    var code = 0
    lateinit var msg:String
    var user = User()
}