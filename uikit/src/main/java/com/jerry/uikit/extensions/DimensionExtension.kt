package com.jerry.uikit.extensions

import android.content.Context
import androidx.core.util.TypedValueCompat

fun Int.dpToPx(context: Context) = this.toFloat().dpToPx(context)
fun Float.dpToPx(context: Context) = TypedValueCompat.dpToPx(this, context.resources.displayMetrics)