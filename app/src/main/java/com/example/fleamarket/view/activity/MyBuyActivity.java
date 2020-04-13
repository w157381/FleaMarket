package com.example.fleamarket.view.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.MyBuyAdapter;
import com.example.fleamarket.bean.MyAddress;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.constants.Constants;
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

public class MyBuyActivity extends BaseActivity implements View.OnClickListener {

    private ImageView img_back;
    private RecyclerView recyclerView;

    private ZLoadingDialog dialog;
    private MyBuyAdapter myBuyAdapter;
    private List<OrderForm> orderFormList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1001) {
                List<OrderForm> aaa = new ArrayList<>();
                for (int i = 0; i < orderFormList.size(); i++) {
                    if (orderFormList.get(i).isDis_buy()) {
                        aaa.add(orderFormList.get(i));
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(MyBuyActivity.this));
                myBuyAdapter = new MyBuyAdapter(MyBuyActivity.this, aaa);
                recyclerView.setAdapter(myBuyAdapter);
                myBuyAdapter.setOnViewClickListener(new MyBuyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener_quxiao(int position, OrderForm orderForm) {

                        ToastEx.warning(MyBuyActivity.this, "待完成！").show();
                    }

                    @Override
                    public void onItemClickListener_del(int position, OrderForm orderForm) {

                    }

                    @Override
                    public void onItemClickListener_sure(int position, OrderForm orderForm, MyBuyAdapter.ViewHolder holder) {
                        orderForm.setBill_state(1);
                        updateData(orderForm,position,holder);
                    }

                    @Override
                    public void onItemClickListener_view(int position, OrderForm orderForm) {
                        ToastEx.warning(MyBuyActivity.this, "待完成！").show();

                    }
                });
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_mybuy;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.mybuy_back);
        recyclerView = findViewById(R.id.mybuy_list);

    }

    @Override
    protected void initData() {

        img_back.setOnClickListener(this);

        orderFormList = new ArrayList<>();

        selectBill();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mybuy_back:
                finish();
                break;

        }

    }

    private void selectBill() {
        dialog = LoadingUtil.loading(MyBuyActivity.this, "Loading...");
        BmobQuery<OrderForm> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("bill_buyUserId", BmobIM.getInstance().getCurrentUid());
        categoryBmobQuery.include("bill_buyUserInfo,bill_goodsInfo");
        categoryBmobQuery.findObjects(new FindListener<OrderForm>() {
            @Override
            public void done(List<OrderForm> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        orderFormList = object;
                        handler.sendEmptyMessage(1001);
                        dialog.cancel();
                    } else {
                        dialog.cancel();
                        handler.sendEmptyMessage(404);
                    }
                } else {
                    dialog.cancel();
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void updateData(OrderForm orderForm, int position, final MyBuyAdapter.ViewHolder holder){
        orderForm.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    holder.state.setText(Constants.BIll_1);
                    holder.del.setVisibility(View.VISIBLE);
                }else{

                }
            }
        });

    }
}
