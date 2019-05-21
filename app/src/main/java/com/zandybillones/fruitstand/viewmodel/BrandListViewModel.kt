package com.zandybillones.fruitstand.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.repo.BrandListRepository


class BrandListViewModel : ViewModel() {

    private lateinit var brandList:MutableLiveData<ArrayList<Brand>>

    fun init() {
        brandList = BrandListRepository.getList()
    }

    fun getBrandList() : LiveData<ArrayList<Brand>> {
        return brandList
    }

    fun clear() {
        brandList.value?.clear()
    }



}