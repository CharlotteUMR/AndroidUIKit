package com.jerry.androiduikit.examples

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.SizeF
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.R
import com.jerry.androiduikit.databinding.ActivityArrowDrawableExampleBinding
import com.jerry.uikit.drawable.ArrowDrawable
import com.jerry.uikit.drawable.ArrowDrawable.Arrow
import com.jerry.uikit.drawable.ArrowDrawable.Direction
import com.jerry.uikit.utils.RectUtil

class ArrowDrawableExampleActivity : AppCompatActivity() {
    companion object {
        const val ARROW_RATIO = 2 / 5F
        val COLOR_ARRAY = arrayOf(Color.RED, Color.GREEN, Color.BLACK, Color.BLUE, Color.CYAN)
    }

    private lateinit var binding: ActivityArrowDrawableExampleBinding

    private lateinit var viewPreview: View

    private lateinit var btnAddLeft: Button
    private lateinit var btnAddUp: Button
    private lateinit var btnAddRight: Button
    private lateinit var btnAddDown: Button

    private lateinit var btnRemove: Button
    private lateinit var btnClear: Button

    private val arrowDrawable = ArrowDrawable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrow_drawable_example)

        binding = ActivityArrowDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPreview = binding.arrowDrawableExampleActivityViewPreview
        viewPreview.background = arrowDrawable

        btnAddLeft = binding.arrowDrawableExampleActivityBtnAddLeft
        btnAddLeft.setOnClickListener {
            addArrow(Direction.DIRECTION_LEFT)
        }
        btnAddUp = binding.arrowDrawableExampleActivityBtnAddUp
        btnAddUp.setOnClickListener {
            addArrow(Direction.DIRECTION_TOP)
        }
        btnAddRight = binding.arrowDrawableExampleActivityBtnAddRight
        btnAddRight.setOnClickListener {
            addArrow(Direction.DIRECTION_RIGHT)
        }
        btnAddDown = binding.arrowDrawableExampleActivityBtnAddDown
        btnAddDown.setOnClickListener {
            addArrow(Direction.DIRECTION_BOTTOM)
        }

        btnRemove = binding.arrowDrawableExampleActivityBtnRemove
        btnRemove.setOnClickListener {
            removeArrow()
        }
        btnClear = binding.arrowDrawableExampleActivityBtnClear
        btnClear.setOnClickListener {
            removeAllArrow()
        }

        setArrowDirection(null)
    }

    private fun removeAllArrow() {
        arrowDrawable.removeAllArrow()
        setArrowDirection(null)
    }

    private fun removeArrow() {
        val arrowList = arrowDrawable.getArrowList()
        val targetCount = arrowList.size - 1
        if (targetCount <= 0) {
            arrowDrawable.removeAllArrow()
            setArrowDirection(null)
        } else {
            updateArrow(arrowList.first().direction, targetCount)
        }
    }

    private fun addArrow(direction: Int) {
        updateArrow(direction, arrowDrawable.getArrowList().size + 1)
    }

    private fun updateArrow(direction: Int, arrowCount: Int) {
        val curArrowCount = arrowDrawable.getArrowList().size
        if (curArrowCount == 0) {
            setArrowDirection(direction)
        }
        val width = viewPreview.width - viewPreview.paddingStart - viewPreview.paddingEnd
        val height = viewPreview.height - viewPreview.paddingTop - viewPreview.paddingBottom

        val left = viewPreview.paddingStart
        val top = viewPreview.paddingTop

        val isVertical =
            direction == Direction.DIRECTION_TOP || direction == Direction.DIRECTION_BOTTOM
        val areaWidth = if (isVertical.not()) {
            width.toFloat() / arrowCount
        } else {
            width.toFloat()
        }
        val areaHeight = if (isVertical) {
            height.toFloat() / arrowCount
        } else {
            height.toFloat()
        }

        val ratio = if (isVertical) {
            areaHeight / areaWidth
        } else {
            areaWidth / areaHeight
        }

        val arrowSize = SizeF(
            if (isVertical) {
                if (ratio > ARROW_RATIO) areaWidth else areaHeight / ARROW_RATIO
            } else {
                if (ratio > ARROW_RATIO) areaHeight * ARROW_RATIO else areaWidth
            }, if (isVertical) {
                if (ratio > ARROW_RATIO) areaWidth * ARROW_RATIO else areaHeight
            } else {
                if (ratio > ARROW_RATIO) areaHeight else areaWidth / ARROW_RATIO
            }
        )
        arrowDrawable.removeAllArrow()
        val arrowList = mutableListOf<Arrow>()
        for (i in 0 until arrowCount) {
            val horSpace = areaWidth - arrowSize.width
            val verSpace = areaHeight - arrowSize.height
            val rect = if (isVertical) {
                RectUtil.getRectFBuilder().set(
                    PointF(left + horSpace / 2, top + i * areaHeight + verSpace / 2),
                    arrowSize
                ).build()
            } else {
                RectUtil.getRectFBuilder().set(
                    PointF(left + i * areaWidth + horSpace / 2, top + verSpace / 2),
                    arrowSize
                ).build()
            }
            val strokeWidth = if (isVertical) arrowSize.height / 10 else arrowSize.width / 10
            arrowList.add(Arrow(COLOR_ARRAY.random(), strokeWidth.toInt(), direction, rect))
        }
        arrowDrawable.addArrow(arrowList)
    }

    private fun setArrowDirection(direction: Int?) {
        val btnList = listOf(btnAddLeft, btnAddUp, btnAddRight, btnAddDown)
        btnList.forEachIndexed { index, button ->
            if (direction == null) {
                button.isEnabled = true
            } else {
                button.isEnabled = direction == index
            }
        }
        if (direction == null) {
            btnRemove.isEnabled = false
            btnClear.isEnabled = false
        } else {
            btnRemove.isEnabled = true
            btnClear.isEnabled = true
        }
    }
}