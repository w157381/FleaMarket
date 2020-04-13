package com.example.fleamarket.view.activity.admin.AdminBullText;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.admin.AdminAddBullAdapter;
import com.example.fleamarket.bean.BulletinText;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.LoginActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AdminAddBullTextActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rela;
    private ImageView img_back;
    private SearchView searchView;
    private EditText et_goodsid, et_title;
    private RecyclerView recyclerView;
    private Button bt;

    private BulletinText bulletinText;

    private List<Goods> goodsList;
    private List<Goods> goodsLista;

    private AdminAddBullAdapter adminAddBullAdapter;


    private Intent data;
    private ZLoadingDialog dialog;


    @Override
    protected int initLayout() {
        return R.layout.admin_bulltext_add;
    }

    @Override
    protected void initView() {
        rela = findViewById(R.id.admin_addbull_rela);
        img_back = findViewById(R.id.admin_addbull_back);
        searchView = findViewById(R.id.admin_addbull_search);
        et_title = findViewById(R.id.admin_addbull_title);
        et_goodsid = findViewById(R.id.admin_addbull_goods_id);
        recyclerView = findViewById(R.id.admin_addbull_list);
        bt = findViewById(R.id.admin_addbull_submit);
    }

    @Override
    protected void initData() {

        data = new Intent();
        bulletinText = new BulletinText();
        goodsList = new ArrayList<>();
        goodsLista = new ArrayList<>();

        img_back.setOnClickListener(this);
        bt.setOnClickListener(this);
        rela.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                goodsLista.clear();
                for (int i=0;i<goodsList.size();i++){
                    Goods goods = goodsList.get(i);
                    if (goods.getGoods_name().indexOf(newText) != -1 || goods.getGoods_info().indexOf(newText) != -1|| goods.getGoods_type().indexOf(newText) != -1|| goods.getGoods_user().getUser_name().indexOf(newText) != -1|| goods.getGoods_user().getUser_stuNumber().indexOf(newText) != -1) {
                        goodsLista.add(goods);

                    }
                }
                setData(goodsLista);
                Log.e("1212","1212goodsLista");
                return false;
            }
        });

        selectGoodsAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_addbull_back:
                data.putExtra("aa", "no");
                setResult(0, data);
                finish();
                break;

            case R.id.admin_addbull_submit:
                String title = et_title.getText().toString().trim();
                String id = et_goodsid.getText().toString().trim();
                if (title.equals("")) {
                    ToastEx.warning(AdminAddBullTextActivity.this, "标题不能为空！").show();
                    return;
                }
                if (id.equals("")) {
                    ToastEx.warning(AdminAddBullTextActivity.this, "请选择商品！").show();
                    return;
                }
                bulletinText.setTitle(title);
                bulletinText.setGoods_objectId(id);
                bulletinText.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            data.putExtra("aa","ok");
                            data.putExtra("bulletinText",bulletinText);
                            setResult(0,data);
                            finish();
                        } else {
                            Log.e("BMOB",e.getMessage());
                        }

                    }
                });

                break;

        }
    }

    //商品数据查询
    private void selectGoodsAll() {
        //等待界面
        dialog = LoadingUtil.loading(AdminAddBullTextActivity.this, "Loding...");
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("goods_user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    goodsList = object;
                    dialog.cancel();
                    setData(goodsList);
                } else {
                    Log.e("BMOB123", e.toString());
                }
            }
        });
    }

    private void setData( List<Goods> goodsList) {

        rela.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminAddBullTextActivity.this));
        adminAddBullAdapter = new AdminAddBullAdapter(AdminAddBullTextActivity.this, goodsList);
        recyclerView.setAdapter(adminAddBullAdapter);
        adminAddBullAdapter.setOnViewClickListener(new AdminAddBullAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Goods goods) {
                et_goodsid.setText(goods.getObjectId());
                bulletinText.setImgUrl(goods.getGoods_imgs().get(0));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            data.putExtra("aa", "no");
            setResult(0, data);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}

