package com.jerry.uikit.utils

import android.graphics.Path
import android.graphics.RectF

/**
 * 路径工具类
 *
 * @author Jerry
 */
object PathUtil {
    fun getPathBuilder(path: Path? = null): PathBuilder {
        return PathBuilder(path)
    }
}

class PathBuilder(path: Path? = null) {
    private val realPath: Path = if (path == null) Path() else Path(path)

    fun set(path: Path): PathBuilder {
        realPath.set(path)
        return this
    }

    fun reset(): PathBuilder {
        realPath.reset()
        return this
    }

    fun moveTo(x: Float, y: Float): PathBuilder {
        realPath.moveTo(x, y)
        return this
    }

    fun lineTo(x: Float, y: Float): PathBuilder {
        realPath.lineTo(x, y)
        return this
    }

    fun quadTo(x1: Float, y1: Float, x2: Float, y2: Float): PathBuilder {
        realPath.quadTo(x1, y1, x2, y2)
        return this
    }

    fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): PathBuilder {
        realPath.cubicTo(x1, y1, x2, y2, x3, y3)
        return this
    }

    fun arcTo(
        oval: RectF,
        startAngle: Float,
        sweepAngle: Float,
        forceMoveTo: Boolean = false
    ): PathBuilder {
        realPath.arcTo(oval, startAngle, sweepAngle, forceMoveTo)
        return this
    }

    fun addRect(rectF: RectF, dir: Path.Direction): PathBuilder {
        realPath.addRect(rectF, dir)
        return this
    }

    fun addRect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        dir: Path.Direction
    ): PathBuilder {
        realPath.addRect(left, top, right, bottom, dir)
        return this
    }

    fun addRoundRect(
        rectF: RectF,
        rx: Float,
        ry: Float,
        dir: Path.Direction
    ): PathBuilder {
        return if (rx == 0F || ry == 0F) {
            addRect(rectF, dir)
        } else {
            realPath.addRoundRect(rectF, rx, ry, dir)
            this
        }
    }

    fun addRoundRect(
        rectF: RectF,
        radii: FloatArray,
        dir: Path.Direction
    ): PathBuilder {
        return if (radii.size == 8) {
            realPath.addRoundRect(rectF, radii, dir)
            this
        } else {
            addRect(rectF, dir)
        }
    }

    fun addOval(oval: RectF, dir: Path.Direction): PathBuilder {
        realPath.addOval(oval, dir)
        return this
    }

    fun addOval(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        dir: Path.Direction
    ): PathBuilder {
        realPath.addOval(left, top, right, bottom, dir)
        return this
    }

    fun op(other: Path, op: Path.Op): PathBuilder {
        realPath.op(other, op)
        return this
    }

    fun close(): PathBuilder {
        realPath.close()
        return this
    }

    fun build(): Path {
        return realPath
    }
}