package com.drake.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.drake.ui.model.IItem;
import com.drake.ui.viewholder.IViewHolder;

import java.util.List;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter {

    protected Context mContext;
    private List<IItem> mList;

    public BaseRecyclerAdapter(Context context, List<IItem> groupItems) {
        this.mContext = context;
        setItemList(groupItems);
    }

    public void setItemList(List<IItem> list) {
        this.mList = list;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(mContext, parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null)
            return;
        IItem item = getItem(position);
        if (holder instanceof IViewHolder && item != null) {
            IViewHolder iViewHolder = (IViewHolder) holder;
            iViewHolder.bind(item, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public IItem getItem(int position) {
        if (mList != null && position >= 0 && position < getItemCount()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            IItem iItem = mList.get(position);
            if (iItem != null) {
                return iItem.getType();
            }
        }
        return super.getItemViewType(position);
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType);
}
