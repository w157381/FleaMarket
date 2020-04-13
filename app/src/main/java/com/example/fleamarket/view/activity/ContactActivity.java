package com.example.fleamarket.view.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.ContactAdapter;
import com.example.fleamarket.bean.PhoneDto;
import com.example.fleamarket.utils.PhoneUtil;
import com.example.fleamarket.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private Intent date;

    private List<PhoneDto> phoneDtos;
    private List<PhoneDto> phoneDtosa;
    @Override
    protected int initLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        //控件获取
        img_back = findViewById(R.id.contact_back);
        searchView = findViewById(R.id.contact_serach);
        recyclerView = findViewById(R.id.contact_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("1212",""+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phoneDtosa.clear();
                for (int i = 0;i<phoneDtos.size();i++){
                    if(phoneDtos.get(i).getName().indexOf(newText) != -1)
                    {
                        phoneDtosa.add(phoneDtos.get(i));
                    }
                }
                selectContact(phoneDtosa);

                return false;
            }
        });

    }

    @Override
    protected void initData() {
        //监听器注册
        img_back.setOnClickListener(this);
        date = new Intent();
        PhoneUtil phoneUtil = new PhoneUtil(this);
        phoneDtos = new ArrayList<>();
        phoneDtos = phoneUtil.getPhone();
        phoneDtosa = new ArrayList<>();
        check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contact_back:
                date.putExtra("aa", "");
                setResult(2, date);
                finish();
                break;
        }
    }
    /**
     * 检查权限
     */
    private void check() {
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 201);
        } else {
            selectContact(phoneDtos);
        }
    }

    private void selectContact(List<PhoneDto> phoneDtos) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter  = new ContactAdapter(this,phoneDtos);
        recyclerView.setAdapter(contactAdapter);
        contactAdapter.setOnViewClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, PhoneDto phoneDtos) {
                date.putExtra("aa", "aa");
                date.putExtra("name",phoneDtos.getName());
                date.putExtra("phone",phoneDtos.getTelPhone());
                setResult(2, date);
                finish();
            }
        });
    }

}
