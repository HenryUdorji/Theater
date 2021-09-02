package com.henryudorji.theater.app

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application(){
}