package com.jerry.uikit.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.RadioGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group

/**
 * 适用于[ConstraintLayout]的类似[RadioGroup]控件
 */
class ConstraintCheckableGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : Group(context, attrs) {
    /**
     * 当前选中的[Checkable]
     */
    private var curSelView: Checkable? = null

    /**
     * 选中监听器
     */
    var onCheckedChangeListener: OnCheckedChangeListener? = null

    override fun updatePreLayout(container: ConstraintLayout) {
        super.updatePreLayout(container)

        curSelView = null
        for (i in 0 until mCount) {
            val referencedId = mIds[i]
            val referencedView = container.getViewById(referencedId)

            if (referencedView !is Checkable) continue

            if (referencedView.isChecked) {
                curSelView = referencedView
            }

            referencedView.setOnClickListener { selectView(it) }
        }
    }

    fun selectView(view: View?) {
        if (view !is Checkable) return
        if (curSelView == view) return

        // 取消当前选中View的选中态
        curSelView?.isChecked = false
        // 选中当前点击的View
        view.isChecked = true
        curSelView = view

        onCheckedChangeListener?.onCheckedChange(this, view.id)
    }

    fun selectView(@IdRes viewId: Int) {
        val container = (parent as? ConstraintLayout) ?: return
        selectView(container.getViewById(viewId))
    }

    interface OnCheckedChangeListener {
        fun onCheckedChange(group: ConstraintCheckableGroup, @IdRes checkedId: Int)
    }
}