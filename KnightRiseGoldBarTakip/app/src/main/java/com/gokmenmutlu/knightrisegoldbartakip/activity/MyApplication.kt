package com.gokmenmutlu.knightrisegoldbartakip.activity

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Google Mobil Reklamlar SDK'sını başlat
        MobileAds.initialize(this)
    }

}