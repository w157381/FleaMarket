package com.example.fleamarket.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.view.BaseFragment;
import com.example.fleamarket.view.activity.AddMyAddressActivity;
import com.example.fleamarket.view.activity.BrowsGoodsHistoryActivity;
import com.example.fleamarket.view.activity.ChangePassActivity;
import com.example.fleamarket.view.activity.CheckStuActivity;
import com.example.fleamarket.view.activity.LoginActivity;
import com.example.fleamarket.view.activity.MyAddrActivity;
import com.example.fleamarket.view.activity.MyBuyActivity;
import com.example.fleamarket.view.activity.MyCollectActivity;
import com.example.fleamarket.view.activity.MyInfoActivity;
import com.example.fleamarket.view.activity.MySellActivity;
import com.example.fleamarket.view.activity.MyfabuActivity;
import com.example.fleamarket.view.activity.SettingActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zia.toastex.ToastEx;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MineFragment extends BaseFragment implements View.OnClickListener {

    private SimpleDraweeView headimg;
    private TextView tv_nickname,tv_department;
    private ImageView img_qiandao,img_myFabu,img_mySell,img_myBuy,img_myCollect;
    private RelativeLayout rela_brows,rela_address,rela_change_pass,rela_about,rela_setting;
    private Button bt_quit;

    private User user;
    private String id;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1001){
                headimg.setImageURI(Uri.parse(user.getUser_headImg()));
                tv_nickname.setText(""+user.getUser_nickName());
                tv_department.setText(""+user.getUser_department());
            }
            if(msg.what==1009){//退出
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fleamarket", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("auto", "false");
                editor.putString("password", "");
                editor.commit();
                BmobIM.getInstance().disConnect();
                IntentUtil.get().goActivity(getContext(), LoginActivity.class);
                getActivity().finish();
            }
        }
    };
    @Override
    public int bindLaout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        //控件获取
        headimg = (SimpleDraweeView) view.findViewById(R.id.frag_mine_userheadimg);
        tv_nickname = view.findViewById(R.id.frag_mine_nickname);
        tv_department = view.findViewById(R.id.frag_mine_department);
        img_qiandao = view.findViewById(R.id.frag_mine_qiandao);
        img_myFabu = view.findViewById(R.id.frag_mine_my_fabu);
        img_mySell = view.findViewById(R.id.frag_mine_my_sell);
        img_myBuy = view.findViewById(R.id.frag_mine_my_buy);
        img_myCollect = view.findViewById(R.id.frag_mine_my_collect);
        rela_brows  = view.findViewById(R.id.frag_mine_browsInfo);
        rela_address  = view.findViewById(R.id.frag_mine_address);
        rela_change_pass = view.findViewById(R.id.frag_mine_change_pass);
        rela_about  = view.findViewById(R.id.frag_mine_about_us);
        rela_setting  = view.findViewById(R.id.frag_mine_setting);
        bt_quit = (Button) view.findViewById(R.id.frag_mine_bt_quit);

    }

    @Override
    public void doBussiness() {

        //监听器注册
        headimg.setOnClickListener(this);
        img_qiandao.setOnClickListener(this);
        img_myFabu.setOnClickListener(this);
        img_mySell.setOnClickListener(this);
        img_myBuy.setOnClickListener(this);
        img_myCollect.setOnClickListener(this);
        rela_brows.setOnClickListener(this);
        rela_change_pass.setOnClickListener(this);
        rela_address.setOnClickListener(this);
        rela_about.setOnClickListener(this);
        rela_setting.setOnClickListener(this);
        bt_quit.setOnClickListener(this);

        //初始化
        user = new User();
        id = BmobIM.getInstance().getCurrentUid();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.frag_mine_userheadimg:
                IntentUtil.get().goActivity(getContext(), MyInfoActivity.class);
                break;
            case R.id.frag_mine_qiandao:
                ToastEx.success(getContext(),"frag_mine_qiandao").show();
                break;
            case R.id.frag_mine_my_fabu:
               IntentUtil.get().goActivity(getActivity(), MyfabuActivity.class);
                break;
            case R.id.frag_mine_my_sell:
                IntentUtil.get().goActivity(getActivity(), MySellActivity.class);
                break;
            case R.id.frag_mine_my_buy:
                IntentUtil.get().goActivity(getActivity(), MyBuyActivity.class);
                break;
            case R.id.frag_mine_my_collect:
                IntentUtil.get().goActivity(getActivity(), MyCollectActivity.class);
                break;
            case R.id.frag_mine_browsInfo:
                IntentUtil.get().goActivity(getActivity(), BrowsGoodsHistoryActivity.class);
                break;
            case R.id.frag_mine_address:
                IntentUtil.get().goActivity(getContext(), MyAddrActivity.class);
                break;
            case R.id.frag_mine_change_pass:
                Intent intent1 = new Intent(getActivity(),ChangePassActivity.class);
                intent1.putExtra("userId",user.getUser_stuNumber());
                intent1.putExtra("type","type02");
                startActivityForResult(intent1, 0);
                break;
            case R.id.frag_mine_about_us:
                ToastEx.success(getContext(),"mine_my_about").show();
                break;
            case R.id.frag_mine_setting:
                IntentUtil.get().goActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.frag_mine_bt_quit:
               mHandle.sendEmptyMessage(1009);
                break;
        }
    }

    public void selectUserInfo(String account) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        user = object.get(0);
                        mHandle.sendEmptyMessage(1001);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        selectUserInfo(id);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0||resultCode==0){
            if(data.getStringExtra("aa").equals("ok")){
                mHandle.sendEmptyMessage(1009);

            }
        }
    }
}
