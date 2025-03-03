package com.gokmenmutlu.knightrisegoldbartakip.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gokmenmutlu.knightrisegoldbartakip.model.KnightOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.repository.FirestoreRepository


class KnightOnlineViewModel : ViewModel() {

    private val repository = FirestoreRepository()
    val knightOnlinePriceData: MutableLiveData<List<KnightOnlinePriceData>?> = MutableLiveData()

    fun getKnightOnlinePriceData() {
        // Firestore'dan veri çekme işlemi burada
        repository.getKnightOnlinePriceData { priceList ->
            knightOnlinePriceData.value = priceList
        }
    }
}

