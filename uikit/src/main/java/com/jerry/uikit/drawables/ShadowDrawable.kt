package com.jerry.uikit.drawables

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.MainThread
import androidx.core.graphics.withSave
import com.jerry.uikit.entities.Offset
import com.jerry.uikit.entities.Quaternion
import com.jerry.uikit.extensions.alpha
import com.jerry.uikit.utils.PathUtil
import com.jerry.uikit.utils.RectUtil
import kotlin.math.abs
import kotlin.math.max

/**
 * 阴影[Drawable]
 *
 * @author Jerry
 */
class ShadowDrawable(private val builder: Builder) : Drawable() {
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val clipRectBuilder = RectUtil.getRectFBuilder()
    private val drawRectBuilder = RectUtil.getRectFBuilder()
    private val pathBuilder = PathUtil.getPathBuilder()

    private var overrideColor: Int? = null
    private var overrideAlpha: Int = 255

    init {
        setupShadow()
    }

    private fun setupShadow() {
        val blurDegree = builder.shadowBlurDegree
        val offsetParam = builder.offsetParam
        if (blurDegree > 0) {
            val realBlurDegree = blurDegree + max(abs(offsetParam.x), abs(offsetParam.y))
            shadowPaint.maskFilter = BlurMaskFilter(realBlurDegree, BlurMaskFilter.Blur.OUTER)
        }
    }

    override fun draw(canvas: Canvas) {
        shadowPaint.color = (overrideColor ?: builder.shadowColor).alpha(overrideAlpha / 255F)
        foregroundPaint.color = builder.foregroundColor

        val blurDegree = builder.shadowBlurDegree
        val corner = builder.cornerParam
        val offset = builder.offsetParam
        val maxOffset = max(abs(offset.x), abs(offset.y))

        val clipRect = if (builder.drawOutside) {
            clipRectBuilder.set(bounds).build()
        } else {
            clipRectBuilder.set(bounds)
                .inset(blurDegree, blurDegree)
                .offset(-offset.x, -offset.y)
                .build()
        }

        val drawRect = if (builder.drawOutside) {
            drawRectBuilder.set(bounds)
                .inset(maxOffset, maxOffset)
                .offset(offset.x, offset.y)
                .build()
        } else {
            drawRectBuilder.set(bounds)
                .inset(blurDegree, blurDegree)
                .inset(maxOffset, maxOffset)
                .build()
        }

        when {
            corner.equals(0F) -> {
                canvas.withSave {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        canvas.clipOutRect(clipRect)
                    } else {
                        canvas.clipRect(clipRect, Region.Op.DIFFERENCE)
                    }

                    canvas.drawRect(drawRect, shadowPaint)
                }

                if (builder.foregroundColor != Color.TRANSPARENT) {
                    canvas.drawRect(clipRect, foregroundPaint)
                }
            }
            corner.isAllValueSame() -> {
                val clipPath = pathBuilder.reset().addRoundRect(
                    clipRect,
                    corner.value1,
                    corner.value1,
                    Path.Direction.CW
                ).build()

                canvas.withSave {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        canvas.clipOutPath(clipPath)
                    } else {
                        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
                    }

                    canvas.drawRoundRect(drawRect, corner.value1, corner.value1, shadowPaint)
                }

                if (builder.foregroundColor != Color.TRANSPARENT) {
                    canvas.drawPath(clipPath, foregroundPaint)
                }
            }
            else -> {
                val clipPath = pathBuilder.reset().addRoundRect(
                    clipRect,
                    floatArrayOf(
                        corner.value1,
                        corner.value1,
                        corner.value2,
                        corner.value2,
                        corner.value3,
                        corner.value3,
                        corner.value4,
                        corner.value4
                    ),
                    Path.Direction.CW
                ).build()

                canvas.withSave {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        canvas.clipOutPath(clipPath)
                    } else {
                        canvas.clipPath(clipPath, Region.Op.DIFFERENCE)
                    }

                    val drawPath = pathBuilder.reset().addRoundRect(
                        drawRect,
                        floatArrayOf(
                            corner.value1,
                            corner.value1,
                            corner.value2,
                            corner.value2,
                            corner.value3,
                            corner.value3,
                            corner.value4,
                            corner.value4
                        ),
                        Path.Direction.CW
                    ).build()
                    canvas.drawPath(drawPath, shadowPaint)
                }

                if (builder.foregroundColor != Color.TRANSPARENT) {
                    canvas.drawPath(clipPath, foregroundPaint)
                }
            }
        }
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
        if (shadowPaint.colorFilter != colorFilter) {
            shadowPaint.colorFilter = colorFilter
            invalidateSelf()
        }
    }

    override fun getColorFilter(): ColorFilter? = shadowPaint.colorFilter

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    data class Builder(
        internal val shadowColor: Int,
        internal val shadowBlurDegree: Float
    ) {
        internal var foregroundColor: Int = Color.TRANSPARENT
        internal var cornerParam: Quaternion<Float> = Quaternion(0F)
        internal var offsetParam: Offset<Float> = Offset(0F)

        /**
         * 是否可以绘制到外部
         */
        internal var drawOutside: Boolean = false

        fun foreground(color: Int): Builder {
            foregroundColor = color
            return this
        }

        fun corner(radius: Quaternion<Float>): Builder {
            cornerParam = radius
            return this
        }

        fun offset(offset: Offset<Float>): Builder {
            offsetParam = offset
            return this
        }

        /**
         * 需要设置clipChildren为false才能生效
         */
        fun drawOutside(): Builder {
            this.drawOutside = true
            return this
        }

        fun build(): ShadowDrawable {
            return ShadowDrawable(this)
        }
    }
}