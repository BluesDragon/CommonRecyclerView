package com.drake.ui.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

    protected Context mContext;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    protected String getString(int resId) {
        return mContext.getResources().getString(resId);
    }

    protected int getColor(int resId) {
        return mContext.getResources().getColor(resId);
    }

}
