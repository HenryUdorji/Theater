package com.henryudorji.theater.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.theater.app.BaseApplication
import com.henryudorji.theater.utils.Constants.SNACKBAR_LENGHT_SHORT

//
// Created by  on 4/28/2021.
//


fun View.showSnackBar(message: String, duration: Int = SNACKBAR_LENGHT_SHORT) {
    when(duration) {
        1 -> Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
        2 -> Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}