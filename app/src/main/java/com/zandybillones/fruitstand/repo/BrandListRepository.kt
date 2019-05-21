package com.zandybillones.fruitstand.repo

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.traceutil.Trace

object BrandListRepository {

    var db: FirebaseFirestore
    var list = ArrayList<Brand>()

    init {
        Trace.show("im in?")
        db = FirebaseFirestore.getInstance()
    }

    fun getList() : MutableLiveData<ArrayList<Brand>> {
        list.clear()
        val mutableLiveData = MutableLiveData<ArrayList<Brand>>()
        mutableLiveData.value = list
        db.collection(Collection.BRANDS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (document in task.result!!) {
                        Trace.show(">> id: " + document.id + " data: " + document.data)
                        val brand = (Brand(document.id, document["name"].toString()))
                        list.add(brand)
                    }
                    mutableLiveData.postValue(list)
                } else {
                    // error fetching data
                }
            }

        return mutableLiveData
    }



}