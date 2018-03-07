package com.drake.commonlib.model;

import com.drake.commonlib.ListFactory;
import com.drake.ui.model.IItem;

public class MyItem implements IItem {

    public String title;

    @Override
    public int getType() {
        return ListFactory.VIEW_TYPE_ITEM;
    }
}
