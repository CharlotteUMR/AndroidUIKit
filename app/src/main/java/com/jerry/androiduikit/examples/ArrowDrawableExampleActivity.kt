package com.jerry.androiduikit.examples

import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityArrowDrawableExampleBinding
import com.jerry.uikit.drawables.ArrowDrawable

class ArrowDrawableExampleActivity : AppCompatActivity() {
    companion object {
        val COLOR_ARRAY = arrayOf(Color.RED, Color.GREEN, Color.BLACK, Color.BLUE, Color.CYAN)
    }

    private lateinit var binding: ActivityArrowDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArrowDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowExampleView1.background = ArrowDrawable(
            ArrowDrawable.Arrow(
                COLOR_ARRAY[0],
                3,
                ArrowDrawable.Direction.DIRECTION_RIGHT,
                RectF(30F, 30F, 60F, 60F)
            )
        )
        binding.arrowExampleView2.background = ArrowDrawable(
            ArrowDrawable.Arrow(
                COLOR_ARRAY[1],
                20,
                ArrowDrawable.Direction.DIRECTION_LEFT,
                RectF(30F, 30F, 60F, 60F)
            )
        )
        binding.arrowExampleView3.background = ArrowDrawable(
            ArrowDrawable.Arrow(
                COLOR_ARRAY[2],
                3,
                ArrowDrawable.Direction.DIRECTION_TOP,
                RectF(30F, 30F, 60F, 60F)
            )
        )
        binding.arrowExampleView4.background = ArrowDrawable(
            ArrowDrawable.Arrow(
                COLOR_ARRAY[3],
                6,
                ArrowDrawable.Direction.DIRECTION_BOTTOM,
                RectF(30F, 10F, 60F, 40F)
            ),
            ArrowDrawable.Arrow(
                COLOR_ARRAY[4],
                6,
                ArrowDrawable.Direction.DIRECTION_BOTTOM,
                RectF(30F, 50F, 60F, 80F)
            )
        )
    }
}