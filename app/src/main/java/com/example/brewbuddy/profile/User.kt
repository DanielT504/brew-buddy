package com.example.brewbuddy.profile

import com.example.brewbuddy.R

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
        return bannerUrl ?: "https://firebasestorage.googleapis.com/v0/b/brew-buddy-ece452.appspot.com/o/placeholder_banner.png?alt=media&token=49e30f3c-cc2d-44f4-a91a-a1295f558a6a"
    }
    fun getUsername(): String {
        return username
    }

    fun getEmail(): String {
        return email
    }

    fun getAvatarUrl(): String {
        return avatarUrl ?: "https://firebasestorage.googleapis.com/v0/b/brew-buddy-ece452.appspot.com/o/placeholder_avatar.png?alt=media&token=e97bbc7f-8607-4d5a-8a87-312b1a77692b";
    }
}