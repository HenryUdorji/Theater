package com.henryudorji.theater.utils

import android.content.Context
import android.content.SharedPreferences
import com.henryudorji.theater.base.BaseApplication

//
// Created by  on 4/28/2021.
//


/**
 * functions to write and get from SharedPreferences
 */
fun Context.putStringInPref(key: String, value: String) {
    BaseApplication.sharedPref.edit().apply {
        putString(key, value)
        apply()
    }
}

fun Context.putBooleanInPref(key: String, value: Boolean) {
    BaseApplication.sharedPref.edit().apply {
        putBoolean(key, value)
        apply()
    }
}

fun Context.getBooleanFromPref(key: String): Boolean {
    return BaseApplication.sharedPref.getBoolean(key, false)
}

fun Context.getStringFromPref(key: String): String {
    return BaseApplication.sharedPref.getString(key, "")!!
}