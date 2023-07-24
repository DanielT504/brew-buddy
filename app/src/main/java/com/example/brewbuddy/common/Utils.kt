package com.example.brewbuddy.common

import android.util.Log
import com.example.brewbuddy.marketplace.Filter

private fun filterToString(list: List<Filter>): String {
    var string = ""
    list.forEach { el ->
        if(!string.isEmpty()) {
            string += "&"
        }
        if(el.name.contains("Asce") || el.name.contains("Desc")) {
            string += "sort=${el.name}"
        } else {
            string += "${el.name}=${el.enabled}"
        }
    }
    return string
}

private fun keywordsToString(str: String): String {
    if(str.isNotEmpty()) {
        return "keywords=${str}"
    }
    return ""
}
fun createQueryString(keywords: String, filters: List<Filter>): String {
    val keywordString = keywordsToString(keywords)
    val filterString = filterToString(filters)

    if (filterString.isEmpty() || keywordString.isEmpty()) {
        Log.d("createQueryString", keywordString + filterString)

        return keywordString + filterString
    }
    Log.d("createQueryString", "${keywordString}&${filterString}")
    return "${keywordString}&${filterString}"
}