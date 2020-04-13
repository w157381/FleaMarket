package com.example.fleamarket.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.MessageAdapter;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.view.BaseFragment;
import com.example.fleamarket.view.activity.ChatActivity;
import com.example.fleamarket.view.activity.MainActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 王鹏飞
 * on 2020-02-13
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener, MessageListHandler {
    private ImageView img_clear_unRead;
    private ImageView img_clear_allMess;
    private RecyclerView recyclerView;
    private TextView tv_bt_open_alert;
    private ImageView img_del_alert;

    private SmartRefreshLayout refreshLayout;
    private RelativeLayout rela_alert;
    private MessageAdapter messageAdapter;
    private List<BmobIMConversation> allCon;
    private BmobIMConversation messageManager;
    private SharedPreferences sharedPreferences;
    //当前用户id
    private String userid;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1001) {

                allCon = BmobIM.getInstance().loadAllConversation();
                messageAdapter = new MessageAdapter(getContext(), allCon);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(messageAdapter);
                messageAdapter.setOnViewClickListener(new MessageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(BmobIMConversation bmobIMConversation) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("conversationId",bmobIMConversation.getConversationId());
                        getContext().startActivity(intent);
                    }
                });
            }
        }
    };


    @Override
    public int bindLaout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        //控件获取
        img_clear_unRead = view.findViewById(R.id.mess_clear_unread);
        img_clear_allMess = view.findViewById(R.id.mess_clear_allmess);
        rela_alert  = view.findViewById(R.id.mess_rela_alert);
        tv_bt_open_alert = view.findViewById(R.id.mess_open_alert);
        img_del_alert = view.findViewById(R.id.mess_del_alert);
        refreshLayout = view.findViewById(R.id.mess_refreshLayout);
        recyclerView = view.findViewById(R.id.mess_re_list);

    }

    @Override
    public void doBussiness() {
        //监听器注册
        img_clear_unRead.setOnClickListener(this);
        img_clear_allMess.setOnClickListener(this);
        tv_bt_open_alert.setOnClickListener(this);
        img_del_alert.setOnClickListener(this);
        //初始化
        allCon = new ArrayList<>();
        allCon = BmobIM.getInstance().loadAllConversation();
        boolean isexit = false;
        if(!allCon.equals(null)){
            for (int i = 0; i < allCon.size(); i++) {
                if (allCon.get(i).getConversationIcon().equals("")) {
                    connMess(allCon.get(i).getConversationId());
                    isexit = true;
                }
            }
            if(isexit){
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(1000);//休眠1秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandle.sendEmptyMessage(1001);
                    }
                }.start();

            }else{
                mHandle.sendEmptyMessage(1001);
            }
        }

    }

    //当会话Icon为空时，调用startPrivateConversation
    public void connMess(String conversationId) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", conversationId);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    BmobIMUserInfo userInfo = new BmobIMUserInfo();
                    User user1 = object.get(0);
                    userInfo.setId(Long.parseLong(user1.getUser_stuNumber()));
                    userInfo.setAvatar(user1.getUser_headImg());
                    userInfo.setName(user1.getUser_nickName());
                    userInfo.setUserId(user1.getUser_stuNumber());
                    BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(userInfo, null);
                    conversationEntrance.setConversationIcon(user1.getUser_headImg());
                    conversationEntrance.setConversationTitle(user1.getUser_nickName());
                    conversationEntrance.setConversationId(user1.getUser_stuNumber());
                    BmobIM.getInstance().updateConversation(conversationEntrance);
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mess_clear_unread:
                List<BmobIMConversation> allmess = BmobIM.getInstance().loadAllConversation();
                if (allmess != null) {
                    for (int i = 0; i < allmess.size(); i++) {
                        messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), allmess.get(i));
                        messageManager.updateLocalCache();
                    }
                }
                break;
            case R.id.mess_clear_allmess:
                //TODO 会话：4.6、清空全部会话，一般不会用到此方法
                BmobIM.getInstance().clearAllConversation();
                mHandle.sendEmptyMessage(1001);
                break;
            case R.id.mess_open_alert:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("alert", "true");
                editor.commit();
                rela_alert.setVisibility(View.GONE);
                break;
            case R.id.mess_del_alert:
                rela_alert.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = getActivity().getSharedPreferences("fleamarket", MODE_PRIVATE);
        if (sharedPreferences != null) {
            String aa = sharedPreferences.getString("alert", "");
            if(aa.equals("true")){
                rela_alert.setVisibility(View.GONE);
            }else{
                rela_alert.setVisibility(View.VISIBLE);
            }
        }
        mHandle.sendEmptyMessage(1001);
    }

    @Override
    public void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        MessageEvent messageEvent = list.get(0);
        boolean isExit = false;
        for (int i = 0; i < allCon.size(); i++) {
            if (messageEvent.getConversation().getConversationId().equals(allCon.get(i).getConversationId())) {
                isExit = true;
                break;
            } else {
                isExit = false;
            }
        }
        if (isExit) {
            //已有会话操作流程
            mHandle.sendEmptyMessage(1001);

        } else {
            //新会话操作流程
            BmobIMUserInfo userInfo = messageEvent.getFromUserInfo();
            BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(userInfo, null);
            conversationEntrance.setConversationIcon(userInfo.getAvatar());
            conversationEntrance.setConversationTitle(userInfo.getName());
            conversationEntrance.setConversationId(userInfo.getUserId());
            BmobIM.getInstance().updateConversation(conversationEntrance);
            messageAdapter.addData(allCon.size(), conversationEntrance);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BmobIM.getInstance().removeMessageListHandler(this);
    }
}
