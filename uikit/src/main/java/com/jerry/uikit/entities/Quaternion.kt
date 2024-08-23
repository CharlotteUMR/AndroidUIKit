package com.jerry.uikit.entities

/**
 * [Int]四元数
 */
data class Quaternion(
    val value1: Int = 0,
    val value2: Int = 0,
    val value3: Int = 0,
    val value4: Int = 0
) {
    constructor(value: Int) : this(value, value, value, value)

    fun isEmpty() = value1 == 0 && value2 == 0 && value3 == 0 && value4 == 0
}

/**
 * [Float]四元数
 */
data class QuaternionF(
    val value1: Float = 0F,
    val value2: Float = 0F,
    val value3: Float = 0F,
    val value4: Float = 0F
) {
    constructor(value: Float) : this(value, value, value, value)

    fun isEmpty() = value1 == 0F && value2 == 0F && value3 == 0F && value4 == 0F
}