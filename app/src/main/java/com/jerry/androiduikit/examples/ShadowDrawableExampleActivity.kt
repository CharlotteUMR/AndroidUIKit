package com.jerry.androiduikit.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityShadowDrawableExampleBinding
import com.jerry.uikit.drawables.ShadowDrawable
import com.jerry.uikit.entities.Offset
import com.jerry.uikit.entities.Quaternion

class ShadowDrawableExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShadowDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShadowDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shadowExampleView1.background = ShadowDrawable.Builder(0x80000000.toInt(), 40F)
            .foreground(0xFFFF8888.toInt())
            .offset(Offset(0F, 10F))
            .corner(Quaternion(10F))
            .drawOutside()
            .build()

        binding.shadowExampleView2.background = ShadowDrawable.Builder(0x800000FF.toInt(), 40F)
            .foreground(0xFF80FF80.toInt())
            .offset(Offset(0F, 10F))
            .corner(Quaternion(10F))
            .build()
    }
}