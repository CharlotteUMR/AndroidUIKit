package com.jerry.androiduikit.examples

import android.graphics.Color
import android.graphics.RectF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.jerry.androiduikit.R
import com.jerry.androiduikit.databinding.ActivityBubbleDrawableExampleBinding
import com.jerry.uikit.drawables.ArrowDrawable
import com.jerry.uikit.drawables.bubble.Anchor
import com.jerry.uikit.drawables.bubble.BubbleDrawable
import com.jerry.uikit.drawables.bubble.CustomPathArrowParam
import com.jerry.uikit.drawables.bubble.DrawableArrowParam
import com.jerry.uikit.drawables.bubble.GradientColorParam
import com.jerry.uikit.drawables.bubble.LineArrowParam
import com.jerry.uikit.drawables.bubble.Side
import com.jerry.uikit.entities.Quaternion

class BubbleDrawableExampleActivity: AppCompatActivity() {
    private lateinit var binding: ActivityBubbleDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBubbleDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bubbleExampleView1.setImageDrawable(
            BubbleDrawable.Builder(GradientColorParam.ltToRbMidperpendicular(intArrayOf(Color.RED, Color.BLUE)))
                .corner(Quaternion(30F))
                .arrow(DrawableArrowParam(
                    80F,
                    20F,
                    AppCompatResources.getDrawable(this, R.drawable.img_bubble_arrow_test)!!,
                    Side.TOP,
                    Anchor.CENTER,
                ))
                .build()
        )
    }
}