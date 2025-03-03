package com.gokmenmutlu.knightrisegoldbartakip.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class RiseOnlinePriceData(
    val serverName: String = "",   // Sunucu Adı
    val site: String = "",     // Site Adı
    val buyPrice: Double? = null,  // Alış Fiyatı
    val sellPrice: Double? = null, // Satış Fiyatı
    val lastUpdated: Timestamp? = null,  // Güncellenme Zamanı
    val logoUrl: String? = "",
    val siteUrl: String? = ""
) {
    // Parametresiz kurucu ekledik
    constructor() : this(
        serverName = "",
        site = "",
        buyPrice = null,
        sellPrice = null,
        lastUpdated = null,
        logoUrl = "",
        siteUrl = ""
    )
}
