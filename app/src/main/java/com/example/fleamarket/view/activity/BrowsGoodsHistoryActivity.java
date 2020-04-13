package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.BrowsGoodsAdapter;
import com.example.fleamarket.bean.BrowsGoodsHistory;
import com.example.fleamarket.bean.MyAddress;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class BrowsGoodsHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView tv_null, tv_delall;

    private List<BrowsGoodsHistory> browsGoodsHistoryList;
    private BrowsGoodsAdapter browsGoodsAdapter;

    private ZLoadingDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                tv_delall.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(BrowsGoodsHistoryActivity.this));
                browsGoodsAdapter = new BrowsGoodsAdapter(BrowsGoodsHistoryActivity.this, browsGoodsHistoryList);
                recyclerView.setAdapter(browsGoodsAdapter);
                browsGoodsAdapter.setOnViewClickListener(new BrowsGoodsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, String objectId) {
                        Intent intent = new Intent(BrowsGoodsHistoryActivity.this, GoodsInfoActivity.class);
                        intent.putExtra("goodsobjectId", objectId);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClickListener_del(int position, BrowsGoodsHistory browsGoodsHistory) {
                        delBrowsHis(position, browsGoodsHistory);
                    }
                });
            }
            if (msg.what == 1002) {
                tv_null.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
            if(msg.what==1003){
                browsGoodsAdapter.removeAllData();
                tv_delall.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_brows_goods_history;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.brows_his_back);
        tv_delall = findViewById(R.id.brows_his_delall);
        refreshLayout = findViewById(R.id.brows_his_refresh);
        recyclerView = findViewById(R.id.brows_his_list);
        tv_null = findViewById(R.id.brows_his_text);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        tv_delall.setOnClickListener(this);
        browsGoodsHistoryList = new ArrayList<>();
        selectBrowsHis(BmobIM.getInstance().getCurrentUid());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.brows_his_back:
                finish();
                break;
            case R.id.brows_his_delall:
                ToastEx.success(BrowsGoodsHistoryActivity.this, "delall").show();
                delAllBrowsHis(browsGoodsHistoryList);
                break;
        }
    }

    //查询记录
    public void selectBrowsHis(String userId) {
        dialog = LoadingUtil.loading(BrowsGoodsHistoryActivity.this, "Loading...");
        BmobQuery<BrowsGoodsHistory> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", userId);
        categoryBmobQuery.include("goods_info,goodsUser_info");
        categoryBmobQuery.order("-createdAt");
        categoryBmobQuery.findObjects(new FindListener<BrowsGoodsHistory>() {
            @Override
            public void done(List<BrowsGoodsHistory> object, BmobException e) {
                dialog.cancel();
                if (e == null) {
                    if (object.size() > 0) {
                        browsGoodsHistoryList = object;
                        handler.sendEmptyMessage(1001);
                    } else {
                        handler.sendEmptyMessage(1002);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    ToastEx.error(BrowsGoodsHistoryActivity.this, "错误！" + e.getMessage());
                    finish();
                }
            }
        });

    }

    //删除指定BrowsGoodsHistory
    private void delBrowsHis(final int position, BrowsGoodsHistory browsGoodsHistory) {
        browsGoodsHistory.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastEx.success(BrowsGoodsHistoryActivity.this, "删除成功!").show();
                    browsGoodsAdapter.removeData(position);
                } else {
                    ToastEx.success(BrowsGoodsHistoryActivity.this, "删除失败！" + e.getMessage()).show();
                }
            }

        });
    }

    //删除所有BrowsGoodsHistory
    private void delAllBrowsHis(final List<BrowsGoodsHistory> browsGoodsHistoryList) {
        for (int i = 0; i < browsGoodsHistoryList.size(); i++) {
            final BrowsGoodsHistory browsGoodsHistory = browsGoodsHistoryList.get(i);
            browsGoodsHistory.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        browsGoodsHistoryList.remove(browsGoodsHistory);
                        if (browsGoodsHistoryList.size() == 0) {
                            handler.sendEmptyMessage(1003);
                            ToastEx.success(BrowsGoodsHistoryActivity.this, "已清空！").show();
                        }
                    } else {
                        ToastEx.error(BrowsGoodsHistoryActivity.this, "删除失败！").show();
                    }
                }

            });
        }

    }
}
