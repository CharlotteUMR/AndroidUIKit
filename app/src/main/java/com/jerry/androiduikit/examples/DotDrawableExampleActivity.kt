package com.jerry.androiduikit.examples

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityDotDrawableExampleBinding
import com.jerry.uikit.drawables.DotDrawable

class DotDrawableExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDotDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDotDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dotExampleView1.background = DotDrawable(DotDrawable.Dot(Color.RED, 10, 30 to 30))
        binding.dotExampleView2.background = DotDrawable(DotDrawable.Dot(Color.RED, 30, 30 to 30))
        binding.dotExampleView3.background = DotDrawable(
            DotDrawable.Dot(Color.RED, 10, 30 to 30),
            DotDrawable.Dot(Color.BLUE, 10, 60 to 60)
        )
        binding.dotExampleView4.background = DotDrawable(
            DotDrawable.Dot(Color.RED, 10, 30 to 30),
            DotDrawable.Dot(Color.BLUE, 10, 30 to 60),
            DotDrawable.Dot(Color.GREEN, 10, 60 to 30),
            DotDrawable.Dot(Color.YELLOW, 10, 60 to 60)
        )
    }
}