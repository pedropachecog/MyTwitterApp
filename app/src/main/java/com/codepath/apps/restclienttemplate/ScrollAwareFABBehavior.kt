package com.codepath.apps.restclienttemplate
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrollAwareFABBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     nestedScrollAxes: Int): Boolean {
        // Ensure we react to vertical scrolling
        return ViewCompat.SCROLL_AXIS_VERTICAL == nestedScrollAxes
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)

        if (dyConsumed > 0 && View.VISIBLE === child.visibility) {
            // User scrolled down and the FAB is currently visible -> hide the FAB
            child.hide()
        } else if (dyConsumed < 0 && View.VISIBLE !== child.visibility) {
            // User scrolled up and the FAB is currently not visible -> show the FAB
            child.show()
        }
    }
}