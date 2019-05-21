package com.zandybillones.fruitstand.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.model.Product
import com.zandybillones.fruitstand.repo.IOnResultListener
import com.zandybillones.fruitstand.repo.ProductListRepository
import com.zandybillones.traceutil.Trace


class ProductListViewModel : ViewModel() {

    private var list = MutableLiveData<ArrayList<Product>>()
    private var orderId = MutableLiveData<String>()

    fun init() {
        list = ProductListRepository.getList()
    }

    fun getProductListByBrand(brandId:String) {
        ProductListRepository.getListByBrandId(brandId, object : IOnResultListener {
            override fun onResult(any: Any) {
                val _list = any
                list.postValue(_list as ArrayList<Product>)
            }
        })
    }



    fun getProductList() : LiveData<ArrayList<Product>> {
        return list
    }

    fun submitOrders(order:Order) {
        orderId.value = null
        ProductListRepository.submitOrders(order, object : IOnResultListener {
            override fun onResult(any: Any) {
                val id = any as String
                orderId.postValue(id)
            }
        })
    }

    fun getOrderId() : LiveData<String> {
        return orderId
    }


    fun clear() {
        list.value?.clear()
    }

}