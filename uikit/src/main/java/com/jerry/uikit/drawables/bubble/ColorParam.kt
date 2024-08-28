package com.jerry.uikit.drawables.bubble

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.CUSTOMIZE
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.LB_TO_RT_MIDPERPENDICULAR
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.LEFT_BOTTOM_TO_RIGHT_TOP
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.LEFT_TOP_TO_RIGHT_BOTTOM
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.LEFT_TO_RIGHT
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.LT_TO_RB_MIDPERPENDICULAR
import com.jerry.uikit.drawables.bubble.GradientColorParam.GradientDirection.Companion.TOP_TO_BOTTOM
import kotlin.math.max
import kotlin.math.min
import kotlin.math.tan

interface IColorParam

interface IShaderColorParam : IColorParam {
    val shader: Shader?
}

/**
 * 纯色
 *
 * [value] 颜色
 */
class SolidColorParam(var color: Int) : IColorParam

/**
 * 渐变色
 *
 * [colorArray] 颜色序列
 *
 * [posArray] 颜色位置序列（0～1）（为空则颜色均匀分布）
 *
 * [gradientDirection] 方向[GradientDirection]
 *
 * [angle] 角度（只有[gradientDirection]=[CUSTOMIZE]才能生效）
 */
class GradientColorParam internal constructor(
    internal val colorArray: IntArray = intArrayOf(),
    internal val posArray: FloatArray? = null,
    @GradientDirection internal val gradientDirection: Int = CUSTOMIZE,
    @IntRange(from = 0, to = 90) internal val angle: Int = 0,
) : IShaderColorParam {
    companion object {
        fun customizeAngle(
            colorArray: IntArray,
            posArray: FloatArray? = null,
            angle: Int
        ) = GradientColorParam(colorArray, posArray, CUSTOMIZE, angle)

        fun topToBottom(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, TOP_TO_BOTTOM)

        fun leftToRight(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, LEFT_TO_RIGHT)

        fun leftTopToRightBottom(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, LEFT_TOP_TO_RIGHT_BOTTOM)

        fun leftBottomToRightTop(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, LEFT_BOTTOM_TO_RIGHT_TOP)

        fun ltToRbMidperpendicular(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, LT_TO_RB_MIDPERPENDICULAR)

        fun lbToRtMidperpendicular(
            colorArray: IntArray,
            posArray: FloatArray? = null
        ) = GradientColorParam(colorArray, posArray, LB_TO_RT_MIDPERPENDICULAR)
    }

    override var shader: Shader? = null

    fun buildShader(width: Float, height: Float) {
        shader = when (gradientDirection) {
            TOP_TO_BOTTOM -> {
                LinearGradient(
                    0F,
                    0F,
                    0F,
                    height,
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            LEFT_TO_RIGHT -> {
                LinearGradient(
                    0F,
                    0F,
                    width,
                    0F,
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            LEFT_TOP_TO_RIGHT_BOTTOM -> {
                LinearGradient(
                    0F,
                    0F,
                    width,
                    height,
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            LEFT_BOTTOM_TO_RIGHT_TOP -> {
                LinearGradient(
                    0F,
                    height,
                    width,
                    0F,
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            LT_TO_RB_MIDPERPENDICULAR -> {
                LinearGradient(
                    width * (width * width - height * height) / (2 * (height * height + width * width)),
                    height * (height * height + 3 * width * width) / (2 * (height * height + width * width)),
                    width * (3 * height * height + width * width) / (2 * (height * height + width * width)),
                    height * (height * height - width * width) / (2 * (height * height + width * width)),
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            LB_TO_RT_MIDPERPENDICULAR -> {
                LinearGradient(
                    width * (width * width - height * height) / (2 * (height * height + width * width)),
                    height * (height * height - width * width) / (2 * (height * height + width * width)),
                    width * (3 * height * height + width * width) / (2 * (height * height + width * width)),
                    height * (height * height + 3 * width * width) / (2 * (height * height + width * width)),
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
            else -> {
                //0 ~ 90
                val angle = max(0, min(90, angle))
                val rad = (angle * Math.PI / 180f).toFloat()
                val x1: Float
                val y1: Float
                if (angle <= 45) {
                    x1 = width
                    y1 = ((width * tan(rad.toDouble())).toFloat())
                } else {
                    x1 = ((height / tan(Math.PI - rad)).toFloat())
                    y1 = height
                }
                LinearGradient(
                    0f,
                    0f,
                    x1,
                    y1,
                    colorArray,
                    posArray,
                    Shader.TileMode.MIRROR
                )
            }
        }
    }

    /**
     * 渐变方向
     *
     * [LT_TO_RB_MIDPERPENDICULAR] 左上到右下对角线的中垂线
     *
     * [LB_TO_RT_MIDPERPENDICULAR] 左下到右上对角线的中垂线
     */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        CUSTOMIZE,
        TOP_TO_BOTTOM,
        LEFT_TO_RIGHT,
        LEFT_TOP_TO_RIGHT_BOTTOM,
        LEFT_BOTTOM_TO_RIGHT_TOP,
        LT_TO_RB_MIDPERPENDICULAR,
        LB_TO_RT_MIDPERPENDICULAR
    )
    annotation class GradientDirection {
        companion object {
            const val CUSTOMIZE = 0
            const val TOP_TO_BOTTOM = 1
            const val LEFT_TO_RIGHT = 2
            const val LEFT_TOP_TO_RIGHT_BOTTOM = 3
            const val LEFT_BOTTOM_TO_RIGHT_TOP = 4
            const val LT_TO_RB_MIDPERPENDICULAR = 5
            const val LB_TO_RT_MIDPERPENDICULAR = 6
        }
    }
}

/**
 * 着色器[Shader]
 */
class ShaderColorParam(override val shader: Shader) : IShaderColorParam