package com.gokmenmutlu.knightrisegoldbartakip.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gokmenmutlu.knightrisegoldbartakip.model.RiseOnlinePriceData
import com.gokmenmutlu.knightrisegoldbartakip.repository.FirestoreRepository

class RiseOnlineViewModel : ViewModel() {

    private val repository = FirestoreRepository()
    val riseOnlinePriceData: MutableLiveData<List<RiseOnlinePriceData>?> = MutableLiveData()

    fun getRiseOnlinePriceData() {
        repository.getRiseOnlinePriceData { priceList ->
            riseOnlinePriceData.value = priceList
        }
    }
}