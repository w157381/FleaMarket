package com.example.fleamarket.view.activity.admin.AdminBill;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminBillAdapter;

import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.constants.Constants;

import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AdminBillActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_text;
    private Spinner spinner;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private AdminBillAdapter adminBillAdapter;
    private List<OrderForm> orderFormList;
    private List<OrderForm> orderFormLista;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    private int positiona;

    //等待界面
    private ZLoadingDialog dialog;

    @Override
    protected int initLayout() {
        return R.layout.admin_bill;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_bill_back);
        tv_text = findViewById(R.id.admin_bill_text);
        spinner = findViewById(R.id.admin_bill_spinner);
        searchView = findViewById(R.id.admin_bill_search);
        recyclerView = findViewById(R.id.admin_bill_list);
    }

    @Override
    protected void initData() {
        orderFormList = new ArrayList<>();
        orderFormLista = new ArrayList<>();
        img_back.setOnClickListener(this);

        //数据
        data_list = new ArrayList<String>();
        data_list.add(Constants.DEFAULT);
        data_list.add(Constants.BIll_F1);
        data_list.add(Constants.BIll_0);
        data_list.add(Constants.BIll_1);
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    orderFormLista.clear();
                    setDataList(orderFormList);
                    tv_text.setText(orderFormList.size() + "");
                }
                if (position == 1) {//  -1交易失败
                    orderFormLista.clear();
                    for (int i = 0; i < orderFormList.size(); i++) {
                        if (orderFormList.get(i).getBill_state() == -1) {
                            orderFormLista.add(orderFormList.get(i));
                        }
                    }
                    setDataList(orderFormLista);
                    tv_text.setText("" + orderFormLista.size() + "/" + orderFormList.size());
                }
                if (position == 2) {//  0交易中
                    orderFormLista.clear();
                    for (int i = 0; i < orderFormList.size(); i++) {
                        if (orderFormList.get(i).getBill_state() == 0) {
                            orderFormLista.add(orderFormList.get(i));
                        }
                    }
                    setDataList(orderFormLista);
                    tv_text.setText("" + orderFormLista.size() + "/" + orderFormList.size());
                }
                if (position == 3) {//  1交易成功
                    orderFormLista.clear();
                    for (int i = 0; i < orderFormList.size(); i++) {
                        if (orderFormList.get(i).getBill_state() == 1) {
                            orderFormLista.add(orderFormList.get(i));
                        }
                    }
                    setDataList(orderFormLista);
                    tv_text.setText("" + orderFormLista.size() + "/" + orderFormList.size());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderFormLista.clear();
                for (int i = 0; i < orderFormList.size(); i++) {
                    OrderForm bill = orderFormList.get(i);
                    if (bill.getBill_goodsInfo().getGoods_type().indexOf(newText) != -1 || bill.getBill_goodsInfo().getGoods_name().indexOf(newText) != -1 ||
                            bill.getBill_goodsInfo().getGoods_info().indexOf(newText) != -1 || bill.getBill_buyUserId().indexOf(newText) != -1 ||
                            bill.getBill_buyUserInfo().getUser_name().indexOf(newText) != -1 || bill.getBill_sellUserId().indexOf(newText) != -1 ||
                            bill.getBill_sellUserInfo().getUser_name().indexOf(newText) != -1) {
                        orderFormLista.add(bill);
                    }
                }
                setDataList(orderFormLista);
                tv_text.setText("" + orderFormLista.size() + "/" + orderFormList.size());
                return false;
            }
        });
        selectBillInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_bill_back:
                finish();
                break;

        }
    }

    private void selectBillInfo() {

        dialog = LoadingUtil.loading(AdminBillActivity.this, "Loading...");
        BmobQuery<OrderForm> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.include("bill_buyUserInfo,bill_sellUserInfo,bill_goodsInfo");
        categoryBmobQuery.findObjects(new FindListener<OrderForm>() {
            @Override
            public void done(List<OrderForm> object, BmobException e) {
                if (e == null) {
                    orderFormList = object;
                    setDataList(orderFormList);
                    tv_text.setText("" + orderFormList.size());
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    private void setDataList(List<OrderForm> userList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminBillActivity.this));
        adminBillAdapter = new AdminBillAdapter(AdminBillActivity.this, userList);
        recyclerView.setAdapter(adminBillAdapter);
        adminBillAdapter.setOnViewClickListener(new AdminBillAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, OrderForm orderForm) {

                positiona =position;
                Intent intent = new Intent(AdminBillActivity.this,AdminBillInfoActivity.class);
                intent.putExtra("orderForm",orderForm);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0||resultCode==0){
            if(data.getStringExtra("aa").equals("no")){

            }
            if(data.getStringExtra("aa").equals("ok")){
                adminBillAdapter.removeData(positiona);
                ToastEx.success(AdminBillActivity.this,"删除成功！").show();
                tv_text.setText(orderFormList.size()+"");
            }
        }
    }
}
