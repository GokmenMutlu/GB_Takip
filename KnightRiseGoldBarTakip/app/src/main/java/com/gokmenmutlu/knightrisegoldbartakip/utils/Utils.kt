package com.gokmenmutlu.knightrisegoldbartakip.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {


    // TimeStamp'i UTC+3'e göre normal tarihe dönüştüren fonksiyon
    fun convertTimestampToDate(seconds: Long, nanoseconds: Int): String {
        val milliseconds = seconds * 1000 + nanoseconds / 1000000 // Nano'yu milisaniyeye çeviriyoruz
        val date = Date(milliseconds)

        // Tarih formatını belirliyoruz
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        // TimeZone'u UTC+3 olarak ayarlıyoruz
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+3")

        return dateFormat.format(date)
    }



}