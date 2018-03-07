# CommonRecyclerView

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
