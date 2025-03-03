package com.gokmenmutlu.knightrisegoldbartakip.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gokmenmutlu.knightrisegoldbartakip.R
import com.gokmenmutlu.knightrisegoldbartakip.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var appOpenAd: AppOpenAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavHostFragment almak
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.itemIconTintList = null

        binding.bottomNavigationView.setupWithNavController(navController)

        supportActionBar?.hide()

        loadAppOpenAd()
    }


    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()


        AppOpenAd.load(
            this,
            adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, // veya APP_OPEN_AD_ORIENTATION_LANDSCAPE
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    showAppOpenAd()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Reklam yüklenemedi, hatayı ele alın
                }
            }
        )
    }

    private fun showAppOpenAd() {
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Reklam kapatıldı, sonraki işlemlere devam edin
                appOpenAd = null
                loadAppOpenAd() // Yeni reklam yükle
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Reklam gösterilemedi, hatayı ele alın
                appOpenAd = null
                loadAppOpenAd() // Yeni reklam yükle
            }

            override fun onAdShowedFullScreenContent() {
                // Reklam gösterildi
            }
        }
        appOpenAd?.show(this)
    }

    override fun onResume() {
        super.onResume()

    }

}



