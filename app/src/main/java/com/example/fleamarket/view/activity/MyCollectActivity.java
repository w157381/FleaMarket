package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.CollectAdapter;
import com.example.fleamarket.bean.Collect;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyCollectActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private RecyclerView recyclerView;
    private TextView text_null;
    private Message msg1;
    private Collect collect;
    private String userId;
    //等待界面
    private ZLoadingDialog dialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1001) {
                Collections.reverse(collect.getGoodsList());
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                final CollectAdapter collectAdapter = new CollectAdapter(MyCollectActivity.this, collect.getGoodsList());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(collectAdapter);
                collectAdapter.setOnViewClickListener(new CollectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener_quxiao(int position, Goods goods) {
                        collectAdapter.removeData(position);
                    }
                    @Override
                    public void onItemClickListener_view(int position, Goods goods) {
                        Intent intent = new Intent (MyCollectActivity.this, GoodsInfoActivity.class);
                        intent.putExtra("goodsobjectId",goods.getObjectId());
                        startActivity(intent);
                    }
                });

            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_collect;
    }

    @Override
    protected void initView() {

        back = findViewById(R.id.collect_back);
        recyclerView = findViewById(R.id.collect_list);
        text_null = findViewById(R.id.collect_text_null);

    }

    @Override
    protected void initData() {
        //监听器
        back.setOnClickListener(this);

        //初始化
        collect = new Collect();
        userId = BmobIM.getInstance().getCurrentUid();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.collect_back:
                finish();
                break;
        }
    }

    public void selectCollectInfo(String userid) {
        dialog = LoadingUtil.loading(MyCollectActivity.this,"Loading");
        BmobQuery<Collect> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userid", userid);
        categoryBmobQuery.findObjects(new FindListener<Collect>() {
            @Override
            public void done(List<Collect> object, BmobException e) {
                if (e == null) {
                    if (object.get(0).getGoodsList().size() > 0) {
                        collect = object.get(0);
                        mHandler.sendEmptyMessage(1001);
                        recyclerView.setVisibility(View.VISIBLE);
                        text_null.setVisibility(View.GONE);
                        Log.e(1212+"",""+12121);
                        dialog.cancel();
                    } else {
                        Log.e(1212+"",""+12122);
                        recyclerView.setVisibility(View.GONE);
                        text_null.setVisibility(View.VISIBLE);
                        dialog.cancel();
                    }
                } else {
                    Log.e(1212+"",""+12123);
                    Log.e("BMOB", e.toString());
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectCollectInfo(userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        collect.update(collect.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                }else{
                    Log.e("error",""+e.getMessage());
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1){

        }
    }
}
