package com.zandybillones.fruitstand.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.zandybillones.fruitstand.model.Brand
import com.zandybillones.fruitstand.model.Order
import com.zandybillones.fruitstand.model.Product
import com.zandybillones.traceutil.Trace

object OrderSummaryRepository {

    var db: FirebaseFirestore

    init {
        Trace.show("im in?")
        db = FirebaseFirestore.getInstance()
    }

    fun getOrder(id:String) : MutableLiveData<Order> {
        val mutableLiveData = MutableLiveData<Order>()

        db.collection(Collection.ORDERS)
            .whereEqualTo(FieldPath.documentId(), id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val list = ArrayList<Product>()

                    val map = task.result!!.documents[0]["items"] as ArrayList<HashMap<String,String>>
                    for (item in map) {
                        val product = Product(
                            item["id"].toString(),
                            item["name"].toString(),
                            item["brandId"].toString())
                        product.quantity = item["quantity"].toString()
                        list.add(product)
                    }

                    val _order = Order(
                        task.result!!.documents[0]["fullname"].toString(),
                        task.result!!.documents[0]["contact"].toString(),
                        list)

                    mutableLiveData.postValue(_order)

                } else {
                    // error fetching data
                }
            }

        return mutableLiveData
    }

    fun confirmOrderReceive(id:String, listener:IOnResultListener) {
        val doc = db.collection(Collection.ORDERS).document(id)
        doc.update("receive",true)
            .addOnSuccessListener {
                listener.onResult(true)
                Trace.show("Update Success!")
            }
            .addOnFailureListener {
                listener.onResult(false)
                Trace.show("Error!")
            }
    }


}