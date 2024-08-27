package com.jerry.uikit.utils

import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.SizeF

object RectUtil {
    fun getRectFBuilder(initialRectF: RectF? = null): RectFBuilder {
        return RectFBuilder(initialRectF)
    }
}

class RectFBuilder(rectF: RectF? = null) {
    private val realRectF = if (rectF == null) RectF() else RectF(rectF)

    fun setEmpty(): RectFBuilder {
        realRectF.setEmpty()
        return this
    }

    fun set(left: Float, top: Float, right: Float, bottom: Float): RectFBuilder {
        realRectF.set(left, top, right, bottom)
        return this
    }

    fun set(rectF: RectF): RectFBuilder {
        realRectF.set(rectF)
        return this
    }

    fun set(rect: Rect): RectFBuilder {
        realRectF.set(rect)
        return this
    }

    fun set(point: PointF, size: SizeF): RectFBuilder {
        realRectF.set(point.x, point.y, point.x + size.width, point.y + size.height)
        return this
    }

    fun offset(dx: Float, dy: Float): RectFBuilder {
        realRectF.offset(dx, dy)
        return this
    }

    fun offsetTo(newLeft: Float, newTop: Float): RectFBuilder {
        realRectF.offsetTo(newLeft, newTop)
        return this
    }

    fun inset(dx: Float, dy: Float): RectFBuilder {
        realRectF.inset(dx, dy)
        return this
    }

    fun union(left: Float, top: Float, right: Float, bottom: Float): RectFBuilder {
        realRectF.union(left, top, right, bottom)
        return this
    }

    fun union(rectF: RectF): RectFBuilder {
        realRectF.union(rectF)
        return this
    }

    fun union(x: Float, y: Float): RectFBuilder {
        realRectF.union(x, y)
        return this
    }

    fun sort(): RectFBuilder {
        realRectF.sort()
        return this
    }

    fun build(): RectF {
        return realRectF
    }
}