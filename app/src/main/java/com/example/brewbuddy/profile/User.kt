package com.example.brewbuddy.profile

import com.example.brewbuddy.R
import com.example.brewbuddy.common.Constants

class User {

    var firstName: String = "";
    var lastName: String = "";
    private var username: String = "";
    private var email: String = "";
    private var bannerUrl: String? = ""
    private var avatarUrl: String? = ""
    constructor(username: String, email: String, bannerUrl: String?, avatarUrl: String?) {
        this.username = username
        this.email = email
        this.bannerUrl = bannerUrl
        this.avatarUrl = avatarUrl
    }

    fun getBannerUrl(): String {
        return bannerUrl ?: Constants.DEFAULT_BANNER_URL
    }
    fun getUsername(): String {
        return username
    }

    fun getEmail(): String {
        return email
    }

    fun getAvatarUrl(): String {
        return avatarUrl ?: Constants.DEFAULT_AVATAR_URL;
    }
}