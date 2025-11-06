package com.rajender.ordereats.data

object CartRepository {
    val cartItemNames = mutableListOf<String>()
    val cartItemPrices = mutableListOf<String>()
    val cartItemImages = mutableListOf<Int>()

    fun addItem(name: String, price: String, image: Int) {
        cartItemNames.add(name)
        cartItemPrices.add(price)
        cartItemImages.add(image)
    }
}