package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.GoodsAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ClassifyGoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_title,tv_null;
    private RelativeLayout rela;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    private String title;
    private List<Goods> goodsList;
    private GoodsAdapter goodsAdapter;
    //等待界面
    private ZLoadingDialog dialog;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1001){
                tv_null.setVisibility(View.GONE);
                for (int i=0;i<goodsList.size();i++){
                    Goods goods = goodsList.get(i);
                    if(goods.getGoods_state()!=1){
                        goodsList.remove(i);
                    }
                }
                if(goodsList.size()==0){
                    sendEmptyMessage(1002);
                }

                recyclerView.setLayoutManager(new GridLayoutManager(ClassifyGoodsActivity.this,2));
                goodsAdapter = new GoodsAdapter(ClassifyGoodsActivity.this,goodsList);
                recyclerView.setAdapter(goodsAdapter);
                goodsAdapter.setOnViewClickListener(new GoodsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, Goods goods) {
                        Intent intent = new Intent(ClassifyGoodsActivity.this, GoodsInfoActivity.class);
                        intent.putExtra("goodsobjectId", goods.getObjectId());
                        startActivity(intent);
                    }
                });
            }
            if (msg.what==1002){
                smartRefreshLayout.setVisibility(View.GONE);
                tv_null.setVisibility(View.VISIBLE);
            }
        }
    };
    @Override
    protected int initLayout() {
        return R.layout.activity_classifygoods;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.classity_back);
        tv_title = findViewById(R.id.classity_title);
        tv_null = findViewById(R.id.classity_null);
        rela = findViewById(R.id.classity_rela);
        smartRefreshLayout = findViewById(R.id.classity_refresh);
        recyclerView = findViewById(R.id.classity_list);
        dialog = LoadingUtil.loading(ClassifyGoodsActivity.this, "Loading...");

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        rela.setOnClickListener(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        tv_title.setText(title);
        if(Constants.GOODS_type01.equals(title)){
            rela.setVisibility(View.VISIBLE);
        }else{
            rela.setVisibility(View.GONE);
        }
        selectClassityGoods(title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.classity_back://返回
                finish();
                break;
            case R.id.classity_rela://跳转专场
                ToastEx.success(ClassifyGoodsActivity.this,"跳转专场");
                break;
        }

    }
    public void selectClassityGoods(String title){
        BmobQuery<Goods> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("goods_type",title);
        categoryBmobQuery.include("goods_user");
        categoryBmobQuery.order("-createdAt");
        categoryBmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    if(object.size()>0){//数据处理
                        goodsList = object;
                        handler.sendEmptyMessage(1001);
                        dialog.cancel();
                    }else{//空数据处理
                       handler.sendEmptyMessage(1002);
                       dialog.cancel();
                    }
                } else {
                    dialog.cancel();
                    ToastEx.error(ClassifyGoodsActivity.this,"错误："+e.getMessage());
                    finish();
                    Log.e("BMOB", e.toString());
                }
            }
        });

    }
}
