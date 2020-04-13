package com.example.fleamarket.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.LogisticsInfo;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.CheckNetWorkUtil;
import com.example.fleamarket.utils.ConnectUtil;
import com.example.fleamarket.utils.GetNetImgUtil;
import com.example.fleamarket.utils.GetTimeUtil;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.utils.Md5util;
import com.example.fleamarket.view.BaseActivity;
import com.example.fleamarket.view.activity.admin.AdminMainActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.scwang.smartrefresh.header.flyrefresh.FlyView;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.leefeng.promptlibrary.PromptDialog;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    //用户头像
    private SimpleDraweeView sim_headImg;
    //账号
    private EditText et_account;
    //密码
    private EditText et_password;
    //清空账号
    private ImageView img_account_del;
    ///显示密码
    private ImageView img_password_dis;
    //登录
    private Button bt_login;
    //注册
    private TextView tv_register;
    //忘记密码
    private TextView tv_forgetpassword;
    //登录成功提示
    private PromptDialog promptDialog;
    //密码是否显示
    private boolean passIsDisplay = false;
    //轻量级的存储类 账号密码储存
    private SharedPreferences sharedPreferences;
    //记住密码
    private boolean isAutoLogin = true;
    //等待界面
    private ZLoadingDialog dialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        //控件获取
        sim_headImg = (SimpleDraweeView) findViewById(R.id.login_userheadimg);
        et_account = (EditText) findViewById(R.id.login_account);
        et_password = (EditText) findViewById(R.id.login_password);
        img_account_del = (ImageView) findViewById(R.id.login_account_del);
        img_password_dis = (ImageView) findViewById(R.id.login_password_dis);
        bt_login = (Button) findViewById(R.id.login_bt);
        tv_register = (TextView) findViewById(R.id.login_register);
        tv_forgetpassword = (TextView) findViewById(R.id.login_forgetpassword);
    }

    @Override
    protected void initData() {
        //初始化操作
        promptDialog = new PromptDialog(this);
        sharedPreferences = getSharedPreferences("fleamarket", MODE_PRIVATE);
        ////输入框图标监听
        img_account_del.setOnClickListener(this);
        img_password_dis.setOnClickListener(this);
        ////tv--忘记密码、注册 点击监听
        tv_forgetpassword.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        ////et_account 焦点监听处理
        et_account.setFocusable(true);//获得焦点
        et_account.setFocusableInTouchMode(true);//获得焦点
        et_account.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    // 此处为失去焦点时的处理内容
                    String account = et_account.getText().toString().trim();
                    //设置头像
                    if (!account.equals("")) setHeadImg(account);
                }
            }
        });

        if (sharedPreferences != null) {
            String aa = sharedPreferences.getString("auto", "");
            String aabb = sharedPreferences.getString("account", "");
            if (aa.equals("true")) {

                BmobIM.connect(aabb, new ConnectListener() {
                    @Override
                    public void done(String uid, BmobException e) {
                        if (e == null) {
                            //连接成功
                            ToastEx.success(LoginActivity.this, "服务器连接成功！").show();

                        } else {
                            //连接失败
                            Log.e("connect", "服务器连接失败！" + BmobIM.getInstance().getCurrentUid());
                        }
                    }
                });
                IntentUtil.get().goActivity(LoginActivity.this, MainActivity.class);
                finish();

            } else {
                String account = sharedPreferences.getString("account", "");
                et_account.setText(account);
                setHeadImg(account);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_account_del:
                et_account.setText("");
                et_password.setText("");
                break;
            case R.id.login_password_dis:
                if (passIsDisplay) {
                    passIsDisplay = false;
                    et_password.setInputType(129);//设置为隐藏密码
                    img_password_dis.setImageDrawable(getDrawable(R.drawable.login_input_password_display));
                } else {
                    passIsDisplay = true;
                    et_password.setInputType(128);//设置为显示密码
                    img_password_dis.setImageDrawable(getDrawable(R.drawable.login_input_password_display2));
                }
                break;
            case R.id.login_register:
                Intent intent = new Intent(LoginActivity.this, CheckStuActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;

            case R.id.login_forgetpassword:
                Intent intent1 = new Intent(LoginActivity.this, CheckStuActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;

            case R.id.login_bt:
                String account = et_account.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (account.equals("")) {
                    ToastEx.warning(LoginActivity.this, "账号不能为空！").show();
                    return;
                }
                if (password.equals("")) {
                    ToastEx.warning(LoginActivity.this, "密码不能为空！").show();
                    return;
                }
                if (CheckNetWorkUtil.netWorkCheck(LoginActivity.this)) {
                    login(account, Md5util.getMD5(password));
                } else {
                    ToastEx.warning(LoginActivity.this, "请检查网络！").show();
                }
                break;
        }

    }

    //登录
    public void login(final String account, final String password) {
        dialog = LoadingUtil.loading(LoginActivity.this, "登录中......");
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        bt_login.setEnabled(false);
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        final User user = object.get(0);
                        //1.判断用户账号登录状态----//登录状态(-1：永久冻结；0:冻结三天，1：正常登录)
                        if (user.getUser_loginState() == -1) {//-1：永久冻结
                            ToastEx.error(LoginActivity.this, "账号冻结，请联系管理员：QQ 2825390501").show();
                            dialog.cancel();
                            bt_login.setEnabled(true);

                        } else if (user.getUser_loginState() == 0) {//0:冻结三天
                            //日期计算
                            String unFreezeTime = formatTime(GetTimeUtil.getTimelongExpend(GetTimeUtil.gettime(), user.getUser_freezeDate()));
                            ToastEx.warning(LoginActivity.this, "账号冻结，解冻时间剩余：" + unFreezeTime).show();
                            dialog.cancel();
                            bt_login.setEnabled(true);
                        } else if (user.getUser_loginState() == 1) {

                            if (user.getUser_password().equals(password)) {
                                if (user.getUser_type() == 0) {

                                    editor.putString("account", account);
                                    editor.putString("password", password);
                                    editor.putString("vibrator", "true");
                                    editor.putString("mediaplayer", "true");
                                    editor.putString("auto", "true");
                                    editor.commit();
                                    bt_login.setEnabled(true);

                                    BmobIM.connect(user.getUser_stuNumber(), new ConnectListener() {
                                        @Override
                                        public void done(String uid, BmobException e) {
                                            if (e == null) {
                                                //连接成功
                                                BmobIMUserInfo info = new BmobIMUserInfo();
                                                info.setUserId(user.getUser_stuNumber());
                                                info.setName(user.getUser_nickName());
                                                info.setAvatar(user.getUser_headImg());
                                                BmobIM.getInstance().updateUserInfo(info);

                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                intent.putExtra("user", user);
                                                startActivity(intent);
                                                ToastEx.success(LoginActivity.this, "登录成功" + user.getUser_stuNumber()).show();
                                                Log.e("connect", "服务器连接成功！" + BmobIM.getInstance().getCurrentUid());
                                                dialog.cancel();
                                                finish();
                                            } else {
                                                //连接失败
                                                Log.e("connect", "服务器连接失败！" + BmobIM.getInstance().getCurrentUid());
                                            }
                                        }
                                    });

                                }
                                if (user.getUser_type() == 1) {
                                    final NormalDialog dialoga = new NormalDialog(LoginActivity.this);
                                    dialoga
                                            .isTitleShow(false)
                                            .content("请选择进入的界面")//
                                            .cornerRadius(10)//
                                            .style(NormalDialog.STYLE_TWO)//
                                            .titleTextSize(23)//
                                            .btnText("客户端", "管理端")//
                                            .btnTextColor(Color.parseColor("#383838"), Color.parseColor("#383838"))//
                                            .btnTextSize(16f, 16f)//
                                            .show();

                                    dialoga.setOnBtnClickL(
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {

                                                    editor.putString("account", account);
                                                    editor.putString("password", password);
                                                    editor.putString("vibrator", "true");
                                                    editor.putString("mediaplayer", "true");
                                                    editor.putString("auto", "true");
                                                    editor.commit();
                                                    bt_login.setEnabled(true);

                                                    BmobIM.connect(user.getUser_stuNumber(), new ConnectListener() {
                                                        @Override
                                                        public void done(String uid, BmobException e) {
                                                            if (e == null) {
                                                                //连接成功
                                                                BmobIMUserInfo info = new BmobIMUserInfo();
                                                                info.setUserId(user.getUser_stuNumber());
                                                                info.setName(user.getUser_nickName());
                                                                info.setAvatar(user.getUser_headImg());
                                                                BmobIM.getInstance().updateUserInfo(info);

                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent.putExtra("user", user);
                                                                startActivity(intent);
                                                                ToastEx.success(LoginActivity.this, "登录成功" + user.getUser_stuNumber()).show();
                                                                Log.e("connect", "服务器连接成功！" + BmobIM.getInstance().getCurrentUid());
                                                                dialog.cancel();
                                                                finish();
                                                            } else {
                                                                //连接失败
                                                                Log.e("connect", "服务器连接失败！" + BmobIM.getInstance().getCurrentUid());
                                                            }
                                                        }
                                                    });
                                                    dialoga.dismiss();
                                                }
                                            },
                                            new OnBtnClickL() {
                                                @Override
                                                public void onBtnClick() {//管理端
                                                    bt_login.setEnabled(true);
                                                    IntentUtil.get().goActivityKill(LoginActivity.this, AdminMainActivity.class);
                                                    finish();
                                                    dialoga.dismiss();
                                                }
                                            });
                                }

                            } else {
                                bt_login.setEnabled(true);
                                ToastEx.warning(LoginActivity.this, "密码错误").show();
                                dialog.cancel();
                            }
                        }
                    } else {
                        bt_login.setEnabled(true);
                        ToastEx.warning(LoginActivity.this, "账号信息不存在").show();
                        dialog.cancel();
                    }
                } else {
                    bt_login.setEnabled(true);
                    ToastEx.warning(LoginActivity.this, "，错误描述：" + e.getMessage()).show();
                    dialog.cancel();
                }
            }
        });
    }

    //头像设置
    public void setHeadImg(String account) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        User user = object.get(0);
                        GetNetImgUtil.roadingNetImg(sim_headImg, user.getUser_headImg());
                    } else {
                        ToastEx.warning(LoginActivity.this, "账号信息不存在，请注册！").show();
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

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
}
