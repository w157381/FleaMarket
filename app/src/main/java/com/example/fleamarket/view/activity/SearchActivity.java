package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.SearchHistoryAdapter;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.bean.SearchHistory;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;

import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back, img_del;
    private SearchView searchView;
    private RelativeLayout rela;
    private RecyclerView recyclerView;
    //数据源
    private SearchHistory data;
    private SearchHistoryAdapter searchHistoryAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                if (data.getLists().size() > 0) {
                    rela.setVisibility(View.VISIBLE);
                }
                recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
                searchHistoryAdapter = new SearchHistoryAdapter(SearchActivity.this, data.getLists());
                recyclerView.setAdapter(searchHistoryAdapter);
                searchHistoryAdapter.setOnViewClickListener(new SearchHistoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener_text(int position, String text) {
                        Intent intent = new Intent(SearchActivity.this,SearchInfoActivity.class);
                        intent.putExtra("title",text);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClickListener_del(int position) {
                        searchHistoryAdapter.removeData(position);

                    }
                });


            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        //控件获取
        img_back = findViewById(R.id.search_back);
        searchView = findViewById(R.id.search_search);
        img_del = findViewById(R.id.search_del);
        rela = findViewById(R.id.search_rela);
        rela.setVisibility(View.GONE);
        data = new SearchHistory();
        recyclerView = findViewById(R.id.search_list);
    }

    @Override
    protected void initData() {
        //监听器
        img_back.setOnClickListener(this);
        img_del.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHistoryAdapter.addData(query);
                Intent intent = new Intent(SearchActivity.this,SearchInfoActivity.class);
                intent.putExtra("title",query.trim());
                startActivity(intent);
                rela.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        //查询信息
        selectHistory();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_del:
                data.getLists().clear();
                handler.sendEmptyMessage(1001);
                break;
        }
    }

    public void selectHistory() {
        BmobQuery<SearchHistory> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", BmobIM.getInstance().getCurrentUid());
        categoryBmobQuery.findObjects(new FindListener<SearchHistory>() {
            @Override
            public void done(List<SearchHistory> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        data = object.get(0);
                        handler.sendEmptyMessage(1001);
                    }
                    data = object.get(0);
                    handler.sendEmptyMessage(1001);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    public void updateSearchHistory() {
        data.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("1212", "更新成功！");
                } else {
                    Log.e("1212", "ERROR:" + e.getMessage());

                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateSearchHistory();

    }
}
