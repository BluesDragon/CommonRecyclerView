package com.drake.commonlib.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.drake.commonlib.R;
import com.drake.commonlib.model.MyItem;
import com.drake.ui.model.IItem;
import com.drake.ui.viewholder.BaseViewHolder;

public class MyViewHolder extends BaseViewHolder {

    private TextView mTv;

    private MyItem mItem;

    public MyViewHolder(Context context, View itemView) {
        super(context, itemView);
        // TODO Find your View here:
        mTv = itemView.findViewById(R.id.id_list_item_tv);
    }

    @Override
    public void bind(IItem item, int position) {
        if (item == null || !(item instanceof MyItem))
            return;
        mItem = (MyItem) item;
        // TODO Refresh your data.

        if (mTv != null) {
            mTv.setText(mItem.title);
        }
    }
}
