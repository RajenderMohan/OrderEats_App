package com.rajender.adminordereats.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllMenu(
    val key: String? = null,
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodImage: Uri? = null,
    val foodDescription: String? = null,
    val foodIngredient: String? = null
) : Parcelable
