package com.dreamakasa.stabbble.common

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import com.dreamakasa.stabbble.R
import de.hdodenhof.circleimageview.CircleImageView

class CollapsingImageBehavior : CoordinatorLayout.Behavior<View> {

    private var mTargetId: Int = 0

    private var mHideableTargetId: Int = 0

    private var mView: IntArray? = null

    private var mTarget: IntArray? = null

    @Suppress("unused")
    constructor() {}

    @Suppress("unused")
    constructor(context: Context, attrs: AttributeSet?) {

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingImageBehavior)
            mTargetId = a.getResourceId(R.styleable.CollapsingImageBehavior_collapsedTarget, 0)
            mHideableTargetId = a.getResourceId(R.styleable.CollapsingImageBehavior_hideableOnScroll, 0)
            a.recycle()
        }

        if (mTargetId == 0) {
            throw IllegalStateException("collapsedTarget attribute not specified on view for behavior")
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: View?, dependency: View?): Boolean =
            dependency is AppBarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: View, dependency: View?): Boolean {

        setup(parent, child)

        val username = parent!!.findViewById(mHideableTargetId)
        val appBarLayout = dependency as AppBarLayout?

        val range = appBarLayout!!.totalScrollRange
        val factor = -appBarLayout.y / range

        username.alpha = 1.0f - factor

        val avatar = child as CircleImageView
        avatar.borderWidth = (5.0f - (2 * factor)).toInt()
//        val left = mView!![X] + (factor * (mTarget!![X] - mView!![X]))
        val top = mView!![Y] + (factor * (mTarget!![Y] - mView!![Y]))
        val width = mView!![WIDTH] + (factor * (mTarget!![WIDTH] - mView!![WIDTH]))
        val height = mView!![HEIGHT] + (factor * (mTarget!![HEIGHT] - mView!![HEIGHT]))

        val lp = child!!.layoutParams as CoordinatorLayout.LayoutParams
        lp.width = width.toInt()
        lp.height = height.toInt()
        child.layoutParams = lp
//        child.x = left
        child.y = top

        return true
    }

    private fun setup(parent: CoordinatorLayout?, child: View?) {

        if (mView != null) return

        mView = IntArray(4)
        mTarget = IntArray(4)

        mView!![X] = child!!.x.toInt()
        mView!![Y] = child.y.toInt()
        mView!![WIDTH] = child.width
        mView!![HEIGHT] = child.height

        val target = parent!!.findViewById(mTargetId) ?: throw IllegalStateException("target view not found")

        mTarget!![WIDTH] += target.width
        mTarget!![HEIGHT] += target.height

        var view = target
        while (view !== parent) {
            mTarget!![X] += view.x.toInt()
            mTarget!![Y] += view.y.toInt()
            view = view.parent as View
        }

    }

    companion object {

        private val X = 0
        private val Y = 1
        private val WIDTH = 2
        private val HEIGHT = 3
    }
}