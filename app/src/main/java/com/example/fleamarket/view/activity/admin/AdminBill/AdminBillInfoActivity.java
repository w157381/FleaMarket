package com.example.fleamarket.view.activity.admin.AdminBill;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminBillAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.utils.admin.MutiProgress;
import com.example.fleamarket.view.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminBillInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private MutiProgress mutiProgress;
    private SimpleDraweeView goodsImg;
    private TextView tv_goodsType, tv_goodsInfo, tv_goodsPrice, tv_namePhone, tv_location, tv_billNum, tv_billTime, tv_payType, tv_payState, tv_payMoney, tv_buyerInfo, tv_sellerInfo, tv_billState_buy, tv_billState_sell;
    private Button bt_del;
    //等待界面
    private ZLoadingDialog dialog;
    private Intent data;
    private OrderForm orderForm;


    @Override
    protected int initLayout() {
        return R.layout.admin_bill_info;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_bill_info_back);
        mutiProgress = findViewById(R.id.admin_bill_info_progress);
        goodsImg = findViewById(R.id.admin_bill_info_goods_img);
        tv_goodsType = findViewById(R.id.admin_bill_info_goods_type);
        tv_goodsInfo = findViewById(R.id.admin_bill_info_goods_info);
        tv_goodsPrice = findViewById(R.id.admin_bill_info_goods_price);
        tv_namePhone = findViewById(R.id.admin_bill_info_name_phone);
        tv_location = findViewById(R.id.admin_bill_info_location);
        tv_billNum = findViewById(R.id.admin_bill_info_billnum);
        tv_billTime = findViewById(R.id.admin_bill_info_billtime);
        tv_payType = findViewById(R.id.admin_bill_info_paytype);
        tv_payState = findViewById(R.id.admin_bill_info_paystate);
        tv_payMoney = findViewById(R.id.admin_bill_info_paymoney);
        tv_buyerInfo = findViewById(R.id.admin_bill_info_buyer);
        tv_sellerInfo = findViewById(R.id.admin_bill_info_seller);
        tv_billState_buy = findViewById(R.id.admin_bill_info_bill_buyer);
        tv_billState_sell = findViewById(R.id.admin_bill_info_bill_seller);
        bt_del = findViewById(R.id.admin_bill_info_del);
    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_del.setOnClickListener(this);

        data = new Intent();
        Intent intent = getIntent();
        orderForm = new OrderForm();
        orderForm = (OrderForm) intent.getSerializableExtra("orderForm");
        //    //发货状态 0:待发货  1：已发货  2:已收货
        if (orderForm.isBill_payState()) {
            mutiProgress.setCurrNodeNO(0);
        }
        if (orderForm.getBill_fahuoState() == 0) {
            mutiProgress.setCurrNodeNO(1);
        }
        if (orderForm.getBill_fahuoState() == 1) {
            mutiProgress.setCurrNodeNO(2);
        }
        if (orderForm.getBill_fahuoState() == 2) {
            mutiProgress.setCurrNodeNO(3);
        }
        if (orderForm.getBill_state() == 1) {
            mutiProgress.setCurrNodeNO(4);
        }

        // tv_payState,tv_payMoney,tv_buyerInfo,tv_sellerInfo,tv_billState_buy,tv_billState_sell;
        //
        Goods goods = orderForm.getBill_goodsInfo();
        goodsImg.setImageURI(goods.getGoods_imgs().get(0));
        tv_goodsType.setText("" + goods.getGoods_type());
        tv_goodsPrice.setText("￥" + goods.getGoods_price());
        tv_goodsInfo.setText("" + goods.getGoods_info());
        tv_namePhone.setText("" + orderForm.getBill_buyAddr().getAddr_name() + " " + orderForm.getBill_buyAddr().getAddr_phone());
        tv_location.setText("" + orderForm.getBill_buyAddr().getAddr_address_a() + " " + orderForm.getBill_buyAddr().getAddr_address_b());
        tv_billNum.setText(orderForm.getObjectId() + "");
        tv_billTime.setText(orderForm.getCreatedAt() + "");
        tv_payType.setText("" + orderForm.getBill_paytype());
        if (orderForm.isBill_payState()) {
            tv_payState.setText("已支付");
            tv_payMoney.setText(orderForm.getBill_paynum() + "");
        } else {
            tv_payState.setText("未支付");
            tv_payMoney.setText("");
        }

        tv_buyerInfo.setText("" + orderForm.getBill_buyUserInfo().getUser_name() + " " + orderForm.getBill_buyUserId());
        tv_sellerInfo.setText("" + orderForm.getBill_sellUserInfo().getUser_name() + " " + orderForm.getBill_sellUserId());
        if (orderForm.isDis_buy()) {
            tv_billState_buy.setText("订单存在");
        } else {
            tv_billState_buy.setText("订单删除");
        }
        if (orderForm.isDis_sell()) {
            tv_billState_sell.setText("订单存在");
        } else {
            tv_billState_sell.setText("订单删除");
        }

        if (orderForm.isDis_buy() || orderForm.isDis_sell()) {
            bt_del.setVisibility(View.GONE);
        } else {
            bt_del.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_bill_info_back:
                data.putExtra("aa", "no");
                setResult(0, data);
                finish();
                break;
            case R.id.admin_bill_info_del:

                delBill(orderForm);
                break;
        }

    }

    private void delBill(OrderForm orderForm) {
        orderForm.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    data.putExtra("aa", "ok");
                    setResult(0, data);
                    finish();
                } else {
                    ToastEx.error(AdminBillInfoActivity.this,"失败："+e.getMessage()).show();

                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            data.putExtra("aa", "no");
            setResult(0, data);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}