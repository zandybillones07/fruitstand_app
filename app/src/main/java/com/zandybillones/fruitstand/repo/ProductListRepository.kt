package com.zandybillones.fruitstand.repo

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.model.Product
import com.zandybillones.traceutil.Trace

object ProductListRepository {

    var db: FirebaseFirestore
    var list = ArrayList<Product>()

    init {
        Trace.show("im in?")
        db = FirebaseFirestore.getInstance()
    }

    fun getListByBrandId(brandId:String, onResultListener: IOnResultListener) {
        list.clear()
        db.collection(Collection.PRODUCTS)
            .whereEqualTo("brandId", brandId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val product = Product(document.id, document["name"].toString(), document["brandId"].toString())
                        list.add(product)
                    }
                    onResultListener.onResult(list)
                } else {
                    // error fetching data
                }
            }
    }

    fun getList() : MutableLiveData<ArrayList<Product>> {
        val mutableLiveData = MutableLiveData<ArrayList<Product>>()
        mutableLiveData.value = list
        db.collection(Collection.PRODUCTS)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (document in task.result!!) {
                        val product = Product(document.id, document["name"].toString(), document["brandId"].toString())
                        list.add(product)
                    }
                    mutableLiveData.postValue(list)
                }
            }


        return mutableLiveData
    }

    fun submitOrders(order:Order, onResultListener: IOnResultListener) {
        db.collection(Collection.ORDERS)
            .add(order)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                onResultListener.onResult(documentReference.id)
            })
    }


}