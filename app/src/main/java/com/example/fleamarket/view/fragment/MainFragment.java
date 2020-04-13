package com.example.fleamarket.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fleamarket.R;
import com.example.fleamarket.adapter.MainAdapter;
import com.example.fleamarket.adapter.SaleAdapter;
import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.bean.BulletinText;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseFragment;
import com.example.fleamarket.view.activity.AddGoodsActivity;
import com.example.fleamarket.view.activity.GoodsInfoActivity;

import com.example.fleamarket.view.activity.SearchActivity;
import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.bakumon.library.view.BulletinView;


/**
 * Created by 王鹏飞
 * on 2020-02-13
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    //轮播文字
    private BulletinView mBulletinView;
    //搜索按钮
    private Button bt_search;
    //添加商品
    private ImageView img_add_more;
    //刷新
    private SmartRefreshLayout refreshLayout;
    //物品列表
    private RecyclerView recyclerView;
    //适配器
    private MainAdapter mainAdapter;
    //数据源
    private List<Goods> goods_list;
    private List<Advertising> adv_list;
    //文字轮播数据
    private List<BulletinText> bull_list;
    //网格布局
    private GridLayoutManager gridLayoutManager;
    //等待界面
    private ZLoadingDialog dialog;
    private Handler mHandler = new Handler() {
        //接收消息
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {//界面数据加载
                dialog.cancel();
                List<Goods> goods = new ArrayList<>();
                for (int i = 0;i<goods_list.size();i++){
                    if(goods_list.get(i).getGoods_state()==1){
                        goods.add(goods_list.get(i));
                    }
                }
                mainAdapter = new MainAdapter(getContext(), goods,adv_list);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(mainAdapter);
                mainAdapter.setOnViewClickListener(new MainAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, Goods goods) {
                        Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                        intent.putExtra("goodsobjectId", goods.getObjectId());
                        startActivity(intent);
                    }
                });
            }
            if (msg.what == 1002) {//轮播文字加载
                mBulletinView.setAdapter(new SaleAdapter(getActivity(), bull_list));
                mBulletinView.setOnBulletinItemClickListener(new BulletinView.OnBulletinItemClickListener() {
                    @Override
                    public void onBulletinItemClick(int position) {
                        Intent intent = new Intent(getActivity(), GoodsInfoActivity.class);
                        intent.putExtra("goodsobjectId",bull_list.get(position).getGoods_objectId());
                        startActivity(intent);

                    }
                });
            }
        }
    };

    @Override
    public int bindLaout() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        //控件绑定
        mBulletinView = view.findViewById(R.id.frag_main_bulletin_view);
        bt_search = view.findViewById(R.id.frag_main_search_bt);
        img_add_more = view.findViewById(R.id.frag_main_more);
        refreshLayout = view.findViewById(R.id.frag_main_refreshLayout);
        recyclerView = view.findViewById(R.id.frag_main_list);
        //监听器
        bt_search.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                selectGoodsAll();
                selectBulletinText();
                refreshLayout.finishRefresh();

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });
        refreshLayout.setRefreshHeader(new PhoenixHeader(getContext()));//设置Header
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));//设置Footer
    }

    @Override
    public void doBussiness() {
        //监听器
        img_add_more.setOnClickListener(this);
        //初始化
        goods_list = new ArrayList<>();
        adv_list = new ArrayList<>();
        //适配器布局
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i > 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });


        //商品数据查询
        selectGoodsAll();
        //文字广告轮播数据
        selectBulletinText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_main_search_bt://跳转搜索界面
                IntentUtil.get().goActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.frag_main_more://发布商品信息
                Intent intent = new Intent(getContext(), AddGoodsActivity.class);
                startActivityForResult(intent, 3);
                break;
        }
    }
    //商品数据查询
    private void selectGoodsAll() {
        //等待界面
        dialog = LoadingUtil.loading(getContext(),"Loding...");
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("goods_user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    goods_list = object;
                    selectAllAdv();
                } else {
                    Log.e("BMOB123", e.toString());
                }
            }
        });
    }
    //广告查询
    private void selectAllAdv(){
        BmobQuery<Advertising> query = new BmobQuery<Advertising>();
        query.findObjects(new FindListener<Advertising>() {
            @Override
            public void done(List<Advertising> object, BmobException e) {
                if (e == null) {
                    adv_list = object;
                    mHandler.sendEmptyMessage(1001);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    //文字广告轮播数据查询
    public void selectBulletinText() {
        BmobQuery<BulletinText> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<BulletinText>() {
            @Override
            public void done(List<BulletinText> object, BmobException e) {
                if (e == null) {
                   bull_list = object;
                   mHandler.sendEmptyMessage(1002);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3) {
            selectGoodsAll();
        }
    }
}
