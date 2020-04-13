package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.FabuAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyfabuActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private RecyclerView list;
    private List<Goods> data;

    private FabuAdapter fabuAdapter;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1001){
                for (int i=0;i<data.size();i++){
                    if(data.get(i).getGoods_state()==-2){
                        data.remove(i);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyfabuActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                list.setLayoutManager(layoutManager);
                fabuAdapter = new FabuAdapter(MyfabuActivity.this,data);
                list.setAdapter(fabuAdapter);
                fabuAdapter.setOnViewClickListener(new FabuAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, Goods goods) {
                        ToastEx.success(MyfabuActivity.this,""+position).show();

                    }

                    @Override
                    public void onItemClickListener_ref(int position, FabuAdapter.ViewHolder holder, Goods goods) {
                        goods.setGoods_state(1);
                        updateGoods(goods);
                        holder.bt_xiajia.setVisibility(View.VISIBLE);
                        holder.bt_delete.setVisibility(View.VISIBLE);
                        holder.bt_update.setVisibility(View.VISIBLE);
                        holder.bt_refabu.setVisibility(View.GONE);
                        fabuAdapter.update();
                    }

                    @Override
                    public void onItemClickListener_xia(int position, FabuAdapter.ViewHolder holder,Goods goods) {
                        ToastEx.success(MyfabuActivity.this,""+position).show();
                        goods.setGoods_state(-1);
                        updateGoods(goods);
                        fabuAdapter.update();
                    }

                    @Override
                    public void onItemClickListener_upd(int position,FabuAdapter.ViewHolder holder, Goods goods) {
                        Intent intent = new Intent (MyfabuActivity.this, MyFabuUpActivity.class);
                        intent.putExtra("goods",goods);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemClickListener_del(int position,FabuAdapter.ViewHolder holder, Goods goods) {
                        ToastEx.success(MyfabuActivity.this,""+position).show();
                        goods.setGoods_state(-2);
                        updateGoods(goods);
                        fabuAdapter.update();
                    }
                });
            }
        }
    };
    @Override
    protected int initLayout() {
        return R.layout.activity_fabu;
    }

    @Override
    protected void initView() {
        list = findViewById(R.id.myfabu_list);
        back = findViewById(R.id.myfa_back);

    }

    @Override
    protected void initData() {
        //监听器注册
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myfa_back:
                finish();
                break;
        }

    }
    public void selectGoods(String account){
        BmobQuery<Goods> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId",account);
        categoryBmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    if(object.size()>0){
                        data = object;
                        mHandle.sendEmptyMessage(1001);
                    }else{

                    }
                } else {

                }
            }
        });
    }

    public void updateGoods(Goods goods){
        goods.update(goods.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastEx.success(MyfabuActivity.this,"操作成功！").show();
                }else{
                    ToastEx.error(MyfabuActivity.this,"操作失败！").show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectGoods(BmobIM.getInstance().getCurrentUid());
    }
}
