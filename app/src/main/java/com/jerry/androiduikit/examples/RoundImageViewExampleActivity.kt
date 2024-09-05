package com.jerry.androiduikit.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityRoundImageViewExampleBinding

class RoundImageViewExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoundImageViewExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoundImageViewExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}