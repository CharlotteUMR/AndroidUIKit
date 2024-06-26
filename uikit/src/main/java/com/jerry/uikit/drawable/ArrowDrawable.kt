package com.jerry.uikit.drawable

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.MainThread
import androidx.annotation.Px
import com.jerry.uikit.drawable.ArrowDrawable.Direction.Companion.DIRECTION_BOTTOM
import com.jerry.uikit.drawable.ArrowDrawable.Direction.Companion.DIRECTION_LEFT
import com.jerry.uikit.drawable.ArrowDrawable.Direction.Companion.DIRECTION_RIGHT
import com.jerry.uikit.drawable.ArrowDrawable.Direction.Companion.DIRECTION_TOP
import com.jerry.uikit.extensions.alpha
import com.jerry.uikit.extensions.clamp
import com.jerry.uikit.utils.PathUtil

/**
 * 箭头[Drawable]
 *
 * [builder] 构造器[Builder]
 *
 * @author p_jruixu
 */
class ArrowDrawable(arrowList: List<Arrow>) : Drawable() {
    /**
     * 箭头数组
     */
    private val arrowList = mutableListOf<Arrow>()

    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 箭头主体区域
     */
    private val arrowRectF = RectF()

    /**
     * 不透明度百分比
     */
    private var alpha: Float = 1F

    constructor(vararg arrows: Arrow) : this(arrows.toList())

    init {
        this.arrowList.apply {
            clear()
            addAll(arrowList)
        }
    }

    fun getArrowList(): List<Arrow> = arrowList

    @MainThread
    fun addArrow(arrows: List<Arrow>) {
        arrowList.addAll(arrows)
        invalidateSelf()
    }

    @MainThread
    fun removeArrow(index: Int = arrowList.size - 1) {
        if (arrowList.isEmpty()) return
        arrowList.removeAt(index.clamp(0, arrowList.size - 1))
        invalidateSelf()
    }

    @MainThread
    fun removeAllArrow() {
        if (arrowList.isEmpty()) return
        arrowList.clear()
        invalidateSelf()
    }

    @MainThread
    fun setColor(color: Int) {
        arrowList.forEach { it.color = color }
        invalidateSelf()
    }

    /**
     * 对箭头颜色设置透明度（在[Arrow.color]基础上设置）
     */
    override fun setAlpha(alpha: Int) {
        this.alpha = (alpha / 255F).clamp(0F, 1F)
        invalidateSelf()
    }

    override fun getAlpha() = (alpha * 255).toInt()

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (paint.colorFilter != colorFilter) {
            paint.colorFilter = colorFilter
            invalidateSelf()
        }
    }

    override fun draw(canvas: Canvas) {
        val pathBuilder = PathUtil.getPathBuilder()
        arrowList.forEach {
            // 设置画笔属性
            paint.color = it.color.alpha(alpha)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = it.strokeWidthPx.toFloat()
            paint.strokeCap = it.strokeCap
            paint.strokeJoin = it.strokeJoin

            // 箭头区域
            arrowRectF.set(it.bounds)

            // 箭头路径
            pathBuilder.reset()
            when (it.direction) {
                DIRECTION_LEFT -> {
                    pathBuilder.moveTo(arrowRectF.right, arrowRectF.top)
                    pathBuilder.lineTo(arrowRectF.left, arrowRectF.centerY())
                    pathBuilder.lineTo(arrowRectF.right, arrowRectF.bottom)
                }
                DIRECTION_TOP -> {
                    pathBuilder.moveTo(arrowRectF.left, arrowRectF.bottom)
                    pathBuilder.lineTo(arrowRectF.centerX(), arrowRectF.top)
                    pathBuilder.lineTo(arrowRectF.right, arrowRectF.bottom)
                }
                DIRECTION_RIGHT -> {
                    pathBuilder.moveTo(arrowRectF.left, arrowRectF.top)
                    pathBuilder.lineTo(arrowRectF.right, arrowRectF.centerY())
                    pathBuilder.lineTo(arrowRectF.left, arrowRectF.bottom)
                }
                DIRECTION_BOTTOM -> {
                    pathBuilder.moveTo(arrowRectF.left, arrowRectF.top)
                    pathBuilder.lineTo(arrowRectF.centerX(), arrowRectF.bottom)
                    pathBuilder.lineTo(arrowRectF.right, arrowRectF.top)
                }
            }

            // 绘制
            canvas.drawPath(pathBuilder.build(), paint)
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

    /**
     * [color] 箭头颜色
     *
     * [strokeWidthPx] 线条宽度像素值
     *
     * [direction] 箭头方向[Direction]
     *
     * [bounds] 箭头位置和大小
     *
     * [strokeCap] 线条笔触[Paint.Cap]
     *
     * [strokeJoin] 线条连接[Paint.Join]
     */
    data class Arrow @JvmOverloads constructor(
        @ColorInt var color: Int,
        @Px var strokeWidthPx: Int,
        @Direction var direction: Int,
        var bounds: RectF,
        var strokeCap: Paint.Cap = Paint.Cap.ROUND,
        var strokeJoin: Paint.Join = Paint.Join.ROUND
    )

    /**
     * 箭头方向
     *
     * [DIRECTION_LEFT] 向左
     *
     * [DIRECTION_TOP] 向上
     *
     * [DIRECTION_RIGHT] 向右
     *
     * [DIRECTION_BOTTOM] 向下
     */
    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DIRECTION_LEFT, DIRECTION_TOP, DIRECTION_RIGHT, DIRECTION_BOTTOM)
    annotation class Direction {
        companion object {
            const val DIRECTION_LEFT = 0
            const val DIRECTION_TOP = 1
            const val DIRECTION_RIGHT = 2
            const val DIRECTION_BOTTOM = 3
        }
    }
}