# CommonRecyclerView

## Maven:
    compile 'com.drake.ui:commonrecyclerview:1.0.2'

### Init CommonRecyclerView
    CommonRecyclerView recyclerView = findViewById(R.id.id_main_commonRecyclerView);
    recyclerView.setCallback(new CommonRecyclerView.Callback() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
            // TODO Add your ViewHolder like this:
            return ListFactory.getHolder(context, parent, viewType);
        }
    
        @Override
        public void buildList(List<IItem> list) {
            // TODO Add your Item like this:
            for(int i = 0; i < 10; i++) {
                MyItem item = new MyItem();
                item.title = "i";
                list.add(item);
            }
        }
    });
    
    // Load Async
    recyclerView.reloadData();

### ListFactory

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

### MyViewHolder
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

### MyItem
    public class MyItem implements IItem {
    
        public String title;
    
        @Override
        public int getType() {
            return ListFactory.VIEW_TYPE_ITEM;
        }
    }

### Load Data
    // Load Async
    recyclerView.reloadData();
    
### Refresh List Async
    recyclerView.updateListAsync();

### Refresh List Sync (Same to notifyDataSetChange)
    recyclerView.updateList();

### Set your Item list
    recyclerView.setItemList(mList);
    // refresh
    recyclerView.updateDataSet();

