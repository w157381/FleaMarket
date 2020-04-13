package com.example.fleamarket.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.AlertAdapter;
import com.example.fleamarket.bean.MessAlert;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.ConnectUtil;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.fragment.MainFragment;
import com.example.fleamarket.view.fragment.MessageFragment;
import com.example.fleamarket.view.fragment.MineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MessageListHandler, View.OnClickListener {
    private static final float BEEP_VOLUME = 1;
    private BottomNavigationView navigation;
    //默认选择第一个fragment
    private int lastSelectedPosition = 0;
    //首页碎片
    private MainFragment mainFragment;
    //消息碎片
    private MessageFragment messageFragment;
    //我的碎片
    private MineFragment mineFragment;
    //碎片集合
    private Fragment[] fragments;
    //轻量级的存储类 账号密码储存
    private SharedPreferences sharedPreferences;
    //浮窗消息列表
    private List<MessAlert> alerts;
    private RelativeLayout rela_alert;
    private RecyclerView messlist_head;
    private ImageView messlist_head_del;
    //当前用户id
    private String id;
    private User user;
    //消息提示音，震动
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    public Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            if (msg.what == 1002) {
                messlist_head.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                final AlertAdapter alertAdapter = new AlertAdapter(MainActivity.this, alerts);
                messlist_head.setAdapter(alertAdapter);
                alertAdapter.setOnViewClickListener(new AlertAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, String conversationId) {
                        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                        intent.putExtra("conversationId", conversationId);
                        startActivity(intent);
                        alertAdapter.removeData(position);
                        if (alerts.size() == 0) {
                            messlist_head_del.setVisibility(View.GONE);
                        }
                    }
                });
            }
            if (msg.what == 1003) {
                rela_alert.setVisibility(View.VISIBLE);
            }
            if(msg.what==1009){//退出
                Log.e("222","退出");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("auto", "false");
                editor.putString("password", "");
                editor.commit();
                BmobIM.getInstance().disConnect();
                mHandle.sendEmptyMessageDelayed(1010,1000);

            }
            if(msg.what==1010){
                IntentUtil.get().goActivity(MainActivity.this, LoginActivity.class);
                finish();
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.main_navigation);
        rela_alert = (RelativeLayout) findViewById(R.id.main_alert_rela);
        messlist_head = (RecyclerView) findViewById(R.id.main_user_mess);
        messlist_head_del = (ImageView) findViewById(R.id.main_user_mess_dis);
    }

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences(Constants.MY_PRE_NAME, Context.MODE_PRIVATE);
        //监听切换事件
        messlist_head_del.setOnClickListener(this);
        messlist_head_del.setVisibility(View.GONE);
        alerts = new ArrayList<>();
        user = new User();
        //监听切换事件
        navigation.setOnNavigationItemSelectedListener(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //平均布局
        mainFragment = new MainFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();
        fragments = new Fragment[]{mainFragment, messageFragment, mineFragment};
        lastSelectedPosition = 0;
        //默认提交第一个
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.tb, mainFragment)//添加
                .show(mainFragment)//展示
                .commit();//提交

        Intent intent = getIntent();
        user =(User)intent.getSerializableExtra("user");
//        ConnectUtil.connect(MainActivity.this, user);
        //消息震动，提示音
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = new MediaPlayer();//这个我定义了一个成员函数
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });
        AssetFileDescriptor file = this.getResources().openRawResourceFd(R.raw.message_sing1);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();

        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            mediaPlayer = null;

        }

    }

    public void setAddNumber(int num) {
        //获取整个的NavigationView
        Log.e("321","321");
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        //获取所添加的每一个Tab，并给第三个Tab添加消息角标
        View tabView = menuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tabView;
        //加载我们的角标布局View，新创建的一个布局
        View badgeView = LayoutInflater.from(this).inflate(R.layout.number_badge, menuView, false);
        TextView number = badgeView.findViewById(R.id.msg_number);
        number.setText(num + "");
        //添加到Tab上
        itemView.addView(badgeView);
    }

    /**
     * 切换Fragment
     *
     * @param lastIndex 上个显示Fragment的索引
     * @param index     需要显示的Fragment的索引
     */
    private void setDefaultFragment(int lastIndex, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastIndex]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.tb, fragments[index]);
        }
        transaction.show(fragments[index]).commit();
    }

    /**
     * 切换事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_homefirst:
                if (0 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 0);
                    lastSelectedPosition = 0;
                    rela_alert.setVisibility(View.VISIBLE);
                }
                return true;
            case R.id.navigation_homesecond:
                if (1 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 1);
                    lastSelectedPosition = 1;
                    alerts.clear();
                    mHandle.sendEmptyMessage(1002);
                    rela_alert.setVisibility(View.GONE);
                }

                return true;
            case R.id.navigation_homethird:
                if (2 != lastSelectedPosition) {
                    setDefaultFragment(lastSelectedPosition, 2);
                    lastSelectedPosition = 2;
                    messlist_head.setVisibility(View.VISIBLE);
                }
                return true;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        setAddNumber((int) BmobIM.getInstance().getAllUnReadCount());
    }

    @Override
    public void onResume() {
        super.onResume();
        BmobIM.getInstance().addMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        MessageEvent messageEvent = list.get(0);

        String aaa = sharedPreferences.getString("alert", "");
        if (aaa.equals("true")) {
            rela_alert.setVisibility(View.VISIBLE);
            int aa = (int) BmobIM.getInstance().getUnReadCount(messageEvent.getConversation().getConversationId());
            if (aa > 0) {
                //有未读消息
                MessAlert messAlert = new MessAlert();
                if (alerts.size() == 0) {
                    messAlert.setMessageEvent(messageEvent);
                    messAlert.setNum(aa + "");
                    alerts.add(messAlert);
                } else {
                    for (int i = 0; i < alerts.size(); i++) {
                        if (messageEvent.getConversation().getConversationId().equals(alerts.get(i).getMessageEvent().getConversation().getConversationId())) {
                            alerts.get(i).setNum(aa + "");
                        } else {
                            messAlert.setMessageEvent(messageEvent);
                            messAlert.setNum(aa + "");
                            alerts.add(messAlert);
                        }
                    }
                }
                if (alerts.size() > 0) {
                    messlist_head_del.setVisibility(View.VISIBLE);
                    mHandle.sendEmptyMessage(1002);
                }

            } else {
                //没有未读消息

            }

        } else {
            rela_alert.setVisibility(View.GONE);
        }


        setAddNumber((int) BmobIM.getInstance().getAllUnReadCount());
        //消息震动和铃声
        if (sharedPreferences != null) {
            String str_vibrator = sharedPreferences.getString("vibrator", "");
            String str_mediaplayer = sharedPreferences.getString("mediaplayer", "");
            if (str_vibrator.equals("true")) {
                long[] pattern = new long[]{0, 80};
                vibrator.vibrate(pattern, -1);  //震动
            }
            if (str_mediaplayer.equals("true")) {
                mediaPlayer.start();
            }

        }
        Log.e("222",""+messageEvent.getMessage().getContent());
        if(messageEvent.getConversation().getConversationId().equals(Constants.ADMIN_ID)){
            if(messageEvent.getMessage().getContent().equals(Constants.ADMIN_DONGJIE_TEXT)){
                mHandle.sendEmptyMessage(1009);
            }
            if(messageEvent.getMessage().getContent().equals(Constants.ADMIN_FENGHAO_TEXT)){
                mHandle.sendEmptyMessage(1009);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BmobIM.getInstance().removeMessageListHandler(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_user_mess_dis:
                rela_alert.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("alert", "false");
                editor.commit();
                break;
        }
    }

}
