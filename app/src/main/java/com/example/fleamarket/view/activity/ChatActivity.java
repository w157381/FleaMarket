package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.ChatAdapter;

import com.example.fleamarket.utils.GlideLoader;
import com.example.fleamarket.view.BaseActivity;
import com.lcw.library.imagepicker.ImagePicker;
import com.zia.toastex.ToastEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.darsh.multipleimageselect.helpers.Constants.REQUEST_CODE;

public class ChatActivity extends BaseActivity implements View.OnClickListener, MessageListHandler {
    private ImageView back;
    private TextView nickname;
    private ImageView menu;
    private RecyclerView messlist;
    private EditText et_text;
    private Button bt_send;
    private ImageView img_send;
    //图片集合
    private ArrayList<String> imgs;
    private ChatAdapter chatAdapter;
    private String conversationId;
    private BmobIMConversation messageManager;
    private BmobIMConversation bmobIMConversation;
    private String curr_id;
    private BmobIMUserInfo bmobIMUserInfo;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            if(msg.what==1001){
                final String[] filePaths = new String[imgs.size()];
                for (int i = 0;i<imgs.size();i++){
                   filePaths[i] = imgs.get(i);
                }
                BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

                    @Override
                    public void onSuccess(List<BmobFile> files,List<String> urls) {
                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                        //2、urls-上传文件的完整url地址
                        if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                           for (int i =0;i<files.size();i++){
                               sendRemoteImageMessage(files.get(i).getUrl());
                           }
                        }
                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                       Log.e("错误:","错误码"+statuscode +",错误描述："+errormsg);
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                        //1、curIndex--表示当前第几个文件正在上传
                        //2、curPercent--表示当前上传文件的进度值（百分比）
                        //3、total--表示总的上传文件数
                        //4、totalPercent--表示总的上传进度（百分比）
                    }
                });

            }
        }
    };
    @Override
    protected int initLayout() {
        return R.layout.activity_chat;

    }

    @Override
    protected void initView() {
        back = findViewById(R.id.chat_back);
        nickname = findViewById(R.id.chat_nickname);
        menu = findViewById(R.id.chat_more);
        messlist = findViewById(R.id.chat_messagelist);
        et_text = findViewById(R.id.chat_text);
        bt_send = findViewById(R.id.chat_send);
        img_send = findViewById(R.id.chat_img_send);

    }

    @Override
    protected void initData() {
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        img_send.setOnClickListener(this);

        imgs = new ArrayList<>();
        curr_id = BmobIM.getInstance().getCurrentUid();
        bmobIMUserInfo = new BmobIMUserInfo();
        bmobIMUserInfo = BmobIM.getInstance().getUserInfo(curr_id);
        bmobIMConversation = new BmobIMConversation();
        Intent intent = getIntent();
        conversationId = intent.getStringExtra("conversationId");

        List<BmobIMConversation> allCon = BmobIM.getInstance().loadAllConversation();
        bmobIMConversation = new BmobIMConversation();
        for (int i = 0; i < allCon.size(); i++) {
            if (allCon.get(i).getConversationId().equals(conversationId)) {
                bmobIMConversation = allCon.get(i);
                break;
            }
        }

        messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), bmobIMConversation);
        messageManager.updateLocalCache();
        nickname.setText("" + bmobIMConversation.getConversationTitle());
        chatAdapter = new ChatAdapter(ChatActivity.this, bmobIMConversation);
        messlist.setLayoutManager(new LinearLayoutManager(this));
        messlist.setAdapter(chatAdapter);
        messlist.scrollToPosition(chatAdapter.getItemCount() - 1);//此句为设置显示
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_more:
                ToastEx.warning(ChatActivity.this, "功能待开发 ！").show();
                break;
            case R.id.chat_send:
                sendTextMessage();

                break;
            case R.id.chat_img_send:
                ImagePicker.getInstance()
                        .setTitle("选择照片")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setImagePaths(imgs)//保存上一次选择图片的状态，如果不需要可以忽略
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(ChatActivity.this, REQUEST_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                break;

        }
    }

    /**
     * 发送文本消息
     */
    private void sendTextMessage() {
        final String text = et_text.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            ToastEx.warning(ChatActivity.this, "请输入发送内容！").show();
            return;
        }
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
                    chatAdapter.addData(bmobIMMessage);
                    messlist.scrollToPosition(chatAdapter.getItemCount() - 1);//此句为设置显示
                    et_text.setText("");
                } else {
                    ToastEx.error(ChatActivity.this,""+e.getMessage()).show();
                }
            }
        });

    }
    /**
     * 直接发送远程图片地址
     */
    public void sendRemoteImageMessage(String imgUrl) {
        //TODO 发送消息：6.3、发送远程图片消息
        BmobIMImageMessage image = new BmobIMImageMessage();
        image.setBmobIMUserInfo(BmobIM.getInstance().getUserInfo(BmobIM.getInstance().getCurrentUid()));
        image.setRemoteUrl(imgUrl);
        messageManager.sendMessage(image , new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if(e==null){
                    Log.e("1212","getMsgType"+bmobIMMessage.getMsgType());
                    Log.e("1212","getMsgType"+bmobIMMessage.getContent());
                    chatAdapter.addData(bmobIMMessage);
                    messlist.scrollToPosition(chatAdapter.getItemCount() - 1);//此句为设置显示
                }else{
                    ToastEx.error(ChatActivity.this,""+e.getMessage()).show();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        MessageEvent messageEvent = list.get(0);
        if (messageEvent.getConversation().getConversationId().equals(conversationId)) {
            Log.e("123", "" + list.get(0).getConversation().getMessages());
            chatAdapter.addData(list.get(0).getMessage());
            messlist.scrollToPosition(chatAdapter.getItemCount() - 1);//此句为设置显示
        }
        //接收处理在线、离线消息
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            imgs = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (imgs.size()>0){
                mHandle.sendEmptyMessage(1001);
            }
        }
    }

}
