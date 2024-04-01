package com.harshilpadsala.signatureviewjetpackcompose.ext

import android.content.res.Resources
import android.util.TypedValue

fun Float.dpToPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}

fun Float.pxToDp(): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return this / scale
}