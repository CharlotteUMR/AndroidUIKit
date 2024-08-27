package com.jerry.uikit.drawables

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.annotation.Px

/**
 * 点[Drawable]
 *
 * @author Jerry
 */
class DotDrawable(dotList: List<Dot>) : Drawable() {
    /**
     * 点数组
     */
    private val dotList = mutableListOf<Dot>()

    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 覆盖颜色
     */
    private var overrideColor: Int? = null

    constructor(vararg dots: Dot) : this(dots.toList())

    init {
        this.dotList.apply {
            clear()
            addAll(dotList)
        }
    }

    @MainThread
    fun addDot(dot: Dot) {
        dotList.add(dot)
        invalidateSelf()
    }

    @MainThread
    fun removeDot(index: Int) {
        dotList.removeAt(index)
        invalidateSelf()
    }

    @MainThread
    fun removeAllDots() {
        if (dotList.isEmpty()) return
        dotList.clear()
        invalidateSelf()
    }

    @MainThread
    fun setColor(color: Int) {
        if (overrideColor != color) {
            overrideColor = color
            invalidateSelf()
        }
    }

    @MainThread
    fun removeColor() {
        if (overrideColor != null) {
            overrideColor = null
            invalidateSelf()
        }
    }

    override fun setAlpha(alpha: Int) {
        if (paint.alpha != alpha) {
            paint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun getAlpha() = paint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (paint.colorFilter != colorFilter) {
            paint.colorFilter = colorFilter
            invalidateSelf()
        }
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int {
        val bounds = bounds
        return if (bounds.isEmpty) {
            super.getIntrinsicWidth()
        } else {
            bounds.width()
        }
    }

    override fun getIntrinsicHeight(): Int {
        val bounds = bounds
        return if (bounds.isEmpty) {
            super.getIntrinsicHeight()
        } else {
            bounds.height()
        }
    }

    override fun draw(canvas: Canvas) {
        dotList.forEach {
            // 设置画笔属性
            paint.style = Paint.Style.STROKE
            paint.color = overrideColor ?: it.color
            paint.strokeWidth = it.dotDiameter.toFloat()
            paint.strokeCap = it.strokeCap

            val (x, y) = it.position
            canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
        }
    }

    /**
     * [color] 点的颜色
     *
     * [dotDiameter] 点的直径
     *
     * [position] 点的位置
     *
     * [strokeCap] 点的形状（只有[Paint.Cap.ROUND]是圆的）
     */
    data class Dot @JvmOverloads constructor(
        @ColorInt val color: Int,
        @Px val dotDiameter: Int,
        val position: Pair<Int, Int>,
        val strokeCap: Paint.Cap = Paint.Cap.ROUND
    )
}