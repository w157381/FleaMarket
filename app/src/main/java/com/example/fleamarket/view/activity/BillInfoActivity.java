package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BillInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private SimpleDraweeView img;
    private TextView tv_info, tv_num, tv_nickname, tv_paytype, tv_money, tv_time;

    private String objectId;
    private OrderForm orderForm;
    private ZLoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                Goods goods = orderForm.getBill_goodsInfo();
                User user = orderForm.getBill_buyUserInfo();
                img.setImageURI(Uri.parse(goods.getGoods_imgs().get(0)));
                tv_info.setText(goods.getGoods_info());
                tv_num.setText(orderForm.getObjectId()+"");
                tv_nickname.setText(user.getUser_nickName());
                tv_paytype.setText("" + orderForm.getBill_paytype());
                tv_money.setText("" + orderForm.getBill_paynum());
                tv_time.setText("" + orderForm.getCreatedAt());
            }
            if (msg.what==1002){
                selectBillInfo(objectId);
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_billinfo;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.bill_back);
        img = findViewById(R.id.bill_gods_img);
        tv_info = findViewById(R.id.bill_goods_info);
        tv_num = findViewById(R.id.bill_num);
        tv_nickname = findViewById(R.id.bill_nickname);
        tv_paytype = findViewById(R.id.bill_paytype);
        tv_money = findViewById(R.id.bill_money);
        tv_time = findViewById(R.id.bill_time);

    }

    @Override
    protected void initData() {

        img_back.setOnClickListener(this);
        orderForm = new OrderForm();
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        dialog = LoadingUtil.loading(BillInfoActivity.this,"Loading...");
        handler.sendEmptyMessageDelayed(1002,1000);
    }

    private void selectBillInfo(String objectId) {
        BmobQuery<OrderForm> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("objectId", objectId);
        categoryBmobQuery.include("bill_buyUserInfo,bill_goodsInfo");
        categoryBmobQuery.findObjects(new FindListener<OrderForm>() {
            @Override
            public void done(List<OrderForm> object, BmobException e) {
                if (e == null) {
                    orderForm = object.get(0);
                    handler.sendEmptyMessage(1001);
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bill_back:
                finish();
                break;
        }
    }
}
