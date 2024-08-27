package com.jerry.uikit.entities

data class Offset<T : Number>(val x: T, val y: T) {
    constructor(value: T) : this(value, value)

    override fun equals(other: Any?): Boolean {
        if (other is Number) return x == other && y == other
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
