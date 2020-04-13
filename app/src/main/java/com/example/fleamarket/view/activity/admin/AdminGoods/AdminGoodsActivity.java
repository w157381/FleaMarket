package com.example.fleamarket.view.activity.admin.AdminGoods;

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
import com.example.fleamarket.adapter.admin.AdminGoodsAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminGoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_text;
    private Spinner spinner;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private List<Goods> goodsList;
    private List<Goods> goodsLista;
    private AdminGoodsAdapter adminGoodsAdapter;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private int positiona;

    //等待界面
    private ZLoadingDialog dialog;

    @Override
    protected int initLayout() {
        return R.layout.admin_goods;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_goods_back);
        tv_text = findViewById(R.id.admin_goods_text);
        spinner = findViewById(R.id.admin_goods_spinner);
        searchView = findViewById(R.id.admin_goods_search);
        recyclerView = findViewById(R.id.admin_goods_list);
    }

    @Override
    protected void initData() {
        goodsList = new ArrayList<>();
        goodsLista = new ArrayList<>();
        img_back.setOnClickListener(this);

        //数据
        data_list = new ArrayList<String>();
        data_list.add("默认");
        data_list.add("已下架");//-1:下架
        data_list.add("已售出");//0：售出
        data_list.add("出售中");//1：出售中
        data_list.add("待审核");// 2:待审核
        data_list.add("已驳回");//3 :已驳回
        data_list.add("已删除");//3 :已驳回

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
                    goodsLista.clear();
                    setDataList(goodsList);
                    tv_text.setText("" + goodsList.size());
                }

                if (position == 1) {//-1:下架
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == -1) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

                }
                if (position == 2) {//0：售出
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == 0) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

                }
                if (position == 3) {//1：出售中
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == 1) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

                }
                if (position == 4) {//2:待审核
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == 2) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

                }
                if (position == 5) {//3 :已驳回
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == 3) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

                }
                if (position == 6) {//-2:已删除
                    goodsLista.clear();
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (goodsList.get(i).getGoods_state() == -2) {
                            goodsLista.add(goodsList.get(i));
                        }
                    }
                    setDataList(goodsLista);
                    tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());

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
                goodsLista.clear();
                for (int i = 0; i < goodsList.size(); i++) {
                    Goods goods = goodsList.get(i);
                    if (goods.getGoods_name().indexOf(newText) != -1 || goods.getGoods_info().indexOf(newText) != -1 || goods.getGoods_type().indexOf(newText) != -1 || goods.getGoods_user().getUser_name().indexOf(newText) != -1 || goods.getGoods_user().getUser_stuNumber().indexOf(newText) != -1) {
                        goodsLista.add(goods);

                    }
                }
                setDataList(goodsLista);
                tv_text.setText("" + goodsLista.size() + "/" + goodsList.size());
                return false;
            }
        });
        selectGoodsAll();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_goods_back:
                finish();
                break;

        }
    }

    //商品数据查询
    private void selectGoodsAll() {
        //等待界面
        dialog = LoadingUtil.loading(AdminGoodsActivity.this, "Loding...");
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("goods_user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    goodsList = object;
                    dialog.cancel();
                    setDataList(goodsList);
                } else {
                    Log.e("BMOB123", e.toString());
                }
            }
        });
    }

    private void delData(final Goods goods, final int position){
        goods.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    adminGoodsAdapter.removeData(position);
                    goodsList.remove(goods);
                    tv_text.setText("" + goodsList.size());
                    ToastEx.success(AdminGoodsActivity.this,"删除成功！").show();
                }else{

                }
            }
        });
    }

    private void setDataList(List<Goods> goods) {

        recyclerView.setLayoutManager(new LinearLayoutManager(AdminGoodsActivity.this));
        adminGoodsAdapter = new AdminGoodsAdapter(AdminGoodsActivity.this, goods);
        recyclerView.setAdapter(adminGoodsAdapter);
        adminGoodsAdapter.setOnViewClickListener(new AdminGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position, Goods goods) {

            }

            @Override
            public void onItemClickListener_gopass(int position, Goods goods) {
                positiona = position;
                Intent intent = new Intent(AdminGoodsActivity.this, AdminGoodsAuditActivity.class);
                intent.putExtra("objectId", goods.getObjectId());
                startActivityForResult(intent, 0);

            }

            @Override
            public void onItemClickListener_del(int position, Goods goods) {
                delData(goods,position);
            }
        });
        tv_text.setText("" + goods.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || resultCode == 0) {
            if (data.getStringExtra("aa").equals("ok")) {
                Goods goods = (Goods) data.getSerializableExtra("goods");
                adminGoodsAdapter.updData(positiona, goods);
                for (int i = 0; i < goodsList.size(); i++) {
                    if (goods.getObjectId().equals(goodsList.get(i).getObjectId())) {
                        goodsList.remove(i);
                        goodsList.add(i, goods);
                    }
                }
                if (goods.getGoods_state() == 1) {
                    ToastEx.success(AdminGoodsActivity.this, "审核通过！商品已上架！").show();
                }
                if (goods.getGoods_state() == 3) {
                    ToastEx.success(AdminGoodsActivity.this, "商品驳回成功！").show();

                }

            }
        }
    }
}
