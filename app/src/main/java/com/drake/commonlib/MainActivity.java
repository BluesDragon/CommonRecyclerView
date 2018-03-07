package com.drake.commonlib;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.drake.commonlib.model.MyItem;
import com.drake.ui.CommonRecyclerView;
import com.drake.ui.model.IItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    item.title = "i-->" + i;
                    list.add(item);
                }
            }
        });

        // Load Async
        recyclerView.reloadData();
    }
}
