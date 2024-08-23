package com.jerry.androiduikit.examples

import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityCrossDrawableExampleBinding
import com.jerry.uikit.drawables.CrossDrawable

class CrossDrawableExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrossDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrossDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.crossExampleView1.background = CrossDrawable(
            CrossDrawable.Cross(
                Color.RED,
                3,
                RectF(30F, 30F, 60F, 60F)
            )
        )

        binding.crossExampleView2.background = CrossDrawable(
            CrossDrawable.Cross(
                Color.GREEN,
                20,
                RectF(30F, 30F, 60F, 60F)
            )
        )
    }
}