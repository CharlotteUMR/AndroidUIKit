package com.jerry.uikit.extensions

import android.graphics.Color

/**
 * 给颜色设置[alphaPercent]alpha百分比（基于当前颜色叠加）
 */
fun Int.alpha(alphaPercent: Float): Int {
    val alpha = Color.alpha(this)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    val newAlpha = (alpha * alphaPercent).toInt()
    return Color.argb(newAlpha, red, green, blue)
}