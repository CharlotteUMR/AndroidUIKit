package com.jerry.uikit.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Spannable
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.LineHeightSpan
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.getSpans
import androidx.core.text.toSpannable
import androidx.core.text.toSpanned
import com.jerry.uikit.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 去除字高[TextView]
 *
 * 完全去除字高实现字号大小即控件高度，并且适配[CharacterStyle]
 *
 * @author Jerry
 */
open class ExcludePaddingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {
    private var isDebug = false
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1F
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ExcludePaddingTextView).let {
            isDebug = it.getBoolean(R.styleable.ExcludePaddingTextView_eptv_is_debug, isDebug)
            it.recycle()
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.isNullOrEmpty()) return
        if (includeFontPadding) return

        val spanText = text.toSpanned()
        if (spanText.getSpans<MinimumLineHeightSpan>().isEmpty()) {
            setText(minimumLineHeight(text))
        }
    }

    private fun minimumLineHeight(text: CharSequence): CharSequence {
        return text.toSpannable().apply {
            setSpan(
                MinimumLineHeightSpan(textSize, paint) { shrinkHeight ->
                    if (shadowRadius < shrinkHeight) {
                        setShadowLayer(shrinkHeight, 0F, 0F, 0x00000001)
                    }
                },
                0,
                length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isDebug) {
            for (i in 0 until layout.lineCount) {
                val lineLeft = layout.getLineLeft(i)
                val lineTop = layout.getLineTop(i).toFloat()
                val lineRight = layout.getLineRight(i)
                val lineBottom = layout.getLineBottom(i).toFloat()
                val lineBaseline = layout.getLineBaseline(i).toFloat()

                debugPaint.color = Color.RED
                canvas.drawRect(lineLeft, lineTop, lineRight, lineBottom, debugPaint)

                debugPaint.color = Color.BLUE
                canvas.drawLine(lineLeft, lineBaseline, lineRight, lineBaseline, debugPaint)
            }
        }
    }

    private class MinimumLineHeightSpan(
        private val textSize: Float,
        private val originPaint: TextPaint,
        private val afterChooseHeight: (Float) -> Unit
    ) : LineHeightSpan {
        override fun chooseHeight(
            text: CharSequence?,
            start: Int,
            end: Int,
            spanstartv: Int,
            lineHeight: Int,
            fm: Paint.FontMetricsInt?
        ) {
            text ?: return
            fm ?: return

            val originAscent = fm.ascent
            val originDescent = fm.descent
            val minimumLineHeight = calMinimumLineHeight(text.toSpannable(), start, end)
            // 此比例是以小米手机为准，经测试在其他手机上表现良好
            fm.ascent = -(minimumLineHeight * 0.9F).roundToInt()
            fm.descent = (minimumLineHeight * 0.1F).roundToInt()
            val shrinkHeight = max(
                abs(originAscent - fm.ascent),
                abs(originDescent - fm.descent)
            ).toFloat()
            afterChooseHeight.invoke(shrinkHeight)
        }

        private fun calMinimumLineHeight(spanText: Spannable, start: Int, end: Int): Float {
            var maxTextSize = textSize
            // 处理设置字体大小Span的情况
            val spans = spanText.getSpans(start, end, CharacterStyle::class.java)
            if (spans.isNotEmpty()) {
                // 取最大字号
                spans.forEach {
                    val tempPaint = TextPaint(originPaint)
                    it.updateDrawState(tempPaint)
                    maxTextSize = max(tempPaint.textSize, maxTextSize)
                }
            }
            return maxTextSize
        }
    }
}