package com.example.brewbuddy.profile

import android.util.Log

class User {

    var firstName: String = "";
    var lastName: String = "";

    constructor(username: String) {
        if(username == "test") {
            firstName = "John"
            lastName = "Doe"
        } else {
            firstName = "Bob"
            lastName = "Roe"
        }
    }
}