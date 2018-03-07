package com.drake.commonlib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drake.commonlib.viewholder.MyViewHolder;

public class ListFactory {

    public static final int VIEW_TYPE_ITEM = 1;

    public static View getView(Context context, ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        }
        return null;
    }

    public static RecyclerView.ViewHolder getHolder(Context context, ViewGroup parent, int viewType) {
        View view = getView(context, parent, viewType);
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return new MyViewHolder(context, view);
        }
        return null;
    }

}
