package com.zandybillones.fruitstand.model

data class Order (
    val fullname:String,
    val contact:String,
    val items: List<Product>
) {
    var isReceive:Boolean = false
}