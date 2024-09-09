package com.jerry.androiduikit.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jerry.androiduikit.databinding.ActivityExcludePaddingTextViewExampleBinding

class ExcludePaddingTextViewExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExcludePaddingTextViewExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcludePaddingTextViewExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}