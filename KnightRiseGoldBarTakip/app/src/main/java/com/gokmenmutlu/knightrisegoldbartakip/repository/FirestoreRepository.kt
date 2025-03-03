package com.gokmenmutlu.knightrisegoldbartakip.repository

import com.gokmenmutlu.knightrisegoldbartakip.model.KnightOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.model.RiseOnlinePriceData
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getKnightOnlinePriceData(callback: (List<KnightOnlinePriceData>?) -> Unit) {
        firestore.collection("knight_online_products") // collection_name
            .get()
            .addOnSuccessListener { result ->
                val priceList = result.mapNotNull { document ->
                    document.toObject(KnightOnlinePriceData::class.java)
                }
                callback(priceList) // Callback ile veriyi döndür
            }
            .addOnFailureListener {
                println("Firestore Veri çekme hatası: ${it.localizedMessage}")
                callback(null) // Hata durumunda null döndür
            }
    }

    fun getRiseOnlinePriceData(callback: (List<RiseOnlinePriceData>?) -> Unit) {
        firestore.collection("rise_online_products") // collection_name
            .get()
            .addOnSuccessListener { result ->
                val priceList = result.mapNotNull { document ->
                    document.toObject(RiseOnlinePriceData::class.java)
                }
                callback(priceList)
            }
            .addOnFailureListener {
                println("Firestore Veri çekme hatası: ${it.localizedMessage}")
                callback(null)
            }
    }




}
