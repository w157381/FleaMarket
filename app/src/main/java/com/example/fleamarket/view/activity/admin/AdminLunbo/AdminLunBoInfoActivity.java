package com.example.fleamarket.view.activity.admin.AdminLunbo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminLunBodapter;
import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AdminLunBoInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView recyclerView;
    private Button bt_add;


    private List<Advertising> advertisingList;
    private AdminLunBodapter adminLunBodapter;

    private ZLoadingDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {

                recyclerView.setLayoutManager(new LinearLayoutManager(AdminLunBoInfoActivity.this));
                adminLunBodapter = new AdminLunBodapter(AdminLunBoInfoActivity.this, advertisingList);
                recyclerView.setAdapter(adminLunBodapter);
                adminLunBodapter.setOnViewClickListener(new AdminLunBodapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, Advertising advertising) {

                    }

                    @Override
                    public void onItemClickListener_del(int position, Advertising advertising) {
                        adminLunBodapter.removeData(position);
                    }

                    @Override
                    public void onItemClickListener_upd(int position, Advertising advertising) {

                    }
                });
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.admin_lunbo;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_lunbo_back);
        recyclerView = findViewById(R.id.admin_lunbo_list);
        bt_add = findViewById(R.id.admin_lunbo_add_bt);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        selectLunBoInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_main_back:
                break;
        }
    }

    private void selectLunBoInfo() {
        dialog = LoadingUtil.loading(AdminLunBoInfoActivity.this, "Loading...");
        BmobQuery<Advertising> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<Advertising>() {
            @Override
            public void done(List<Advertising> object, BmobException e) {
                if (e == null) {
                    advertisingList = object;
                    handler.sendEmptyMessage(1001);
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
}

