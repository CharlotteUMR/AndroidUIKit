package com.jerry.androiduikit.entrance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jerry.androiduikit.databinding.ActivityEntranceBinding
import com.jerry.androiduikit.examples.ArrowDrawableExampleActivity
import com.jerry.androiduikit.examples.BubbleDrawableExampleActivity
import com.jerry.androiduikit.examples.CrossDrawableExampleActivity
import com.jerry.androiduikit.examples.DotDrawableExampleActivity
import com.jerry.androiduikit.examples.ShadowDrawableExampleActivity

class EntranceActivity : AppCompatActivity() {
    companion object {
        val TAB_TITLE_ARRAY = arrayOf("Drawable", "View")
        val DRAWABLE_DATA_ARRAY = arrayListOf(
            TabFragment.ItemData("ArrowDrawable", ArrowDrawableExampleActivity::class.java),
            TabFragment.ItemData("CrossDrawable", CrossDrawableExampleActivity::class.java),
            TabFragment.ItemData("DotDrawable", DotDrawableExampleActivity::class.java),
            TabFragment.ItemData("ShadowDrawable", ShadowDrawableExampleActivity::class.java),
            TabFragment.ItemData("BubbleDrawable", BubbleDrawableExampleActivity::class.java),
        )
        val VIEW_ARRAY = arrayOf("ExcludePaddingTextView")
    }

    private lateinit var binding: ActivityEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEntranceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vpTab = binding.entranceActivityVpTab.apply {
            offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
            adapter = object : FragmentStateAdapter(this@EntranceActivity) {
                override fun getItemCount() = TAB_TITLE_ARRAY.size

                override fun createFragment(position: Int): Fragment {
                    return TabFragment.newInstance(
                        when (position) {
                            0 -> DRAWABLE_DATA_ARRAY
                            else -> arrayListOf()
                        }
                    )
                }
            }
        }
        val tlTab = binding.entranceActivityTlTab
        TabLayoutMediator(tlTab, vpTab) { tab, pos -> tab.setText(TAB_TITLE_ARRAY[pos]) }.attach()
    }
}