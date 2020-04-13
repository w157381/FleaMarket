package com.example.fleamarket.view.activity;

import android.content.Intent;
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
import com.example.fleamarket.adapter.ChooseLocaAdapter;
import com.example.fleamarket.bean.MyAddress;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ChooseLocationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView list;
    private Button bt;
    private MyAddress myAddress;
    private ChooseLocaAdapter chooseLocaAdapter;
    private String userId;
    private Intent date;
    private ZLoadingDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1001) {
                list.setLayoutManager(new LinearLayoutManager(ChooseLocationActivity.this));
                chooseLocaAdapter = new ChooseLocaAdapter(ChooseLocationActivity.this, myAddress.getAddressitems());
                list.setAdapter(chooseLocaAdapter);
                chooseLocaAdapter.setOnViewClickListener(new ChooseLocaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, MyAddressitem myAddressitem) {
                        date.putExtra("aa", "ok");
                        date.putExtra("myAddressitem", myAddressitem);
                        setResult(1, date);
                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_chooselocation;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.chooseloca_back);
        list = findViewById(R.id.chooseloca_list);
        bt = findViewById(R.id.chooseloca_bt);

    }

    @Override
    protected void initData() {

        img_back.setOnClickListener(this);
        bt.setOnClickListener(this);
        myAddress = new MyAddress();
        date = new Intent();
        userId = BmobIM.getInstance().getCurrentUid();
        selectLocation(userId);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.chooseloca_back:
                date.putExtra("aa", "no");
                setResult(1, date);
                finish();
                break;
            case R.id.chooseloca_bt:
                IntentUtil.get().goActivity(ChooseLocationActivity.this, MyAddrActivity.class);
                break;

        }
    }

    public void selectLocation(String userId) {
        BmobQuery<MyAddress> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("addr_userId", userId);
        categoryBmobQuery.findObjects(new FindListener<MyAddress>() {
            @Override
            public void done(List<MyAddress> object, BmobException e) {
                if (e == null) {
                    myAddress = object.get(0);
                    handler.sendEmptyMessage(1001);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);//休眠1秒
                    selectLocation(userId);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
