package com.rajender.adminordereats.model

data class AllMenu(
    val foodName : String ?= null,
    val foodPrice : String ?= null,
    val foodDescription : String ?= null,
    val foodImage : Int ?= null,
    val foodIngredient : String ?= null,

)
