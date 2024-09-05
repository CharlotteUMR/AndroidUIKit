package com.jerry.uikit.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatImageView
import com.jerry.uikit.R
import com.jerry.uikit.entities.Quaternion
import com.jerry.uikit.utils.PathUtil
import com.jerry.uikit.utils.RectUtil
import kotlin.math.min

/**
 * 圆角图片控件
 *
 * @author Jerry
 */
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {
    /**
     * 各个角的圆角大小（左上 - 右上 - 右下 - 左下）
     */
    private var cornerSize: Quaternion<Float>? = null

    /**
     * 路径工具
     */
    private val pathBuilder = PathUtil.getPathBuilder()

    /**
     * 矩形工具
     */
    private val rectBuilder = RectUtil.getRectFBuilder()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RoundImageView).let {
            val all = it.getDimension(R.styleable.RoundImageView_riv_corner_size, 0F)
            val leftTop =
                it.getDimension(R.styleable.RoundImageView_riv_left_top_corner_size, -1F)
            val rightTop =
                it.getDimension(R.styleable.RoundImageView_riv_right_top_corner_size, -1F)
            val rightBottom =
                it.getDimension(R.styleable.RoundImageView_riv_right_bottom_corner_size, -1F)
            val leftBottom =
                it.getDimension(R.styleable.RoundImageView_riv_left_bottom_corner_size, -1F)
            cornerSize = Quaternion(
                if (leftTop >= 0) leftTop else all,
                if (rightTop >= 0) rightTop else all,
                if (rightBottom >= 0) rightBottom else all,
                if (leftBottom >= 0) leftBottom else all
            )
            it.recycle()
        }
    }

    @MainThread
    fun setCornerSize(cornerSize: Quaternion<Float>) {
        if (this.cornerSize != cornerSize) {
            this.cornerSize = cornerSize
            invalidate()
        }
    }

    @MainThread
    fun setCornerSize(all: Float) {
        setCornerSize(all, all, all, all)
    }

    @MainThread
    fun setCornerSize(leftTop: Float, rightTop: Float, rightBottom: Float, leftBottom: Float) {
        if (cornerSize?.value1 != leftTop
            || cornerSize?.value2 != rightTop
            || cornerSize?.value3 != rightBottom
            || cornerSize?.value4 != leftBottom
        ) {
            cornerSize = Quaternion(leftTop, rightTop, rightBottom, leftBottom)
            invalidate()
        }
    }

    override fun draw(canvas: Canvas) {
        val clipPath = buildPath()
        if (clipPath != null) {
            // 裁切圆角
            canvas.clipPath(clipPath)
        }
        super.draw(canvas)
    }

    private fun buildPath(): Path? {
        val shortSide = min(width, height)
        var (leftTop, rightTop, rightBottom, leftBottom) = cornerSize ?: return null
        leftTop = min(leftTop, shortSide / 2F)
        rightTop = min(rightTop, shortSide / 2F)
        rightBottom = min(rightBottom, shortSide / 2F)
        leftBottom = min(leftBottom, shortSide / 2F)
        return pathBuilder.reset()
            .moveTo(0F, leftTop)
            .arcTo(
                rectBuilder.set(
                    0F,
                    0F,
                    leftTop * 2,
                    leftTop * 2
                ).build(),
                180F,
                90F
            )
            .arcTo(
                rectBuilder.set(
                    width - rightTop * 2,
                    0F,
                    width.toFloat(),
                    rightTop * 2
                ).build(),
                270F,
                90F
            )
            .arcTo(
                rectBuilder.set(
                    width - rightBottom * 2,
                    height - rightBottom * 2,
                    width.toFloat(),
                    height.toFloat()
                ).build(),
                0F,
                90F
            )
            .arcTo(
                rectBuilder.set(
                    0F,
                    height - leftBottom * 2,
                    leftBottom * 2,
                    height.toFloat()
                ).build(),
                90F,
                90F
            )
            .close()
            .build()
    }
}