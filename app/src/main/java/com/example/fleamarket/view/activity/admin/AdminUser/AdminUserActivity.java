package com.example.fleamarket.view.activity.admin.AdminUser;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminUserdapter;
import com.example.fleamarket.bean.User;

import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AdminUserActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_text;
    private Spinner spinner;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private AdminUserdapter adminUserdapter;
    private List<User> users;
    private List<User> usersa;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    //等待界面
    private ZLoadingDialog dialog;
    @Override
    protected int initLayout() {
        return R.layout.admin_user;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_user_back);
        tv_text = findViewById(R.id.admin_user_text);
        spinner = findViewById(R.id.admin_user_spinner);
        searchView = findViewById(R.id.admin_user_search);
        recyclerView = findViewById(R.id.admin_user_list);
    }

    @Override
    protected void initData() {
        users = new ArrayList<>();
        usersa = new ArrayList<>();
        img_back.setOnClickListener(this);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("默认");
        data_list.add("账号冻结用户");
        data_list.add("账号封号用户");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    usersa.clear();
                    setDataList(users);
                    tv_text.setText(""+usersa.size()+"/"+users.size());
                }
                if (position==1){//账号冻结用户  0
                    usersa.clear();
                    for(int i = 0;i<users.size();i++){
                        if(users.get(i).getUser_loginState()==0){
                            usersa.add(users.get(i));
                        }
                    }
                    setDataList(usersa);
                    tv_text.setText(""+usersa.size()+"/"+users.size());

                }
                if (position==2){//账号封号用户  -1
                    usersa.clear();
                    for(int i = 0;i<users.size();i++){
                        if(users.get(i).getUser_loginState()==-1){
                            usersa.add(users.get(i));
                        }
                    }
                    setDataList(usersa);
                    tv_text.setText(""+usersa.size()+"/"+users.size());
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
                usersa.clear();
                for(int i=0;i<users.size();i++){
                    User user = users.get(i);
                    if(user.getUser_stuNumber().indexOf(newText) != -1||user.getUser_name().indexOf(newText)!=-1||user.getUser_department().indexOf(newText) !=-1){
                        usersa.add(user);
                    }
                }
                setDataList(usersa);
                tv_text.setText(""+usersa.size()+"/"+users.size());
                return false;
            }
        });
        selectUserInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_user_back:
                finish();
                break;

        }
    }

    private void selectUserInfo(){

        dialog = LoadingUtil.loading(AdminUserActivity.this,"Loading...");
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    users = object;
                    setDataList(users);
                    tv_text.setText(""+users.size());
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void setDataList(List<User> userList){
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUserActivity.this));
        adminUserdapter = new AdminUserdapter(AdminUserActivity.this,userList);
        recyclerView.setAdapter(adminUserdapter);
        adminUserdapter.setOnViewClickListener(new AdminUserdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, User user) {
                Intent intent = new Intent(AdminUserActivity.this,AdminUserInfoActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

    }
}

