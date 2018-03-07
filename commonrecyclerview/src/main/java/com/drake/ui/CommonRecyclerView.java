package com.drake.ui;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.drake.ui.adapter.BaseRecyclerAdapter;
import com.drake.ui.layoutmanager.StableLinearLayoutManager;
import com.drake.ui.model.IItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的RecyclerView，内部集成了Adapter等通用的逻辑，只需要实现简单的回调即可。
 */
public class CommonRecyclerView<ITEM extends IItem> extends RecyclerView {

    public abstract static class Callback {

        public abstract ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType);

        public abstract void buildList(List<IItem> list);

        public void filterList(List<IItem> list) {
        }

        public boolean onBindViewHolder(ViewHolder holder, IItem item, int position) {
            return false;
        }

        public void onDataSetChanged() {
        }

        public void onItemRemoved(IItem iItem) {

        }

        public void onItemRemoved(List<IItem> list) {

        }
    }

    static HandlerThread sHt = null;

    public static synchronized Looper getNonUiLooper() {
        if (sHt == null) {
            sHt = new HandlerThread("crv");
            sHt.start();
        }
        return sHt.getLooper();
    }

    private Context mContext;
    protected BaseRecyclerAdapter mAdapter;
    protected final List<IItem> mList = new ArrayList<>();
    private Callback mCallback;

    private static final int MSG_UPDATE_LIST = 1;
    private static final int MSG_NOTIFY_DATA_SET_CHANGE = 2;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_LIST:
                    if (mAdapter == null) {
                        mAdapter = new BaseRecyclerAdapter(mContext, mList) {
                            @Override
                            public ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
                                if (mCallback != null)
                                    return mCallback.onCreateViewHolder(context, parent, viewType);
                                return null;
                            }

                            @Override
                            public void onBindViewHolder(ViewHolder holder, int position) {
                                if (mCallback != null && mCallback.onBindViewHolder(holder, getItem(position), position)) {
                                    return;
                                }
                                super.onBindViewHolder(holder, position);
                            }
                        };
                        setAdapter(mAdapter);
                    } else {
                        mAdapter.setItemList(mList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case MSG_NOTIFY_DATA_SET_CHANGE:
                    if (mCallback != null) {
                        mCallback.onDataSetChanged();
                    }
                    break;
            }
        }
    };

    private static final int MSG_UPDATE_LIST_ASYNC = 1;
    private static final int MSG_BUILD_LIST = 2;
    private static final int MSG_ADD_ITEM = 3;
    private static final int MSG_REMOVE_ITEM = 4;
    private static final int MSG_CLEAR_LIST = 5;
    private Handler mNonUIHandler;

    private void initNonUIHandler() {
        if (isInEditMode())
            return;
        mNonUIHandler = new Handler(getNonUiLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_LIST_ASYNC:
                        synchronized (mList) {
                            if (mCallback != null) {
                                mCallback.filterList(mList);
                            }
                            updateDataSet();
                            notifyDataSetChange();
                        }
                        break;
                    case MSG_BUILD_LIST:
                        synchronized (mList) {
                            if (mCallback != null) {
                                List<IItem> list = new ArrayList<>();
                                mCallback.buildList(list);
                                mList.clear();
                                mList.addAll(list);
                                updateList();
                            }
                        }
                        break;
                    case MSG_ADD_ITEM: {
                        synchronized (mList) {
                            IItem iItem = (IItem) msg.obj;
                            if (!mList.contains(iItem)) {
                                if (msg.arg1 != -1) {
                                    mList.add(msg.arg1, iItem);
                                } else {
                                    mList.add(iItem);
                                }
                            }
                            updateList();
                        }
                        break;
                    }
                    case MSG_REMOVE_ITEM:
                        synchronized (mList) {
                            IItem iItem = (IItem) msg.obj;
                            if (mList.contains(iItem)) {
                                mList.remove(iItem);
                                if (mCallback != null) {
                                    mCallback.onItemRemoved(iItem);
                                }
                            }
                            updateList();
                        }
                        break;
                    case MSG_CLEAR_LIST:
                        synchronized (mList) {
                            mList.clear();
                        }
                        updateList();
                        break;
                }
            }
        };
    }

    public CommonRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context.getApplicationContext();
        initNonUIHandler();
        setLayoutManager(initLayoutManager(mContext));
    }

    private void notifyDataSetChange() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_NOTIFY_DATA_SET_CHANGE);
        }
    }

    public void updateDataSet() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(MSG_UPDATE_LIST);
        }
    }

    protected LayoutManager initLayoutManager(Context context) {
        return new StableLinearLayoutManager(context);
    }

    public void reloadData() {
        if (mNonUIHandler != null) {
            mNonUIHandler.sendEmptyMessage(MSG_BUILD_LIST);
        }
    }

    public List<IItem> getItemList() {
        return mList;
    }

    /**
     * 获取当前列表数量
     */
    public int getCurrentListSize() {
        return mList.size();
    }

    /**
     * 添加一条数据
     */
    public void addItem(IItem iItem) {
        addItem(-1, iItem);
    }

    /**
     * 添加一条数据
     */
    public void addItem(int position, IItem iItem) {
        if (mNonUIHandler != null) {
            Message msg = Message.obtain();
            msg.what = MSG_ADD_ITEM;
            msg.obj = iItem;
            msg.arg1 = position;
            mNonUIHandler.sendMessage(msg);
        }
    }

    public void setItemList(List<ITEM> list) {
        synchronized (mList) {
            this.mList.clear();
            this.mList.addAll(list);
        }
    }

    /**
     * 立即清空List数据，不刷新
     */
    public void clearListImmediately() {
        mList.clear();
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(int position) {
        if (position >= 0 && mList.size() > position) {
            IItem iItem = mList.remove(position);
            if (mCallback != null) {
                mCallback.onItemRemoved(iItem);
            }
        }
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(IItem iItem) {
        if (mList.contains(iItem)) {
            mList.remove(iItem);
            if (mCallback != null) {
                mCallback.onItemRemoved(iItem);
            }
        }
    }

    /**
     * 立即从数据列表中删除一条数据，不负责刷新View
     */
    public void removeItemImmediately(List<IItem> list) {
        mList.removeAll(list);
        if (mCallback != null) {
            mCallback.onItemRemoved(list);
        }
    }

    /**
     * 删除一条数据
     */
    public void removeItem(IItem iItem) {
        if (mNonUIHandler != null) {
            mNonUIHandler.obtainMessage(MSG_REMOVE_ITEM, iItem).sendToTarget();
        }
    }

    /**
     * 清空列表
     */
    public void clearList() {
        if (mNonUIHandler != null) {
            mNonUIHandler.sendEmptyMessage(MSG_CLEAR_LIST);
        }
    }

    /**
     * 刷新列表
     */
    public void updateList() {
        if (mNonUIHandler != null) {
            mNonUIHandler.sendEmptyMessage(MSG_UPDATE_LIST_ASYNC);
        }
    }

    public IItem getItem(int position) {
        if (mAdapter != null)
            return mAdapter.getItem(position);
        return null;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public int getItemViewType(int position) {
        if (mList != null && position >= 0 && position < mList.size()) {
            IItem iItem = mList.get(position);
            if (iItem != null) {
                return iItem.getType();
            }
        }
        return 0;
    }

    @Override
    protected void onAttachedToWindow() {
        if (!isInEditMode()) {
            super.onAttachedToWindow();
        }
    }
}
