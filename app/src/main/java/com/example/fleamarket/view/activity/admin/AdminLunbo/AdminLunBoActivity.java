package com.example.fleamarket.view.activity.admin.AdminLunbo;

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
import com.example.fleamarket.adapter.admin.AdminLunBodapter;
import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.AnalyzeWebActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminLunBoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private RecyclerView recyclerView;
    private Button bt_add;


    private List<Advertising> advertisingList;
    private AdminLunBodapter adminLunBodapter;

    private ZLoadingDialog dialog;
    private int positiona;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {

                recyclerView.setLayoutManager(new LinearLayoutManager(AdminLunBoActivity.this));
                adminLunBodapter = new AdminLunBodapter(AdminLunBoActivity.this, advertisingList);
                recyclerView.setAdapter(adminLunBodapter);
                adminLunBodapter.setOnViewClickListener(new AdminLunBodapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, Advertising advertising) {
                        Intent intent = new Intent(AdminLunBoActivity.this, AnalyzeWebActivity.class);
                        intent.putExtra("url",advertising.getAdv_url());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClickListener_del(int position,Advertising advertising) {
                        delcctLunboitem(advertising,position);
                    }

                    @Override
                    public void onItemClickListener_upd(int position, Advertising advertising) {
                        positiona = position;
                        Intent intent = new Intent(AdminLunBoActivity.this,AdminUpdLunBoActivity.class);
                        intent.putExtra("advertising",advertising);
                        startActivityForResult(intent, 2);
                    }
                });
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.admin_lunbo;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.admin_lunbo_back);
        recyclerView = findViewById(R.id.admin_lunbo_list);
        bt_add = findViewById(R.id.admin_lunbo_add_bt);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        selectLunBoInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_lunbo_back:
                finish();
                break;
            case R.id.admin_lunbo_add_bt:
                IntentUtil.get().goActivityResult(AdminLunBoActivity.this,AdminAddLunBoInfoActivity.class,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0||resultCode==0){
            if(data.getStringExtra("aa").equals("no")){
                ToastEx.warning(AdminLunBoActivity.this,"取消添加！").show();
            }
            if(data.getStringExtra("aa").equals("ok")){
                Advertising advertising = (Advertising) data.getSerializableExtra("advertising");
                adminLunBodapter.addData(advertising);
                ToastEx.success(AdminLunBoActivity.this,"添加成功！").show();
            }
        }
        if(requestCode==2||resultCode==2){
            if(data.getStringExtra("aa").equals("no")){
                ToastEx.warning(AdminLunBoActivity.this,"取消修改！").show();
            }
            if(data.getStringExtra("aa").equals("ok")){
                Advertising advertising = (Advertising) data.getSerializableExtra("advertising");
                adminLunBodapter.updData(positiona,advertising);
                ToastEx.success(AdminLunBoActivity.this,"修改成功！").show();
            }
        }
    }

    //查询数据
    private void selectLunBoInfo() {
        dialog = LoadingUtil.loading(AdminLunBoActivity.this, "Loading...");
        BmobQuery<Advertising> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<Advertising>() {
            @Override
            public void done(List<Advertising> object, BmobException e) {
                if (e == null) {
                    advertisingList = object;
                    handler.sendEmptyMessage(1001);
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    //删除数据
    private void delcctLunboitem(Advertising advertising, final int position){
        advertising.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    adminLunBodapter.removeData(position);
                    ToastEx.success(AdminLunBoActivity.this,"删除成功！").show();
                }else{
                    ToastEx.error(AdminLunBoActivity.this,"删除失败！"+e.getMessage()).show();
                }

            }
        });
    }
}

