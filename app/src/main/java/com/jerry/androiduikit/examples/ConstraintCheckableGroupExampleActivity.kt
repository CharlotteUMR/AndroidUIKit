package com.jerry.androiduikit.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityConstraintCheckableGroupExampleBinding
import com.jerry.androiduikit.databinding.ActivityShadowDrawableExampleBinding
import com.jerry.uikit.drawables.ShadowDrawable
import com.jerry.uikit.entities.Offset
import com.jerry.uikit.entities.Quaternion

class ConstraintCheckableGroupExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstraintCheckableGroupExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstraintCheckableGroupExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}