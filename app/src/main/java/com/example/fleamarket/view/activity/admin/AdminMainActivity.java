package com.example.fleamarket.view.activity.admin;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fleamarket.R;
import com.example.fleamarket.constants.Constants;

import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.admin.AdminBill.AdminBillActivity;
import com.example.fleamarket.view.activity.admin.AdminBullText.AdminBullTextActivity;
import com.example.fleamarket.view.activity.admin.AdminGoods.AdminGoodsActivity;
import com.example.fleamarket.view.activity.admin.AdminLunbo.AdminLunBoActivity;
import com.example.fleamarket.view.activity.admin.AdminUser.AdminUserActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;

public class AdminMainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private Button bt_user, bt_lunbo, bt_bulltext, bt_goods, bt_bill;


    //等待界面
    private ZLoadingDialog dialog;
    @Override
    protected int initLayout() {
        return R.layout.admin_main;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_main_back);
        bt_user = findViewById(R.id.admin_main_bt_userinfo);
        bt_lunbo = findViewById(R.id.admin_main_bt_lunbo);
        bt_bulltext = findViewById(R.id.admin_main_bt_bulltext);
        bt_goods = findViewById(R.id.admin_main_bt_goodsinfo);
        bt_bill = findViewById(R.id.admin_main_bt_bill);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_user.setOnClickListener(this);
        bt_lunbo.setOnClickListener(this);
        bt_bulltext.setOnClickListener(this);
        bt_goods.setOnClickListener(this);
        bt_bill.setOnClickListener(this);

        bt_user.setVisibility(View.GONE);
        bt_lunbo.setVisibility(View.GONE);
        bt_bulltext.setVisibility(View.GONE);
        bt_goods.setVisibility(View.GONE);
        bt_bill.setVisibility(View.GONE);

        dialog = LoadingUtil.loading(AdminMainActivity.this,"Connect...");
        BmobIM.connect(Constants.ADMIN_ID, new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    //连接成功
                    bt_user.setVisibility(View.VISIBLE);
                    bt_lunbo.setVisibility(View.VISIBLE);
                    bt_bulltext.setVisibility(View.VISIBLE);
                    bt_goods.setVisibility(View.VISIBLE);
                    bt_bill.setVisibility(View.VISIBLE);
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setUserId(Constants.ADMIN_ID);
                    info.setName("系统管理员");
                    info.setAvatar("http://cloud.taest.club/2020/04/12/5ef28dc2401933b5800e0c5dc8a0d9c3.png");
                    BmobIM.getInstance().updateUserInfo(info);

                    dialog.cancel();
                    ToastEx.success(AdminMainActivity.this,"服务器连接成功!"+BmobIM.getInstance().getCurrentUid()).show();
                } else {
                    //连接失败
                    Log.e("connect","服务器连接失败！"+BmobIM.getInstance().getCurrentUid());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_main_back:
                finish();
                break;
            case R.id.admin_main_bt_userinfo:
                IntentUtil.get().goActivity(AdminMainActivity.this, AdminUserActivity.class);
                break;
            case R.id.admin_main_bt_lunbo:
                IntentUtil.get().goActivity(AdminMainActivity.this, AdminLunBoActivity.class);
                break;
            case R.id.admin_main_bt_bulltext:
                IntentUtil.get().goActivity(AdminMainActivity.this, AdminBullTextActivity.class);
                break;
            case R.id.admin_main_bt_goodsinfo:
                IntentUtil.get().goActivity(AdminMainActivity.this, AdminGoodsActivity.class);
                break;
            case R.id.admin_main_bt_bill:
                IntentUtil.get().goActivity(AdminMainActivity.this, AdminBillActivity.class);
                break;
        }

    }
}

