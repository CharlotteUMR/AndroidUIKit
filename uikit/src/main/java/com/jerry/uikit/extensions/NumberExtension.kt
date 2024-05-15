package com.jerry.uikit.extensions

import kotlin.math.max
import kotlin.math.min

/**
 * 将值限制在[min]到[max]区间范围内
 */
fun Int.clamp(min: Int, max: Int) = max(min, min(this, max))
fun Long.clamp(min: Long, max: Long) = max(min, min(this, max))
fun Float.clamp(min: Float, max: Float) = max(min, min(this, max))