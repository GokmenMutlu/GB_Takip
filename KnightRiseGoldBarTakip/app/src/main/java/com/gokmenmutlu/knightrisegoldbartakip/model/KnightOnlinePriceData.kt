package com.gokmenmutlu.knightrisegoldbartakip.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
data class KnightOnlinePriceData(
    val serverName: String = "",   // Sunucu Adı (ZERO, FELIS vb.)
    val site: String = "",     // Site Adı
    val buyPrice: Double? = null,  // Alış Fiyatı (null olabilir)
    val sellPrice: Double? = null, // Satış Fiyatı (null olabilir)
    val lastUpdated: Timestamp? = null,  // Güncellenme Zamanı (null olabilir)
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
