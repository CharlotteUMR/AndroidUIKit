package com.jerry.uikit.entities

data class Quaternion<T: Number>(
    val value1: T,
    val value2: T,
    val value3: T,
    val value4: T
){
    constructor(value: T) : this(value, value, value, value)

    fun isAllValueSame(): Boolean {
        return value1 == value2 && value2 == value3 && value3 == value4
    }

    override fun equals(other: Any?): Boolean {
        if (other is Number) return value1 == other && value2 == other && value3 == other && value4 == other
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}