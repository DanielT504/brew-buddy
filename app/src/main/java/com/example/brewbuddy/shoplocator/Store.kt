package com.example.brewbuddy.shoplocator;

public class Store {
    var storeName: String = "";
    var address: String = "";
    var latitude: Double = 0.0;
    var longitude: Double = 0.0;
    var items: List<String> = listOf();
    var rating: Double = 0.0;
    var saved: Boolean = false;

    constructor(
        storeName: String = "",
        address: String = "",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        items: List<String> = listOf(),
        rating: Double = 0.0,
        saved: Boolean = false,
    ){
        this.storeName = storeName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude
        this.items = items;
        this.rating = rating;
        this.saved = saved;
    }

    fun getName(): String {
        return storeName
    }
}
