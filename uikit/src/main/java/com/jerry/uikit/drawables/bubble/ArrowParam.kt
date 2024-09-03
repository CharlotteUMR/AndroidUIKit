package com.jerry.uikit.drawables.bubble

import android.graphics.Path
import android.graphics.drawable.Drawable
import androidx.annotation.IntDef
import com.jerry.uikit.drawables.bubble.Anchor.Companion.CENTER
import com.jerry.uikit.drawables.bubble.Anchor.Companion.END
import com.jerry.uikit.drawables.bubble.Anchor.Companion.START
import com.jerry.uikit.drawables.bubble.Side.Companion.BOTTOM
import com.jerry.uikit.drawables.bubble.Side.Companion.LEFT
import com.jerry.uikit.drawables.bubble.Side.Companion.RIGHT
import com.jerry.uikit.drawables.bubble.Side.Companion.TOP
import com.jerry.uikit.utils.PathBuilder
import com.jerry.uikit.utils.PathUtil

interface IArrowParam {
    val width: Float
    val height: Float
    val side: Int
    val anchor: Int
    val offset: Float
    val applyColor: Boolean
}

interface IPathArrowParam : IArrowParam {
    fun buildPath(): Path
}

class LineArrowParam(
    override val width: Float,
    override val height: Float,
    override val side: Int,
    override val anchor: Int,
    override val offset: Float = 0F,
) : IPathArrowParam {
    override val applyColor: Boolean
        get() = true

    private val pathBuilder = PathUtil.getPathBuilder()

    override fun buildPath(): Path {
        pathBuilder.reset()
        pathBuilder.moveTo(0F, height).lineTo(width / 2, 0F).lineTo(width, height).close()
        return pathBuilder.build()
    }
}

class DrawableArrowParam(
    override val width: Float,
    override val height: Float,
    val drawable: Drawable,
    override val side: Int,
    override val anchor: Int,
    override val offset: Float = 0F,
    override val applyColor: Boolean = true
) : IArrowParam

/**
 * 自定义箭头
 *
 * [buildPath] 自定义箭头路径
 */
class CustomPathArrowParam(
    override val width: Float,
    override val height: Float,
    override val side: Int,
    override val anchor: Int,
    override val offset: Float = 0F,
    private val buildPath: (width: Float, height: Float, pathBuilder: PathBuilder) -> Unit
) : IPathArrowParam {
    override val applyColor: Boolean
        get() = true

    private val pathBuilder = PathUtil.getPathBuilder()

    override fun buildPath(): Path {
        pathBuilder.reset()
        buildPath.invoke(width, height, pathBuilder)
        return pathBuilder.build()
    }
}

/**
 * 箭头锚点
 *
 * [START] 上[Side.TOP]下[Side.BOTTOM]边为左，左[Side.LEFT]右[Side.RIGHT]边为上
 *
 * [CENTER] 边的中间
 *
 * [END] 上[Side.TOP]下[Side.BOTTOM]边为右，左[Side.LEFT]右[Side.RIGHT]边为下
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(START, CENTER, END)
annotation class Anchor {
    companion object {
        const val START = 0
        const val CENTER = 1
        const val END = 2
    }
}

/**
 * 箭头所在的边
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(LEFT, TOP, RIGHT, BOTTOM)
annotation class Side {
    companion object {
        const val LEFT = 0
        const val TOP = 1
        const val RIGHT = 2
        const val BOTTOM = 3
    }
}

