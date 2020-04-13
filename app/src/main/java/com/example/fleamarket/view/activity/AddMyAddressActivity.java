package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.example.fleamarket.R;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.utils.GetLocationUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;


public class AddMyAddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private EditText et_name;
    private ImageView img_contact;
    private EditText et_phone;
    private EditText et_location_a;
    private ImageView img_location;
    private EditText et_location_b;
    private RadioGroup type;
    private String str_type="";
    private SwitchCompat switchCompat;
    private boolean is_switch =false;
    private Button bt_save;
    private Intent date;

    private MyAddressitem myAddressitem;


    @Override
    protected int initLayout() {
        return R.layout.activity_addmyaddr;
    }

    @Override
    protected void initView() {
        //控件获取
        img_back = findViewById(R.id.addaddr_back);
        et_name = findViewById(R.id.addaddr_name);
        img_contact = findViewById(R.id.addaddr_img_contact);
        et_phone = findViewById(R.id.addaddr_phone);
        et_location_a = findViewById(R.id.addaddr_locationa);
        img_location = findViewById(R.id.addaddr_img_location);
        et_location_b = findViewById(R.id.addaddr_locationb);
        type = findViewById(R.id.addaddr_RadioGroup1);
        switchCompat = findViewById(R.id.addaddr_switch);
        bt_save = findViewById(R.id.addaddr_save);

    }

    @Override
    protected void initData() {
        //监听器注册
        img_back.setOnClickListener(this);
        img_contact.setOnClickListener(this);
        img_location.setOnClickListener(this);
        et_location_a.setOnClickListener(this);
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
        date = new Intent();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addaddr_back:
                date.putExtra("aa","");
                setResult(0, date);
                finish();
                break;
            case R.id.addaddr_img_contact:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivityForResult(intent, 2);

                break;
            case R.id.addaddr_img_location:
                et_location_a.setText(GetLocationUtil.getLocation(AddMyAddressActivity.this));
                break;
            case R.id.addaddr_locationa:

                CityPickerView mCityPickerView = new CityPickerView(this);
                // 设置点击外部是否消失
                mCityPickerView.setCancelable(true);
                // 设置滚轮字体大小
                mCityPickerView.setTextSize(18f);
                // 设置标题
                mCityPickerView.setTitle("选择地址");
                // 设置取消文字
                mCityPickerView.setCancelText("取消");
                // 设置取消文字颜色
                mCityPickerView.setCancelTextColor(Color.GRAY);
                // 设置取消文字大小
                mCityPickerView.setCancelTextSize(14f);
                // 设置确定文字
                mCityPickerView.setSubmitText("确定");
                // 设置确定文字颜色
                mCityPickerView.setSubmitTextColor(Color.BLACK);
                // 设置确定文字大小
                mCityPickerView.setSubmitTextSize(14f);
                // 设置头部背景
                mCityPickerView.setHeadBackgroundColor(Color.WHITE);
                mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener() {
                    @Override
                    public void onCitySelect(String prov, String city, String area) {
                        // 省、市、区 分开获取
                        Log.e("11111111", "省: " + prov + " 市: " + city + " 区: " + area);
                    }

                    @Override
                    public void onCitySelect(String str) {

                        et_location_a.setText(str);
                    }
                });
                mCityPickerView.show();
                break;
            case R.id.addaddr_save:
                String name = et_name.getText().toString();
                String phone = et_phone.getText().toString();
                String loca_a = et_location_a.getText().toString();
                String loca_b = et_location_b.getText().toString();
                if (name.equals("")) {
                    ToastEx.warning(AddMyAddressActivity.this, "请填写收货人姓名！").show();
                    return;
                }
                if (phone.equals("")) {
                    ToastEx.warning(AddMyAddressActivity.this, "请填写收货人手机号码！").show();
                    return;
                }
                if (loca_a.equals("")) {
                    ToastEx.warning(AddMyAddressActivity.this, "请填写地址！").show();
                    return;
                }
                if (loca_b.equals("")) {
                    ToastEx.warning(AddMyAddressActivity.this, "请补充详细地址！").show();
                    return;
                }
                myAddressitem = new MyAddressitem();
                myAddressitem.setAddr_name(name);
                myAddressitem.setAddr_phone(phone);
                myAddressitem.setAddr_address_a(loca_a);
                myAddressitem.setAddr_address_b(loca_b);
                myAddressitem.setAddr_type(str_type);
                myAddressitem.setIsdefault(is_switch);

                date.putExtra("aa","aa");
                date.putExtra("name", myAddressitem.getAddr_name());
                date.putExtra("phone", myAddressitem.getAddr_phone());
                date.putExtra("addra", myAddressitem.getAddr_address_a());
                date.putExtra("addrb", myAddressitem.getAddr_address_b());
                date.putExtra("type", myAddressitem.getAddr_type());
                date.putExtra("isdefault", myAddressitem.isIsdefault());
                //当前activity销毁时，data会传递给上一级activity
                setResult(0, date);
                //销毁当前activity
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
