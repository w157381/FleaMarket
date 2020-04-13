package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.fleamarket.R;
import com.example.fleamarket.adapter.AnalyzeAdapter;
import com.example.fleamarket.bean.Root;
import com.example.fleamarket.bean.Showapi_res_body;
import com.example.fleamarket.request.ShowApiRequest;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalyzeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView recyclerView;

    private AnalyzeAdapter analyzeAdapter;
    private String title,sss;
    private Root root;
    private Intent intent;
    private ZLoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sss = (new ShowApiRequest("https://route.showapi.com/1615-1", "164651", "9553b5809ec0491fa55fb979a2fc16a8"))
                                    .addTextPara("keyWords",title)
                                    .addTextPara("page", "1")
                                    .addTextPara("mallId", "")
                                    .post();

                            Thread.sleep(1000);//休眠1秒
                            handler.sendEmptyMessage(1002);
                        } catch (InterruptedException e) {
                            handler.sendEmptyMessage(404);
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
            if(msg.what==1002){
                try {
                    JSONObject object = new JSONObject(sss);
                    root = new Root();
                    root.setShowapi_res_error(object.getString("showapi_res_error"));
                    root.setShowapi_res_id(object.getString("showapi_res_id"));
                    root.setShowapi_res_code(object.getInt("showapi_res_code"));
                    root.setShowapi_res_body(JSON.parseObject(String.valueOf(object.getJSONObject("showapi_res_body")), Showapi_res_body.class));
                    Log.e("1212","root："+root.getShowapi_res_body().getAllSize());
                    Log.e("1212","root："+root.getShowapi_res_body().getShopList().get(0).getShopAddr());
                    Log.e("1212","root："+root.getShowapi_res_body().getShopList().get(0).getShopImg());
                    Log.e("1212","root："+root.getShowapi_res_body().getShopList().get(0).getShopPrice());
                    Log.e("1212","root："+root.getShowapi_res_body().getShopList().get(0).getShopTitle());
                    Log.e("1212","root："+root.getShowapi_res_body().getShopList().get(0).getShopType());

                    ToastEx.error(AnalyzeActivity.this,"数据获取成功，共"+root.getShowapi_res_body().getAllSize()+"条！");
                    handler.sendEmptyMessage(1003);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(405);
                    e.printStackTrace();
                }
            }
            if (msg.what==1003){
                dialog.cancel();
                recyclerView.setLayoutManager(new LinearLayoutManager(AnalyzeActivity.this));
                analyzeAdapter = new AnalyzeAdapter(AnalyzeActivity.this,root.getShowapi_res_body().getShopList());
                recyclerView.setAdapter(analyzeAdapter);
                analyzeAdapter.setOnViewClickListener(new AnalyzeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, String addrUrl) {
                        Intent intent = new Intent(AnalyzeActivity.this,AnalyzeWebActivity.class);
                        intent.putExtra("url",addrUrl);
                        startActivity(intent);
                    }
                });

            }
            if (msg.what==404){
                ToastEx.error(AnalyzeActivity.this,"数据获取失败！");
            }
            if (msg.what==405){
                ToastEx.error(AnalyzeActivity.this,"数据解析失败！");
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_analyze;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.analyze_back);
        recyclerView = findViewById(R.id.analyze_list);
        Intent intent = getIntent();
        title = intent.getStringExtra("name");
        handler.sendEmptyMessage(1001);
        dialog =  LoadingUtil.loading(AnalyzeActivity.this,"Loading...");
    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);

        root = new Root();
        intent = getIntent();
        title = intent.getStringExtra("name");
        handler.sendEmptyMessage(1001);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.analyze_back:
                finish();
                break;
        }

    }
}
