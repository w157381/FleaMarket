package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;

public class UpdaAddrActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_back;
    private TextView tv_del;
    private EditText et_name;
    private ImageView img_contact;
    private EditText et_phone;
    private EditText et_location_a;
    private ImageView img_location;
    private EditText et_location_b;
    private RadioGroup type;
    private RadioButton rb01, rb02, rb03;
    private String str_type = "";
    private SwitchCompat switchCompat;
    private boolean is_switch = false;
    private Button bt_save;
    private Intent date;
    private MyAddressitem myAddressitem;


    @Override
    protected int initLayout() {
        return R.layout.activity_updamyaddr;
    }

    @Override
    protected void initView() {
        //控件获取
        img_back = findViewById(R.id.upaddr_back);
        tv_del = findViewById(R.id.upaddr_del);
        et_name = findViewById(R.id.upaddr_name);
        img_contact = findViewById(R.id.upaddr_img_contact);
        et_phone = findViewById(R.id.upaddr_phone);
        et_location_a = findViewById(R.id.upaddr_locationa);
        img_location = findViewById(R.id.upaddr_img_location);
        et_location_b = findViewById(R.id.upaddr_locationb);
        type = findViewById(R.id.upaddr_RadioGroup1);
        rb01 = findViewById(R.id.upaddr_radio01);
        rb02 = findViewById(R.id.upaddr_radio02);
        rb03 = findViewById(R.id.upaddr_radio03);
        switchCompat = findViewById(R.id.upaddr_switch);
        bt_save = findViewById(R.id.upaddr_save);

    }

    @Override
    protected void initData() {
        //监听器注册
        img_back.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        img_contact.setOnClickListener(this);
        et_location_a.setOnClickListener(this);
        img_location.setOnClickListener(this);
        bt_save.setOnClickListener(this);

        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton RB = (RadioButton) findViewById(i);//获取被选择的单选按钮
                str_type = RB.getText().toString();

            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_switch = isChecked;
            }
        });
        //数据初始化
        date = new Intent();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String type = intent.getStringExtra("type");
        String addra = intent.getStringExtra("addra");
        String addrb = intent.getStringExtra("addrb");
        boolean defaulta = intent.getBooleanExtra("isdefault", false);
        et_name.setText(name);
        et_phone.setText(phone);
        et_location_a.setText(addra);
        et_location_b.setText(addrb);
        if (type.equals("家")) {
            rb01.setChecked(true);
            str_type = "家";

        } else if (type.equals("学校")) {
            rb02.setChecked(true);
            str_type = "学校";
        } else {
            rb03.setChecked(true);
            str_type = "公司";
        }
        switchCompat.setChecked(defaulta);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upaddr_back:
                date.putExtra("aa", "");
                setResult(1, date);
                finish();
                break;
            case R.id.upaddr_save:
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                String loca_a = et_location_a.getText().toString();
                String loca_b = et_location_b.getText().toString();
                if (name.equals("")) {
                    ToastEx.warning(UpdaAddrActivity.this, "请填写收货人姓名！").show();
                    return;
                }
                if (phone.equals("")) {
                    ToastEx.warning(UpdaAddrActivity.this, "请填写收货人手机号码！").show();
                    return;
                }
                if (loca_a.equals("")) {
                    ToastEx.warning(UpdaAddrActivity.this, "请填写地址！").show();
                    return;
                }
                if (loca_b.equals("")) {
                    ToastEx.warning(UpdaAddrActivity.this, "请补充详细地址！").show();
                    return;
                }
                myAddressitem = new MyAddressitem();
                myAddressitem.setAddr_name(name);
                myAddressitem.setAddr_phone(phone);
                myAddressitem.setAddr_address_a(loca_a);
                myAddressitem.setAddr_address_b(loca_b);
                myAddressitem.setAddr_type(str_type);
                myAddressitem.setIsdefault(is_switch);

                date.putExtra("aa", "aa");
                date.putExtra("name", myAddressitem.getAddr_name());
                date.putExtra("phone", myAddressitem.getAddr_phone());
                date.putExtra("addra", myAddressitem.getAddr_address_a());
                date.putExtra("addrb", myAddressitem.getAddr_address_b());
                date.putExtra("type", myAddressitem.getAddr_type());
                date.putExtra("isdefault", myAddressitem.isIsdefault());

                //当前activity销毁时，data会传递给上一级activity
                setResult(1, date);
                //销毁当前activity
                finish();
                break;
            case R.id.upaddr_img_contact:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.upaddr_locationa:

                break;
            case R.id.upaddr_img_location:

                break;
            case R.id.upaddr_del:
                date.putExtra("aa", "del");
                setResult(1, date);
                finish();
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2||resultCode==2){
            Log.e("1212","success");
            if (data.getStringExtra("aa").equals("")){
                Log.e("1212","null");
            }
            if(data.getStringExtra("aa").equals("aa")){
                Log.e("1212","!null");
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                et_name.setText(""+name);
                et_phone.setText(""+phone);
            }

        }
    }
}
