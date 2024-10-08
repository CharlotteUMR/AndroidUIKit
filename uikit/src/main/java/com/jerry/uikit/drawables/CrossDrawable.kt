package com.jerry.uikit.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.MainThread
import androidx.annotation.Px
import com.jerry.uikit.extensions.alpha

/**
 * 叉号[Drawable]
 *
 * @author Jerry
 */
class CrossDrawable(private val cross: Cross) : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val crossRectF = RectF()

    private var overrideColor: Int? = null
    private var overrideAlpha: Int = 255

    override fun draw(canvas: Canvas) {
        // 设置画笔属性
        paint.color = (overrideColor ?: cross.color).alpha(overrideAlpha / 255F)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = cross.strokeWidthPx.toFloat()
        paint.strokeCap = cross.strokeCap
        paint.strokeJoin = cross.strokeJoin

        // 叉号区域
        crossRectF.set(cross.bounds)

        // 绘制
        canvas.drawLine(crossRectF.left, crossRectF.top, crossRectF.right, crossRectF.bottom, paint)
        canvas.drawLine(crossRectF.left, crossRectF.bottom, crossRectF.right, crossRectF.top, paint)
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

    @MainThread
    override fun setAlpha(alpha: Int) {
        if (overrideAlpha != alpha) {
            overrideAlpha = alpha
            invalidateSelf()
        }
    }

    override fun getAlpha() = overrideAlpha

    @MainThread
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
            super.getIntrinsicWidth()
        } else {
            bounds.height()
        }
    }

    /**
     * [color] 叉号颜色
     *
     * [strokeWidthPx] 线条宽度像素值
     *
     * [bounds] 箭头位置和大小
     *
     * [strokeCap] 线条笔触[Paint.Cap]
     *
     * [strokeJoin] 线条连接[Paint.Join]
     */
    data class Cross @JvmOverloads constructor(
        @ColorInt val color: Int,
        @Px val strokeWidthPx: Int,
        val bounds: RectF,
        val strokeCap: Paint.Cap = Paint.Cap.ROUND,
        val strokeJoin: Paint.Join = Paint.Join.ROUND
    )
}