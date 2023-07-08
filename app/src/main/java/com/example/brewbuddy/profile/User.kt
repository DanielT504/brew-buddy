package com.example.brewbuddy.profile

import com.example.brewbuddy.R

class User {

    var firstName: String = "";
    var lastName: String = "";
    private var username: String = "";
    private var email: String = "";
    private var profilePicture: Int = R.drawable.profile_picture
    constructor(username: String, email: String) {
        this.username = username
        this.email = email
    }

    fun getUsername(): String {
        return username
    }

    fun getEmail(): String {
        return email
    }

    fun getAvatar(): Int {
        return profilePicture;
    }
}