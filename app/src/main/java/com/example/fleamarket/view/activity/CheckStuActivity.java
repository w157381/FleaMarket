package com.example.fleamarket.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.CheckNetWorkUtil;
import com.example.fleamarket.utils.CommonUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.utils.RequestParamUtils;
import com.example.fleamarket.view.BaseActivity;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckStuActivity extends BaseActivity implements View.OnClickListener {
    //返回上级界面
    private ImageView img_back;
    //清除帐号、密码
    private ImageView img_stuid_del;
    private ImageView img_stupass_dis;
    //学号
    private EditText et_stuid;
    //教务系统密码
    private EditText et_stupass;
    //验证
    private Button bt_checkgo;
    //密码是否显示
    private boolean passIsDisplay = false;
    //等待界面
    private ZLoadingDialog dialog;

    //获取操作类型  0：注册新用户 1：忘记密码
    private int type;
    private Handler mHandler = new Handler() {
        //接收消息
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                //取出消息中的信息
                Log.i("localvar", "接收到空消息1001");
                dialog.cancel();
                ToastEx.success(CheckStuActivity.this, "验证成功！").show();
            } else if (msg.what == 1002) {
                Log.i("localvar", "接收到空消息1002");
            } else if (msg.what == 4040) {
                Log.i("localvar", "接收到空消息4040");
                dialog.cancel();
                ToastEx.error(CheckStuActivity.this, "验证失败1！").show();
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_checkstu;
    }

    @Override
    protected void initView() {
        //控件获取
        img_back = (ImageView) findViewById(R.id.register_back);
        img_stuid_del = (ImageView) findViewById(R.id.register_stuid_del);
        img_stupass_dis = (ImageView) findViewById(R.id.register_stupass_dis);
        et_stuid = (EditText) findViewById(R.id.register_stuid);
        et_stupass = (EditText) findViewById(R.id.register_stupass);
        bt_checkgo = (Button) findViewById(R.id.register_check_bt);

    }

    @Override
    protected void initData() {

        //监听器
        img_back.setOnClickListener(this);
        img_stupass_dis.setOnClickListener(this);
        img_stuid_del.setOnClickListener(this);
        bt_checkgo.setOnClickListener(this);

        //获取操作类型  0：注册新用户 1：忘记密码
        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_stuid_del:
                et_stupass.setText("");
                et_stuid.setText("");
                break;
            case R.id.register_stupass_dis:
                if (passIsDisplay) {
                    passIsDisplay = false;
                    et_stupass.setInputType(129);//设置为隐藏密码
                    img_stupass_dis.setImageDrawable(getDrawable(R.drawable.login_input_password_display));
                } else {
                    passIsDisplay = true;
                    et_stupass.setInputType(128);//设置为显示密码
                    img_stupass_dis.setImageDrawable(getDrawable(R.drawable.login_input_password_display2));
                }
                break;
            case R.id.register_check_bt:
                String id = et_stuid.getText().toString().trim();
                String pass = et_stupass.getText().toString().trim();
                if (id.equals("")) {
                    ToastEx.warning(CheckStuActivity.this, "学号不能为空！").show();
                    return;
                }
                if (pass.equals("")) {
                    ToastEx.warning(CheckStuActivity.this, "密码不能为空！").show();
                    return;
                }
                if (!CheckNetWorkUtil.netWorkCheck(CheckStuActivity.this)) {
                    ToastEx.warning(CheckStuActivity.this, "请检查网络设置").show();
                    return;
                }
                if(type==0){//注册操作
                    selectUserInfo(id, pass);
                }
                if(type==1){//忘记密码操作
                    getStudentInformation(id,pass);
                }


                break;
        }

    }

    /**
     * 获取学生的信息
     *
     * @param id       学号
     * @param password 密码
     */
    private void getStudentInformation(final String id, String password) {
        dialog = LoadingUtil.loading(CheckStuActivity.this, "验证中...");
        final String param = "xxdm=10482&loginId=" + id + "&appver=2.5.103&action=getLoginInfoNew&pwd=" + password + "&loginmode=0";
        final OkHttpClient client = new OkHttpClient();
        final RequestBody body = new FormBody.Builder()
                .add("param", RequestParamUtils.of_encrypt(param, "op5nb9"))
                .add("param2", CommonUtil.getInstance().md5(param))
                .add("appinfo", "android2.5.103")
                .add("token", "000000")
                .build();
        final Request request = new Request.Builder()
                .url("http://www.xiqueer.com:8080/manager//wap/wapController.jsp")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("1212","123123");
                mHandler.sendEmptyMessage(4040);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    final String uuid = object.getString("uuid");
                    String token = object.getString("token");
                    String param = "usertype=STU&userId=10482_" + id + "&sfid=10482_" + id + "&uuid=" + uuid;
                    RequestBody body = new FormBody.Builder()
                            .add("param", RequestParamUtils.of_encrypt(param, "op5nb9"))
                            .add("param2", CommonUtil.getInstance().md5(param))
                            .add("appinfo", "android2.5.103")
                            .add("token", token)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://www.xiqueer.com:8080/manager//wap/baseInfoServlet")
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                            mHandler.sendEmptyMessage(4040);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                User user = new User();
                                user.setUser_name(object.optString("xm", ""));//姓名
                                user.setUser_stuNumber(object.optString("xh", ""));//学号
                                user.setUser_sex(object.optString("xb", ""));//性别
                                user.setUser_birthday(object.optString("csrq", ""));//出生日期
                                user.setUser_department(object.optString("yx", ""));//院系
                                user.setUser_major(object.optString("zy", ""));//专业
                                user.setUser_grade(object.optString("rxnj", ""));//入学年级
                                user.setUser_class(object.optString("ssbj", ""));//班级
                                user.setUser_locationLive(object.optString("jg", ""));//籍贯
                                user.setUser_univercity(object.optString("xxmc", ""));
                                if(type==0){//跳转注册
                                    mHandler.sendEmptyMessage(1001);
                                    Intent intent = new Intent(CheckStuActivity.this, RegisterActivity.class);
                                    intent.putExtra("user",user);
                                    startActivity(intent);
                                    finish();
                                }
                                if(type==1){//跳转修改密码
                                    mHandler.sendEmptyMessage(1001);
                                    Intent intent1 = new Intent(CheckStuActivity.this,ChangePassActivity.class);
                                    intent1.putExtra("userId",user.getUser_stuNumber());
                                    intent1.putExtra("type","type01");
                                    startActivity(intent1);
                                    finish();
                                }

                                Log.i("姓名", "" + user.getUser_name());
                                Log.i("学号", "" + user.getUser_stuNumber());
                                Log.i("性别", "" + user.getUser_sex());
                                Log.i("出生日期", "" + user.getUser_birthday());
                                Log.i("院系", "" + user.getUser_department());
                                Log.i("专业", "" + user.getUser_major());
                                Log.i("入学年级", "" + user.getUser_grade());
                                Log.i("班级", "" + user.getUser_class());
                                Log.i("籍贯", "" + user.getUser_locationLive());
                                Log.i("学校", "" + user.getUser_univercity());

                            } catch (Exception e) {

                                Log.e("1212","11111:"+e.getMessage());
                                mHandler.sendEmptyMessage(4040);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("1212","22222");
                    mHandler.sendEmptyMessage(4040);
                }
            }
        });
    }

    public void selectUserInfo(final String account, final String pass) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        ToastEx.warning(CheckStuActivity.this, "账号信息已存在，请登录！").show();
                        finish();
                    } else {
                        getStudentInformation(account, pass);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
}
