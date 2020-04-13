package com.example.fleamarket.view.activity.admin.AdminUser;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.GetTimeUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.ChatActivity;
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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class AdminUserInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private TextView tv_stuNum, tv_name, tv_depart;
    private Button bt_fh, bt_dj, bt_unfh, bt_undj, bt_send;
    private RelativeLayout rela;

    private User user;

    //等待界面
    private ZLoadingDialog dialog;

    private BmobIMConversation messageManager;
    private BmobIMConversation conversationEntrance;


    private CountDownTimer timer;
    private boolean istimer = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                dialog.cancel();
                rela.setVisibility(View.VISIBLE);
                tv_stuNum.setText(user.getUser_stuNumber());
                tv_name.setText(user.getUser_name());
                tv_depart.setText(user.getUser_department());
                if (user.getUser_loginState() == -1) {
                    bt_send.setVisibility(View.GONE);
                    bt_dj.setVisibility(View.GONE);
                    bt_fh.setVisibility(View.GONE);
                    bt_undj.setVisibility(View.GONE);
                }
                if (user.getUser_loginState() == 0) {
                    bt_send.setVisibility(View.GONE);
                    bt_dj.setVisibility(View.GONE);
                    bt_fh.setVisibility(View.GONE);
                    bt_unfh.setVisibility(View.GONE);
                    sendEmptyMessage(1002);

                }
                if (user.getUser_loginState() == 1) {
                    bt_undj.setVisibility(View.GONE);
                    bt_unfh.setVisibility(View.GONE);
                }

                BmobIMUserInfo info = new BmobIMUserInfo();
                info.setUserId(user.getUser_stuNumber());
                info.setAvatar(user.getUser_headImg());
                info.setName(user.getUser_name());
                conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, null);
                conversationEntrance.setConversationIcon(user.getUser_headImg());
                conversationEntrance.setConversationId(user.getUser_stuNumber());
                conversationEntrance.setConversationTitle(user.getUser_nickName());
                BmobIM.getInstance().updateConversation(conversationEntrance);
                messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);

            }
            if (msg.what == 1002) {
                istimer = true;
                timer = new CountDownTimer(GetTimeUtil.getTimelongExpend(GetTimeUtil.gettime(), user.getUser_freezeDate()), 1000) {
                    /**
                     * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
                     * @param millisUntilFinished
                     */
                    @Override
                    public void onTick(long millisUntilFinished) {
                        bt_undj.setText("解除冻结  " + formatTime(millisUntilFinished));
                    }

                    /**
                     * 倒计时完成时被调用
                     */
                    @Override
                    public void onFinish() {
                        bt_undj.setText("解除冻结");
                    }
                };
                timerStart();
            }
            if (msg.what == 1006) {
                bt_dj.setVisibility(View.GONE);
                bt_fh.setVisibility(View.GONE);
                bt_undj.setVisibility(View.GONE);
                bt_unfh.setVisibility(View.VISIBLE);
                bt_send.setVisibility(View.GONE);
                ToastEx.success(AdminUserInfoActivity.this, "封号操作成功！1006").show();
            }
            if (msg.what == 1007) {
                bt_dj.setVisibility(View.GONE);
                bt_fh.setVisibility(View.GONE);
                bt_undj.setVisibility(View.VISIBLE);
                bt_unfh.setVisibility(View.GONE);
                bt_send.setVisibility(View.GONE);
                ToastEx.success(AdminUserInfoActivity.this, "冻结成功！1007").show();
            }
            if (msg.what == 1008) {
                bt_dj.setVisibility(View.VISIBLE);
                bt_fh.setVisibility(View.VISIBLE);
                bt_undj.setVisibility(View.GONE);
                bt_unfh.setVisibility(View.GONE);
                bt_send.setVisibility(View.VISIBLE);
                ToastEx.success(AdminUserInfoActivity.this, "操作成功1008！").show();
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.admin_userinfo;
    }

    @Override
    protected void initView() {
        rela = findViewById(R.id.admin_userinfo_rela);
        img_back = findViewById(R.id.admin_userinfo_back);
        tv_stuNum = findViewById(R.id.admin_userinfo_stunum);
        tv_name = findViewById(R.id.admin_userinfo_name);
        tv_depart = findViewById(R.id.admin_userinfo_depart);
        bt_fh = findViewById(R.id.admin_userinfo_bt_fenghao);
        bt_dj = findViewById(R.id.admin_userinfo_bt_dongjie);
        bt_unfh = findViewById(R.id.admin_userinfo_bt_unfenghao);
        bt_undj = findViewById(R.id.admin_userinfo_bt_undongjie);
        bt_send = findViewById(R.id.admin_userinfo_bt_send);


    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        bt_fh.setOnClickListener(this);
        bt_dj.setOnClickListener(this);
        bt_unfh.setOnClickListener(this);
        bt_undj.setOnClickListener(this);
        bt_send.setOnClickListener(this);

        user = new User();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        selectUserInfo();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.admin_userinfo_back://返回
                finish();
                break;
            case R.id.admin_userinfo_bt_fenghao://封号操作 //登录状态(-1：永久冻结；0:冻结，1：正常登录)
                user.setUser_loginState(-1);
                updateUserInfo(user, -1);
                break;
            case R.id.admin_userinfo_bt_dongjie://冻结三天
                user.setUser_loginState(0);
                user.setUser_freezeDate(GetTimeUtil.gettime());
                handler.sendEmptyMessage(1002);
                updateUserInfo(user, 0);
                break;
            case R.id.admin_userinfo_bt_unfenghao://解除封号
                user.setUser_loginState(1);
                updateUserInfo(user, 2);

                break;
            case R.id.admin_userinfo_bt_undongjie://解除冻结
                user.setUser_loginState(1);
                user.setUser_freezeDate("");
                updateUserInfo(user, 3);

                break;
            case R.id.admin_userinfo_bt_send://发送信息
                Intent intent = new Intent(AdminUserInfoActivity.this, ChatActivity.class);
                intent.putExtra("conversationId", conversationEntrance.getConversationId());
                startActivity(intent);
                break;
        }
    }

    private void updateUserInfo(User user, final int t) {
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (t == -1) {
                        sendTextMessage(Constants.ADMIN_FENGHAO_TEXT, -1);
                    }
                    if (t == 0) {
                        sendTextMessage(Constants.ADMIN_DONGJIE_TEXT, 0);
                    }
                    if (t == 2) {
                        sendTextMessage(Constants.ADMIN_UNFENGHAO_TEXT, 5);
                    }
                    if (t == 3) {
                        sendTextMessage(Constants.ADMIN_UNDONGJIE_TEXT, 5);
                        timerCancel();
                    }

                } else {
                    ToastEx.error(AdminUserInfoActivity.this, "操作失败！" + e.getMessage()).show();
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
        msg.setBmobIMUserInfo(BmobIM.getInstance().getUserInfo(Constants.ADMIN_ID));
        //可随意设置额外信息
        final Map<String, Object> map = new HashMap<>();
        map.put("level", "1");
        msg.setExtraMap(map);
        messageManager.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null) {
                    Log.e("1212", "" + msg);
                    if (tt == -1) {
                        handler.sendEmptyMessage(1006);
                    }
                    if (tt == 0) {
                        handler.sendEmptyMessage(1007);
                    }
                    if(tt==5) {
                        handler.sendEmptyMessage(1008);
                    }
                } else {
                    ToastEx.error(AdminUserInfoActivity.this, "操作失败,请重新发送！").show();
                }
            }
        });

    }

    private void selectUserInfo() {
        dialog = LoadingUtil.loading(AdminUserInfoActivity.this, "Loading...");
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("objectId", user.getObjectId());
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    user = list.get(0);
                    handler.sendEmptyMessage(1001);
                } else {

                }

            }
        });
    }


    /**
     * 将毫秒转化为 分钟：秒 的格式
     *
     * @param millisecond 毫秒
     * @return
     */
    public String formatTime(long millisecond) {
        //以天数为单位取整
        Long day = millisecond / (1000 * 60 * 60 * 24);
        //以小时为单位取整
        Long hour = (millisecond / (60 * 60 * 1000) - day * 24);
        //以分钟为单位取整
        Long min = ((millisecond / (60 * 1000)) - day * 24 * 60 - hour * 60);
        //以秒为单位
        Long second = (millisecond / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        String time = day + "天 " + hour + "时" + min + "分" + second + "秒";

        return time;
    }

    /**
     * 取消倒计时
     */
    public void timerCancel() {
        timer.cancel();
        bt_undj.setText("解除冻结");

    }

    /**
     * 开始倒计时
     */
    public void timerStart() {
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (istimer) {
            timerCancel();
        }
    }
}

