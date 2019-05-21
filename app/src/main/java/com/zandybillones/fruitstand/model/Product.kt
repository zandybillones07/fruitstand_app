package com.zandybillones.fruitstand.model

data class Product(val id:String, val name:String, val brandId:String) {
    var image:String = ""
    var isSelected:Boolean = false
    var quantity:String = "1"
}

