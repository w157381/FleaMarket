package com.example.fleamarket.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fleamarket.R;
import com.example.fleamarket.view.BaseActivity;


public class PayActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_money;
    private Button bt_01, bt_02, bt_03;
    private String str_money;

    private Intent data;

    @Override
    protected int initLayout() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.pay_back);
        tv_money = findViewById(R.id.pay_money);
        bt_01 = findViewById(R.id.pay_bt01);
        bt_02 = findViewById(R.id.pay_bt02);
        bt_03 = findViewById(R.id.pay_bt03);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_01.setOnClickListener(this);
        bt_02.setOnClickListener(this);
        bt_03.setOnClickListener(this);

        //初始化
        data = new Intent();
        Intent intent = getIntent();
        str_money = intent.getStringExtra("money");
        tv_money.setText(str_money);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_back:
                data.putExtra("aa","no");
                setResult(2,data);
                finish();
                break;
            case R.id.pay_bt01:
                data.putExtra("aa","ok");
                setResult(2,data);
                data.putExtra("paytype","支付宝");
                finish();

                break;
            case R.id.pay_bt02:
                data.putExtra("aa","ok");
                data.putExtra("paytype","微信");
                setResult(2,data);
                finish();

                break;
            case R.id.pay_bt03:
                data.putExtra("aa","ok");
                data.putExtra("paytype","云闪付");
                setResult(2,data);
                finish();
                break;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            data.putExtra("aa","no");
            setResult(2,data);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
