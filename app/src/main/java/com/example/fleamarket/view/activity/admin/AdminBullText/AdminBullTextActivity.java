package com.example.fleamarket.view.activity.admin.AdminBullText;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminBulldapter;
import com.example.fleamarket.bean.BulletinText;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.GoodsInfoActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminBullTextActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView recyclerView;
    private Button bt;

    private ZLoadingDialog dialog;
    private AdminBulldapter adminBulldapter;
    private List<BulletinText> bulletinTextList;


    @Override
    protected int initLayout() {
        return R.layout.admin_bulltext;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_bull_back);
        recyclerView = findViewById(R.id.admin_bull_list);
        bt = findViewById(R.id.admin_bull_bt);
    }

    @Override
    protected void initData() {
        bulletinTextList = new ArrayList<>();
        img_back.setOnClickListener(this);
        bt.setOnClickListener(this);


        selectBullInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_bull_back:
                finish();
                break;

            case R.id.admin_bull_bt:
                IntentUtil.get().goActivityResult(AdminBullTextActivity.this, AdminAddBullTextActivity.class, 0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || resultCode == 0) {
            if (data.getStringExtra("aa").equals("ok")) {
                BulletinText bulletinText =(BulletinText) data.getSerializableExtra("bulletinText");
                adminBulldapter.addData(bulletinText);
                ToastEx.success(AdminBullTextActivity.this,"添加成功！").show();
            }

        }
    }


    private void selectBullInfo() {

        dialog = LoadingUtil.loading(AdminBullTextActivity.this, "Loading...");
        BmobQuery<BulletinText> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<BulletinText>() {
            @Override
            public void done(List<BulletinText> object, BmobException e) {
                if (e == null) {
                    bulletinTextList = object;
                    setData();
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }


    private void setData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminBullTextActivity.this));
        adminBulldapter = new AdminBulldapter(AdminBullTextActivity.this, bulletinTextList);
        recyclerView.setAdapter(adminBulldapter);
        adminBulldapter.setOnViewClickListener(new AdminBulldapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, BulletinText bulletinText) {

                Intent intent = new Intent(AdminBullTextActivity.this, GoodsInfoActivity.class);
                intent.putExtra("goodsobjectId", bulletinText.getGoods_objectId());
                startActivity(intent);
            }

            @Override
            public void onItemClickListener_del(int position, BulletinText bulletinText) {

                delData(bulletinText, position);

            }
        });

    }

    private void delData(BulletinText bulletinText, final int position) {
        bulletinText.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    adminBulldapter.removeData(position);
                } else {

                }

            }
        });
    }
}

