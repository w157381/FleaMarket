package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.AddressAdapter;
import com.example.fleamarket.bean.MyAddress;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyAddrActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_back;
    private RecyclerView addr_list;
    private Button bt_addMore;
    private MyAddress myAddress;
    private List<MyAddressitem> myAddressitems;
    private AddressAdapter addressAdapter;
    private int positiona = -1;
    //等待界面
    private ZLoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1001) {
                myAddressitems = myAddress.getAddressitems();
                handler.sendEmptyMessage(1002);
            }
            if (msg.what == 1002) {
                addr_list.setLayoutManager(new LinearLayoutManager(MyAddrActivity.this));
                addressAdapter = new AddressAdapter(MyAddrActivity.this, myAddressitems);
                addr_list.setAdapter(addressAdapter);
                addressAdapter.setOnViewClickListener(new AddressAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, MyAddressitem myAddressitem) {

                        positiona = position;
                        Intent intent = new Intent(MyAddrActivity.this, UpdaAddrActivity.class);
                        intent.putExtra("name", myAddressitem.getAddr_name());
                        intent.putExtra("phone", myAddressitem.getAddr_phone());
                        intent.putExtra("type", myAddressitem.getAddr_type());
                        intent.putExtra("addra", myAddressitem.getAddr_address_a());
                        intent.putExtra("addrb", myAddressitem.getAddr_address_b());
                        Log.e("defaulta", "" + myAddressitem.isIsdefault());
                        intent.putExtra("isdefault", myAddressitem.isIsdefault());
                        startActivityForResult(intent, 1);

                    }
                });
            }
            if (msg.what == 404) {

            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_myaddr;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.addr_back);
        bt_addMore = findViewById(R.id.addr_more);
        addr_list = findViewById(R.id.addr_list);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_addMore.setOnClickListener(this);
        myAddress = new MyAddress();
        myAddressitems = new ArrayList<>();
        selectAddr(BmobIM.getInstance().getCurrentUid());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addr_back:
                finish();
                break;
            case R.id.addr_more:
                Intent intent = new Intent(this, AddMyAddressActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 || resultCode == 0) {
            if (!data.getStringExtra("aa").equals("")) {
                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String addra = data.getStringExtra("addra");
                String addrb = data.getStringExtra("addrb");
                String type = data.getStringExtra("type");
                boolean isdefault = data.getBooleanExtra("isdefault", false);

                MyAddressitem myAddressitem = new MyAddressitem();
                myAddressitem.setAddr_name(name);
                myAddressitem.setAddr_phone(phone);
                myAddressitem.setAddr_address_a(addra);
                myAddressitem.setAddr_address_b(addrb);
                myAddressitem.setAddr_type(type);
                myAddressitem.setIsdefault(isdefault);
                if (isdefault) {
                    for (int i = 0; i < myAddress.getAddressitems().size(); i++) {
                        myAddressitems.get(i).setIsdefault(false);
                    }
                    myAddressitems.add(myAddressitem);
                    handler.sendEmptyMessage(1002);
                } else {
                    addressAdapter.addData(myAddressitem);
                }
                ToastEx.success(MyAddrActivity.this, "添加成功！").show();
            } else {

            }

        }
        if (requestCode == 1 || resultCode == 1) {

            if (data.getStringExtra("aa").equals("")) {
                Log.e("1212","null");
            }
            if (data.getStringExtra("aa").equals("aa")) {

                String name = data.getStringExtra("name");
                String phone = data.getStringExtra("phone");
                String addra = data.getStringExtra("addra");
                String addrb = data.getStringExtra("addrb");
                String type = data.getStringExtra("type");
                boolean isdefault = data.getBooleanExtra("isdefault", false);

                MyAddressitem myAddressitem = new MyAddressitem();
                myAddressitem.setAddr_name(name);
                myAddressitem.setAddr_phone(phone);
                myAddressitem.setAddr_address_a(addra);
                myAddressitem.setAddr_address_b(addrb);
                myAddressitem.setAddr_type(type);
                myAddressitem.setIsdefault(isdefault);
                if (isdefault) {
                    for (int i = 0; i < myAddress.getAddressitems().size(); i++) {
                        myAddressitems.get(i).setIsdefault(false);
                    }
                    handler.sendEmptyMessage(1002);
                    addressAdapter.update(positiona, myAddressitem);

                } else {
                    addressAdapter.update(positiona, myAddressitem);
                }
                Log.e("1212","update");
                ToastEx.success(MyAddrActivity.this, "修改成功！").show();
            }
            if (data.getStringExtra("aa").equals("del")) {
                Log.e("1212","delete");
                ToastEx.success(MyAddrActivity.this, "删除成功！").show();
                addressAdapter.removeData(positiona);

            }

        }


    }

    public void selectAddr(String userId) {
        dialog = LoadingUtil.loading(MyAddrActivity.this,"Loading...");
        BmobQuery<MyAddress> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("addr_userId", userId);
        categoryBmobQuery.findObjects(new FindListener<MyAddress>() {
            @Override
            public void done(List<MyAddress> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        myAddress = object.get(0);
                        handler.sendEmptyMessage(1001);
                        dialog.cancel();
                    } else {
                        dialog.cancel();
                        handler.sendEmptyMessage(404);
                        ToastEx.warning(MyAddrActivity.this, "暂无地址信息！").show();

                    }
                } else {
                    dialog.cancel();
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.e("1212", "1212");
        super.onDestroy();
        updateInfo(myAddress);
    }

    public void updateInfo(MyAddress myAddress) {
        if(myAddress.getObjectId().equals("")){
            return;
        }
        myAddress.update(myAddress.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastEx.success(MyAddrActivity.this, "!").show();

                } else {
                    ToastEx.error(MyAddrActivity.this, "? " + e.getMessage()).show();
                }
            }

        });

    }
}
