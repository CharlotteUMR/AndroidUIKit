package com.jerry.androiduikit.examples

import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.jerry.androiduikit.R
import com.jerry.androiduikit.databinding.ActivityBubbleDrawableExampleBinding
import com.jerry.uikit.drawables.bubble.Anchor
import com.jerry.uikit.drawables.bubble.BubbleDrawable
import com.jerry.uikit.drawables.bubble.CustomPathArrowParam
import com.jerry.uikit.drawables.bubble.DrawableArrowParam
import com.jerry.uikit.drawables.bubble.GradientColorParam
import com.jerry.uikit.drawables.bubble.LineArrowParam
import com.jerry.uikit.drawables.bubble.Side
import com.jerry.uikit.entities.Quaternion

class BubbleDrawableExampleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBubbleDrawableExampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBubbleDrawableExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bubbleExampleView1.background = BubbleDrawable.Builder(0x88FF0000.toInt())
            .corner(Quaternion(30F))
            .arrow(LineArrowParam(80F, 20F, Side.LEFT, Anchor.CENTER))
            .build()
        val leftToRight = GradientColorParam.leftToRight(
            intArrayOf(
                Color.RED,
                Color.BLUE
            )
        )
        binding.bubbleExampleView2.background = BubbleDrawable.Builder(leftToRight)
            .corner(Quaternion(30F))
            .arrow(
                DrawableArrowParam(
                    80F,
                    20F,
                    AppCompatResources.getDrawable(this, R.drawable.img_bubble_arrow_test)!!,
                    Side.TOP,
                    Anchor.CENTER
                )
            )
            .build()
        val lt2rb = GradientColorParam.leftTopToRightBottom(
            intArrayOf(
                Color.RED,
                Color.BLUE
            )
        )
        binding.bubbleExampleView3.background = BubbleDrawable.Builder(lt2rb)
            .corner(Quaternion(30F))
            .arrow(CustomPathArrowParam(
                80F,
                80F,
                Side.RIGHT,
                Anchor.CENTER
            ) { width, height, pathBuilder ->
                pathBuilder.addOval(5F, 0F, width - 5, height - 10, Path.Direction.CW)
            })
            .build()
        val customAngle = GradientColorParam.customizeAngle(
            intArrayOf(
                Color.RED,
                Color.BLUE
            ),
            angle = 10
        )
        binding.bubbleExampleView4.background = BubbleDrawable.Builder(customAngle)
            .corner(Quaternion(30F))
            .arrow(LineArrowParam(80F, 20F, Side.BOTTOM, Anchor.CENTER))
            .stroke(BubbleDrawable.StrokeParam(9F, 0x8800FF00.toInt()))
            .build()
    }
}