package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.Md5util;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePassActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private EditText et_pass01, et_pass02;
    private Button bt;

    private String userId;
    private String type;
    private User user;


    @Override
    protected int initLayout() {
        return R.layout.activity_change_pass;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.change_pass_back);
        et_pass01 = findViewById(R.id.change_pass_pass_01);
        et_pass02 = findViewById(R.id.change_pass_pass_02);
        bt = findViewById(R.id.change_pass_bt);
    }

    @Override
    protected void initData() {

        bt.setEnabled(false);
        img_back.setOnClickListener(this);
        bt.setOnClickListener(this);
        //获取需要修改账户Id
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        type = intent.getStringExtra("type");
        selectUserInfo(userId);
        user = new User();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_pass_back:
                if(type.equals("type01")){
                    finish();
                }
                if(type.equals("type02")){
                    Intent date = new Intent();
                    date.putExtra("aa","0");
                    setResult(0, date);
                    finish();
                }
                break;
            case R.id.change_pass_bt:
                String pass01 = et_pass01.getText().toString().trim();
                String pass02 = et_pass02.getText().toString().trim();
                if (!pass01.equals(pass02)) {
                    ToastEx.warning(ChangePassActivity.this, "两次密码不一致！").show();
                    return;
                }
                user.setUser_password(Md5util.getMD5(pass01));
                updateUserInfo();
                break;
        }

    }

    public void selectUserInfo(String userId) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", userId);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        user = object.get(0);
                        bt.setEnabled(true);

                    } else {

                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    private void updateUserInfo() {
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if(type.equals("type01")){
                        ToastEx.success(ChangePassActivity.this, "密码修改成功！").show();
                        finish();
                    }
                    if(type.equals("type02")){
                        ToastEx.success(ChangePassActivity.this, "密码修改成功,请重新登录！").show();
                        Intent date = new Intent();
                        date.putExtra("aa","ok");
                        setResult(0, date);
                        finish();
                    }

                } else {
                    ToastEx.warning(ChangePassActivity.this, "密码修改失败！").show();
                }
            }
        });

    }
}
