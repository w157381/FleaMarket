package com.example.fleamarket.view.activity.admin.AdminGoods;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.ImgsUrlAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;

import com.example.fleamarket.view.activity.admin.AdminUser.AdminUserInfoActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminGoodsAuditActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private SimpleDraweeView head;
    private TextView stuNum, name, department, type, info;
    private RecyclerView recyclerView;
    private ImgsUrlAdapter imgsUrlAdapter;
    private Button bt_no, bt_ok;

    private ZLoadingDialog dialog;
    private Goods goods;


    private BmobIMConversation messageManager;
    private BmobIMConversation conversationEntrance;
    private String curr_id;
    private BmobIMUserInfo bmobIMUserInfo;



    private Intent data;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==1001){
                User user = goods.getGoods_user();


                BmobIMUserInfo infouser = new BmobIMUserInfo();
                infouser.setUserId(user.getUser_stuNumber());
                infouser.setAvatar(user.getUser_headImg());
                infouser.setName(user.getUser_name());
                conversationEntrance = BmobIM.getInstance().startPrivateConversation(infouser, null);

                conversationEntrance.setConversationIcon(user.getUser_headImg());
                conversationEntrance.setConversationId(user.getUser_stuNumber());
                conversationEntrance.setConversationTitle(user.getUser_nickName());
                BmobIM.getInstance().updateConversation(conversationEntrance);
                messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
                head.setImageURI(user.getUser_headImg());
                stuNum.setText(user.getUser_stuNumber());
                name.setText(user.getUser_name());
                department.setText(user.getUser_department());
                type.setText(goods.getGoods_type()+"");
                info.setText(goods.getGoods_info());
                LinearLayoutManager layoutManager = new LinearLayoutManager(AdminGoodsAuditActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                imgsUrlAdapter = new ImgsUrlAdapter(AdminGoodsAuditActivity.this,goods.getGoods_imgs());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(imgsUrlAdapter);


            }
        }
    };
    @Override
    protected int initLayout() {
        return R.layout.admin_goods_audit;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_goods_audit_back);
        head = findViewById(R.id.admin_goods_audit_userhead);
        stuNum = findViewById(R.id.admin_goods_audit_usernum);
        name = findViewById(R.id.admin_goods_audit_username);
        department = findViewById(R.id.admin_goods_audit_department);
        type = findViewById(R.id.admin_goods_audit_goodstype);
        info = findViewById(R.id.admin_goods_audit_goodsinfo);
        recyclerView = findViewById(R.id.admin_goods_audit_goods_imgs);
        bt_no = findViewById(R.id.admin_goods_audit_bt_nopass);
        bt_ok = findViewById(R.id.admin_goods_audit_bt_pass);

    }

    @Override
    protected void initData() {
        curr_id = BmobIM.getInstance().getCurrentUid();
        bmobIMUserInfo = new BmobIMUserInfo();
        bmobIMUserInfo = BmobIM.getInstance().getUserInfo(curr_id);
        data = new Intent();
        goods = new Goods();
        img_back.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        bt_no.setOnClickListener(this);




        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");



        selectGoodsInfo(objectId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_goods_audit_back:
                data.putExtra("aa","no");
                setResult(0,data);
                finish();
                break;
            case R.id.admin_goods_audit_bt_nopass:
                goods.setGoods_state(3);
                updateGoodsInfo(goods,3);

                break;
            case R.id.admin_goods_audit_bt_pass:
                goods.setGoods_state(1);
                updateGoodsInfo(goods,1);

                break;
        }
    }

    private void selectGoodsInfo(String objectId) {

        dialog = LoadingUtil.loading(AdminGoodsAuditActivity.this, "Loading...");
        BmobQuery<Goods> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("objectId",objectId);
        categoryBmobQuery.include("goods_user");
        categoryBmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    goods = object.get(0);
                    handler.sendEmptyMessage(1001);
                    dialog.cancel();
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void updateGoodsInfo(final Goods goods,final int t){
        goods.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    if(t==1){
                        sendTextMessage(goods.getGoods_name()+ Constants.GOODS_AUDIT_PASS,1);
                    }
                    if(t==3){
                        sendTextMessage(goods.getGoods_name()+ Constants.GOODS_AUDIT_PASS,3);
                    }

                }else{

                }
            }
        });
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage(final String text, final int tt) {

        //TODO 发送消息：6.1、发送文本消息
        final BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可随意设置额外信息
        final Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        msg.setBmobIMUserInfo(bmobIMUserInfo);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null) {
                        data.putExtra("aa", "ok");
                        data.putExtra("goods",goods);
                        setResult(0, data);
                        finish();

                } else {
                    ToastEx.error(AdminGoodsAuditActivity.this, "操作失败,请重新发送！").show();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            data.putExtra("aa", "no");
            setResult(0, data);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
