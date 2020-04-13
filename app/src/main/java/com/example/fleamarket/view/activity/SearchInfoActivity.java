package com.example.fleamarket.view.activity;

import android.content.Entity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.GoodsAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.view.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zia.toastex.ToastEx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SearchInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_titie;
    private Spinner spinner;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;


    private String title;
    private Intent intent;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private List<Goods> goodsDataChange,goodsData,goodsData0;

    private GoodsAdapter goodsAdapter;

    // 分页数据
    private int pageIndex = 0;
    private int pageSize = 10;

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始

    String lastTime = null;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                Goods goods = new Goods();
                for (int i = 0; i < goodsData0.size(); i++) {
                    goods = goodsData0.get(i);
                    if (goods.getGoods_name().indexOf(title) != -1 || goods.getGoods_info().indexOf(title) != -1) {
                        if(goods.getGoods_state()==1){
                            goodsData.add(goodsData0.get(i));
                        }
                    }
                }
                Log.e("1212", "goodsData:" + goodsData.size());
                setListDate(goodsData);
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_search_info;
    }

    @Override
    protected void initView() {
        tv_titie = findViewById(R.id.search_info_title);
        spinner = findViewById(R.id.search_info_spinner);
        refreshLayout = findViewById(R.id.search_info_refresh);
        recyclerView = findViewById(R.id.search_info_list);

    }

    @Override
    protected void initData() {
        tv_titie.setOnClickListener(this);

        goodsData = new ArrayList<>();
        goodsData0 = new ArrayList<>();
        goodsDataChange = new ArrayList<>();
        intent = getIntent();
        title = intent.getStringExtra("title");
        tv_titie.setText(title);
        //数据
        data_list = new ArrayList<String>();
        data_list.add("默认");
        data_list.add("价格从高到低");
        data_list.add("价格从低到高");
        data_list.add("喜欢人数从高到低");
        data_list.add("信誉从高到低");
        data_list.add("包邮");

        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {//默认

                }
                if (position == 1) {//价格从高到低
                    Collections.sort(goodsDataChange, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods o1, Goods o2) {
                            if (o1.getGoods_price() > o2.getGoods_price()) {
                                return -1;
                            } else if (o1.getGoods_price() < o2.getGoods_price()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                if (position == 2) {//价格从低到高
                    Collections.sort(goodsDataChange, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods o1, Goods o2) {
                            if (o1.getGoods_price() > o2.getGoods_price()) {
                                return 1;
                            } else if (o1.getGoods_price() < o2.getGoods_price()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                if (position == 3) {//喜欢人数从高到低
                    Collections.sort(goodsDataChange, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods o1, Goods o2) {
                            if (o1.getGoods_like().size() > o2.getGoods_like().size()) {
                                return -1;
                            } else if (o1.getGoods_like().size() < o2.getGoods_like().size()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                if (position == 4) {//信誉从高到低
                    Collections.sort(goodsDataChange, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods o1, Goods o2) {
                            if (o1.getGoods_user().getUser_credit() > o2.getGoods_user().getUser_credit()) {
                                return -1;
                            } else if (o1.getGoods_user().getUser_credit() < o2.getGoods_user().getUser_credit()) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });
                }
                if (position == 5) {//包邮
                    Goods goods = new Goods();
                    for (int i = 0; i < goodsDataChange.size(); i++) {
                        goods = goodsDataChange.get(i);
                        if (goods.getGoods_post() != 0) {
                            goodsDataChange.remove(i);

                        }
                    }
                }
                setListDate(goodsDataChange);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        queryData();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.search_info_title:
                finish();
                break;
        }

    }

    private void queryData() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("goods_user");
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    Log.e("1212", "object:" + object.size());
                    goodsData0 = object;
                    handler.sendEmptyMessage(1001);
                } else {
                    Log.e("bmobaaa", "失败：" + e.getMessage());
                }
            }
        });

    }

    private void setListDate(List<Goods> goods) {
        recyclerView.setLayoutManager(new GridLayoutManager(SearchInfoActivity.this, 2));
        goodsAdapter = new GoodsAdapter(SearchInfoActivity.this, goods);
        recyclerView.setAdapter(goodsAdapter);
        goodsAdapter.setOnViewClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Goods goods) {
                Intent intent = new Intent(SearchInfoActivity.this, GoodsInfoActivity.class);
                intent.putExtra("goodsobjectId", goods.getObjectId());
                startActivity(intent);
            }
        });
        goodsDataChange = goodsData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
