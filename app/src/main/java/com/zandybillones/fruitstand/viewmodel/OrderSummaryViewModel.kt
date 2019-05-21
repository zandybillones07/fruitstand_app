package com.zandybillones.fruitstand.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.repo.BrandListRepository
import com.zandybillones.fruitstand.repo.IOnResultListener
import com.zandybillones.fruitstand.repo.OrderSummaryRepository


class OrderSummaryViewModel : ViewModel() {

    private lateinit var order:LiveData<Order>
    private var isReceive = MutableLiveData<Boolean>()

    fun init(id:String) {
        order = OrderSummaryRepository.getOrder(id)
    }

    fun getOrder() : LiveData<Order> {
        return order
    }

    fun setReceive(id:String) {
        OrderSummaryRepository.confirmOrderReceive(id, object : IOnResultListener {
            override fun onResult(any: Any) {
                isReceive.postValue(any as Boolean)
            }

        })
    }

    fun isReceive():LiveData<Boolean> {
        return isReceive
    }


}