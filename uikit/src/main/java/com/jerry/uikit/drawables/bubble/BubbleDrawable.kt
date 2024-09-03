package com.jerry.uikit.drawables.bubble

import android.graphics.*
import android.graphics.drawable.Drawable
import com.jerry.uikit.entities.Quaternion
import com.jerry.uikit.extensions.alpha
import com.jerry.uikit.utils.PathUtil
import com.jerry.uikit.utils.RectUtil
import kotlin.math.min

/**
 * 气泡[Drawable]
 *
 * 圆角矩形 + 箭头 + 边框
 *
 * @author p_jruixu
 */
class BubbleDrawable(private val builder: Builder) : Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mainRect = RectF()
    private val cornerRectBuilder = RectUtil.getRectFBuilder()
    private val arrowRectBuilder = RectUtil.getRectFBuilder()

    private val mainPathBuilder = PathUtil.getPathBuilder()

    private val matrix = Matrix()

    private var overrideColor: Int? = null
    private var overrideAlpha: Int = 255

    override fun draw(canvas: Canvas) {
        // 气泡主体
        mainRect.set(bounds)

        // 将内容区域往内缩，为箭头留出位置
        val arrow = builder.arrow
        if (arrow != null) {
            when (arrow.side) {
                Side.LEFT -> mainRect.left += arrow.height
                Side.TOP -> mainRect.top += arrow.height
                Side.RIGHT -> mainRect.right -= arrow.height
                Side.BOTTOM -> mainRect.bottom -= arrow.height
            }
        }

        // 根据线宽调整控件和内容区域
        val stroke = builder.stroke
        if (stroke != null) {
            val halfStrokeWidthPx = stroke.widthPx / 2F
            mainRect.inset(halfStrokeWidthPx, halfStrokeWidthPx)
        }

        // 圆角
        val corner = builder.corner
        val reasonableCorner = if (corner != null && corner.equals(0).not()) {
            // 约束圆角半径不能超过宽高最小值的一半
            val maxRadius = min(mainRect.width() / 2, mainRect.height() / 2)
            Quaternion(
                min(corner.value1, maxRadius),
                min(corner.value2, maxRadius),
                min(corner.value3, maxRadius),
                min(corner.value4, maxRadius)
            )
        } else {
            Quaternion(0F)
        }

        // 构建气泡主体
        val mainPath = buildRectPath(mainRect, reasonableCorner)

        // 构建箭头
        val arrowPath = if (arrow is IPathArrowParam) {
            buildArrowPath(arrow, reasonableCorner)
        } else null

        val mainColor = builder.color
        // 保存画布为后面染色准备
        val saveId = if (mainColor is IShaderColorParam) {
            canvas.saveLayer(0F, 0F, bounds.width().toFloat(), bounds.height().toFloat(), paint)
        } else null

        if (arrowPath != null) {
            // 有箭头路径时，将主体和箭头合并
            mainPath.op(arrowPath, Path.Op.UNION)
        } else if (arrow is DrawableArrowParam) {
            // 箭头为Drawable时，单独绘制箭头
            drawArrowDrawable(canvas, arrow, reasonableCorner)
        }

        paint.color = when (mainColor) {
            is SolidColorParam -> mainColor.color.alpha(overrideAlpha / 255F)
            else -> Color.BLACK.alpha(overrideAlpha / 255F)
        }
        paint.style = Paint.Style.FILL
        canvas.drawPath(mainPath, paint)

        if (mainColor is IShaderColorParam) {
            // 着色器
            paint.shader = mainColor.shader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        if (arrow != null && arrow.applyColor) {
            // 有箭头并且需要着色，全局染色
            canvas.drawRect(bounds, paint)
        } else {
            // 没有箭头或者不需要着色，只染主体
            canvas.drawRect(mainRect, paint)
        }
        // 还原画笔状态
        paint.shader = null
        paint.xfermode = null
        if (saveId != null) {
            // 还原画布
            canvas.restoreToCount(saveId)
        }

        if (stroke != null && arrow is IPathArrowParam) {
            // 需要边线且没有箭头图片时才绘制边线
            when (val strokeColor = stroke.color) {
                is IShaderColorParam -> {
                    paint.color = 0xFF000000.toInt().alpha(overrideAlpha / 255F)
                    paint.shader = strokeColor.shader
                }
                is SolidColorParam -> {
                    paint.color = strokeColor.color.alpha(overrideAlpha / 255F)
                }
            }
            paint.strokeWidth = stroke.widthPx.toFloat()
            paint.style = Paint.Style.STROKE
            canvas.drawPath(mainPath, paint)
        }
    }

    private fun drawArrowDrawable(
        canvas: Canvas,
        arrow: DrawableArrowParam,
        corner: Quaternion<Float>
    ) {
        val arrowRect = getArrowRect(arrow, corner)
        arrow.drawable.setBounds(0, 0, arrow.width.toInt(), arrow.height.toInt())
        when (arrow.side) {
            Side.LEFT -> {
                canvas.save()
                canvas.translate(arrowRect.left, arrowRect.bottom)
                canvas.rotate(-90F)
                arrow.drawable.draw(canvas)
                canvas.restore()
            }
            Side.TOP -> {
                canvas.save()
                canvas.translate(arrowRect.left, arrowRect.top)
                arrow.drawable.draw(canvas)
                canvas.restore()
            }
            Side.RIGHT -> {
                canvas.save()
                canvas.translate(arrowRect.right, arrowRect.top)
                canvas.rotate(90F)
                arrow.drawable.draw(canvas)
                canvas.restore()
            }
            Side.BOTTOM -> {
                canvas.save()
                canvas.translate(arrowRect.right, arrowRect.bottom)
                canvas.rotate(180F)
                arrow.drawable.draw(canvas)
                canvas.restore()
            }
        }
    }

    private fun buildArrowPath(arrow: IPathArrowParam, corner: Quaternion<Float>): Path {
        val arrowRect = getArrowRect(arrow, corner)
        val arrowPath = arrow.buildPath()
        // 箭头
        matrix.reset()
        when (arrow.side) {
            Side.LEFT -> {
                matrix.setTranslate(arrowRect.left, arrowRect.bottom)
                matrix.preRotate(-90F)
            }
            Side.TOP -> {
                matrix.setTranslate(arrowRect.left, arrowRect.top)
            }
            Side.RIGHT -> {
                matrix.setTranslate(arrowRect.right, arrowRect.top)
                matrix.preRotate(90F)
            }
            Side.BOTTOM -> {
                matrix.setTranslate(arrowRect.right, arrowRect.bottom)
                matrix.preRotate(180F)
            }
        }
        arrowPath.transform(matrix)
        return arrowPath
    }

    private fun getArrowRect(arrow: IArrowParam, corner: Quaternion<Float>): RectF {
        when (arrow.side) {
            Side.LEFT -> {
                val arrowStart = sideOffset(
                    mainRect.top,
                    corner.value1,
                    mainRect.bottom,
                    corner.value4,
                    arrow.anchor,
                    arrow.offset,
                    arrow.width
                )
                return arrowRectBuilder.set(
                    mainRect.left - arrow.height,
                    arrowStart,
                    mainRect.left,
                    arrowStart + arrow.width
                ).build()
            }
            Side.TOP -> {
                val arrowStart = sideOffset(
                    mainRect.left,
                    corner.value1,
                    mainRect.right,
                    corner.value2,
                    arrow.anchor,
                    arrow.offset,
                    arrow.width
                )
                return arrowRectBuilder.set(
                    arrowStart,
                    mainRect.top - arrow.height,
                    arrowStart + arrow.width,
                    mainRect.top
                ).build()
            }
            Side.RIGHT -> {
                val arrowStart = sideOffset(
                    mainRect.top,
                    corner.value2,
                    mainRect.bottom,
                    corner.value3,
                    arrow.anchor,
                    arrow.offset,
                    arrow.width
                )
                return arrowRectBuilder.set(
                    mainRect.right,
                    arrowStart,
                    mainRect.right + arrow.height,
                    arrowStart + arrow.width
                ).build()
            }
            Side.BOTTOM -> {
                val arrowStart = sideOffset(
                    mainRect.left,
                    corner.value4,
                    mainRect.right,
                    corner.value3,
                    arrow.anchor,
                    arrow.offset,
                    arrow.width
                )
                return arrowRectBuilder.set(
                    arrowStart,
                    mainRect.bottom,
                    arrowStart + arrow.width,
                    mainRect.bottom + arrow.height
                ).build()
            }
            else -> return RectF()
        }
    }

    /**
     * 构建气泡主体
     *
     * [corner] 圆角半径
     */
    private fun buildRectPath(rect: RectF, corner: Quaternion<Float>): Path {
        mainPathBuilder.reset()
        return if (corner.equals(0)) {
            mainPathBuilder.addRect(rect, Path.Direction.CW)
        } else {
            mainPathBuilder.arcTo(
                cornerRectBuilder.set(
                    rect.left,
                    rect.top,
                    rect.left + corner.value1 * 2,
                    rect.top + corner.value1 * 2
                ).sort().build(), 180F, 90F, true
            ).arcTo(
                cornerRectBuilder.set(
                    rect.right,
                    rect.top,
                    rect.right - corner.value2 * 2,
                    rect.top + corner.value2 * 2
                ).sort().build(), -90F, 90F
            ).arcTo(
                cornerRectBuilder.set(
                    rect.right,
                    rect.bottom,
                    rect.right - corner.value3 * 2,
                    rect.bottom - corner.value3 * 2
                ).sort().build(), 0F, 90F
            ).arcTo(
                cornerRectBuilder.set(
                    rect.left,
                    rect.bottom,
                    rect.left + corner.value4 * 2,
                    rect.bottom - corner.value4 * 2
                ).sort().build(), 90F, 90F
            ).close()
        }.build()
    }

    /**
     * 边偏移
     *
     * [start] 边开始点
     *
     * [startDeadZone] 边开始点无效区
     *
     * [end] 边结束点
     *
     * [endDeadZone] 边结束点无效区
     *
     * [anchor] 锚点
     *
     * [offset] 偏移
     *
     * [width] 箭头宽度
     */
    private fun sideOffset(
        start: Float,
        startDeadZone: Float,
        end: Float,
        endDeadZone: Float,
        @Anchor anchor: Int,
        offset: Float,
        width: Float
    ): Float {
        // 安全起点
        val safeStart = start + startDeadZone
        // 安全终点
        val safeEnd = end - endDeadZone
        // 保证其能在安全区域内
        return when (anchor) {
            Anchor.START -> when {
                start + offset + width > safeEnd -> safeEnd - width
                start + offset < safeStart -> safeStart
                else -> start + offset
            }
            Anchor.CENTER -> {
                when {
                    (width > safeEnd - safeStart) or (offset == 0F) -> {
                        (safeStart + safeEnd - width) / 2
                    }
                    (offset > 0) and ((safeStart + safeEnd + width) / 2 + offset > safeEnd) -> {
                        safeEnd - width
                    }
                    (offset < 0) and ((safeStart + safeEnd - width) / 2 + offset < safeStart) -> {
                        safeStart
                    }
                    else -> {
                        (safeStart + safeEnd - width) / 2 + offset
                    }
                }
            }
            Anchor.END -> when {
                end - offset - width < safeStart -> safeStart
                end - offset > safeEnd -> safeEnd - width
                else -> end - offset - width
            }
            else -> safeStart
        }
    }

    override fun setAlpha(alpha: Int) {
        if (overrideAlpha != alpha) {
            this.overrideAlpha = alpha
            val arrow = builder.arrow
            if (arrow is DrawableArrowParam) {
                arrow.drawable.alpha = alpha
            }
            invalidateSelf()
        }
    }

    override fun getAlpha() = overrideAlpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (paint.colorFilter != colorFilter) {
            paint.colorFilter = colorFilter
            val arrow = builder.arrow
            if (arrow is DrawableArrowParam) {
                arrow.drawable.colorFilter = colorFilter
            }
            invalidateSelf()
        }
    }

    override fun getColorFilter(): ColorFilter? = paint.colorFilter

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        val color = builder.color
        if (color is IShaderColorParam) {
            color.buildShader(bounds.width().toFloat(), bounds.height().toFloat())
        }

        val strokeColor = builder.stroke?.color
        if (strokeColor is IShaderColorParam) {
            strokeColor.buildShader(bounds.width().toFloat(), bounds.height().toFloat())
        }
    }

    override fun getConstantState(): ConstantState {
        return BubbleState(builder)
    }

    private class BubbleState(private val builder: Builder) : ConstantState() {
        override fun newDrawable(): Drawable {
            return BubbleDrawable(builder)
        }

        override fun getChangingConfigurations() = 0
    }

    data class Builder(internal val color: IColorParam) {
        internal var corner: Quaternion<Float>? = null
        internal var arrow: IArrowParam? = null
        internal var stroke: StrokeParam? = null

        constructor(color: Int) : this(SolidColorParam(color))

        fun corner(corner: Quaternion<Float>): Builder {
            this.corner = corner
            return this
        }

        fun arrow(arrow: IArrowParam): Builder {
            this.arrow = arrow
            return this
        }

        fun stroke(stroke: StrokeParam): Builder {
            this.stroke = stroke
            return this
        }

        fun build(): BubbleDrawable {
            return BubbleDrawable(this)
        }
    }

    /**
     * [widthPx] 线条宽度
     *
     * [color] 线条颜色
     */
    data class StrokeParam(
        internal val widthPx: Int = 0,
        internal val color: IColorParam
    ) {
        constructor(widthPx: Int, color: Int) : this(
            widthPx,
            SolidColorParam(color)
        )
    }
}