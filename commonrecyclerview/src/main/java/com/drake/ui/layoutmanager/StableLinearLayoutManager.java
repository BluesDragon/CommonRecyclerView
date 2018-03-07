package com.drake.ui.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 添加内部崩溃保护的LinearLayoutManager
 */
public class StableLinearLayoutManager extends LinearLayoutManager {

    public StableLinearLayoutManager(Context context) {
        super(context);
    }

    public StableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public StableLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int verticallyBy = 0;
        /**
         * 避免内部错误，由Google官方解决。
         * This bug is still not fixed in 23.1.1, but a common workaround would be to catch the exception.
         */
        try {
            verticallyBy = super.scrollVerticallyBy(dy, recycler, state);
        } catch (Exception e) {
            // do nothing.
        }
        return verticallyBy;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        /**
         * 避免内部错误，由Google官方解决。
         * This bug is still not fixed in 23.1.1, but a common workaround would be to catch the exception.
         */
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            // do nothing.
        }
    }
}
