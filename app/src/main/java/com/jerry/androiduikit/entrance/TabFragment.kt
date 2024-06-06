package com.jerry.androiduikit.entrance

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textview.MaterialTextView
import com.jerry.androiduikit.databinding.FragmentTabBinding
import com.jerry.uikit.drawables.ArrowDrawable
import com.jerry.uikit.extensions.dpToPx
import com.jerry.uikit.utils.RectUtil
import java.io.Serializable

class TabFragment : Fragment() {
    companion object {
        private const val ARGUMENT_KEY_DATA_LIST = "argument_data_list"

        fun newInstance(dataArrayList: ArrayList<ItemData>): TabFragment {
            val argBundle = Bundle().apply {
                putSerializable(ARGUMENT_KEY_DATA_LIST, dataArrayList)
            }
            return TabFragment().apply { arguments = argBundle }
        }
    }

    private val dataList = mutableListOf<ItemData>()

    private var binding: FragmentTabBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataList.clear()
        val serializable = arguments?.getSerializable(ARGUMENT_KEY_DATA_LIST)
        if (serializable is ArrayList<*>) {
            serializable.forEach {
                if (it is ItemData) dataList.add(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tabFragmentRvList?.apply {
            adapter = SimpleAdapter(requireActivity(), dataList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : ItemDecoration() {
                private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.GRAY
                    strokeWidth = 1F
                }

                override fun onDrawOver(
                    c: Canvas,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val childCount = parent.childCount
                    if (childCount <= 1) return
                    for (i in 0 until childCount - 1) {
                        val child = parent.getChildAt(i)
                        c.drawLine(
                            (parent.left).toFloat(),
                            child.bottom.toFloat(),
                            (parent.right).toFloat(),
                            child.bottom.toFloat(),
                            paint
                        )
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private class SimpleAdapter(
        private val activity: Activity,
        private val dataList: List<ItemData>
    ) : Adapter<SimpleVH>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleVH {
            val context = parent.context
            return SimpleVH(MaterialTextView(context).apply {
                val dp24 = 24.dpToPx(context)
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, (dp24 * 2).toInt())
                val arrowDrawable = ArrowDrawable(
                    ArrowDrawable.Arrow(
                        currentTextColor,
                        4,
                        ArrowDrawable.Direction.DIRECTION_RIGHT,
                        RectUtil.getRectFBuilder()
                            .set(dp24 * 2 / 5, dp24 / 4, dp24 * 3 / 5, dp24 * 3 / 4)
                            .build()
                    )
                ).apply { setBounds(0, 0, dp24.toInt(), dp24.toInt()) }
                setCompoundDrawables(null, null, arrowDrawable, null)
            })
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: SimpleVH, position: Int) {
            val itemData = dataList[position]
            (holder.itemView as TextView).text = itemData.title
            holder.itemView.setOnClickListener {
                activity.startActivity(Intent(activity, itemData.activityClass))
            }
        }
    }

    private class SimpleVH(itemView: TextView) : ViewHolder(itemView)

    data class ItemData(val title: String, val activityClass: Class<*>): Serializable
}