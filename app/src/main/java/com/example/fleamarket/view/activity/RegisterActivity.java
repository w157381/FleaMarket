package com.example.fleamarket.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.BrowsGoodsHistory;
import com.example.fleamarket.bean.Collect;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.MyAddress;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.bean.SearchHistory;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.bean.Warning;
import com.example.fleamarket.utils.CheckNetWorkUtil;
import com.example.fleamarket.utils.ConnectUtil;
import com.example.fleamarket.utils.GetLocationUtil;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.utils.Md5util;
import com.example.fleamarket.view.BaseActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private ImageView img_headImg;
    private EditText et_nickname;
    private EditText et_introduce;
    private TextView tv_locationNow;
    private ImageView img_loaction_now;
    private EditText et_password;
    private EditText et_passwordag;
    private Button bt_go;
    //等待界面
    private ZLoadingDialog dialog;

    private User user;
    private String picPath = "";
    private BottomSheetDialog sheetDialog;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        img_back = (ImageView) findViewById(R.id.register_back);
        img_headImg = (ImageView) findViewById(R.id.register_headimg);
        et_nickname = (EditText) findViewById(R.id.register_nickname);
        et_introduce = (EditText) findViewById(R.id.register_introduce);
        tv_locationNow = (TextView) findViewById(R.id.register_location_now);
        img_loaction_now = (ImageView) findViewById(R.id.register_location_dingwei);
        et_password = (EditText) findViewById(R.id.register_password);
        et_passwordag = (EditText) findViewById(R.id.register_passwordag);
        bt_go = (Button) findViewById(R.id.register_bt);

    }

    @Override
    protected void initData() {
        //设置监听器
        img_back.setOnClickListener(this);
        img_headImg.setOnClickListener(this);
        tv_locationNow.setOnClickListener(this);
        img_loaction_now.setOnClickListener(this);
        bt_go.setOnClickListener(this);
        //承接数据
        user = (User) getIntent().getExtras().get("user");
        //设置地理位置
        tv_locationNow.setText("" + GetLocationUtil.getLocation(RegisterActivity.this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCamera:
                startActForResult(1, 101);
                sheetDialogDismiss();
                break;
            case R.id.tvPhoto:
                startActForResult(2, 102);
                sheetDialogDismiss();
                break;
            case R.id.tvCancel:
                sheetDialogDismiss();
                break;
            case R.id.register_back:
                finish();
                break;
            case R.id.register_headimg:
                showDialog();
                break;
            case R.id.register_location_now:
                tv_locationNow.setText(GetLocationUtil.getLocation(RegisterActivity.this));
                break;
            case R.id.register_location_dingwei:
                tv_locationNow.setText(GetLocationUtil.getLocation(RegisterActivity.this));
                break;
            case R.id.register_bt:
                if (CheckNetWorkUtil.netWorkCheck(RegisterActivity.this)) {

                    String nickname = et_nickname.getText().toString().trim();
                    String introduce = et_introduce.getText().toString().trim();
                    String location = tv_locationNow.getText().toString().trim();
                    String pass = et_password.getText().toString().trim();
                    String passag = et_passwordag.getText().toString().trim();
                    if (picPath.equals("")) {
                        ToastEx.warning(RegisterActivity.this, "请设置头像！").show();
                        return;
                    }
                    if (nickname.equals("")) {
                        ToastEx.warning(RegisterActivity.this, "请设置昵称！").show();
                        return;
                    }
                    if (introduce.equals("")) {
                        introduce = "这个人很懒 !>_<! 什么都没写";

                    }
                    if (location.equals("")) {
                        ToastEx.warning(RegisterActivity.this, "请设置地址！").show();
                        return;
                    }
                    if (pass.equals("")) {
                        ToastEx.warning(RegisterActivity.this, "请设置密码！").show();
                        return;
                    }
                    if (passag.equals("")) {
                        ToastEx.warning(RegisterActivity.this, "请确认密码！").show();
                        return;
                    }
                    if (!pass.equals(passag)) {
                        ToastEx.warning(RegisterActivity.this, "两次密码输入不一致！").show();
                        return;
                    }
                    user.setUser_nickName(nickname);
                    user.setUser_introduce(introduce);
                    user.setUser_locationNow(location);
                    user.setUser_password(Md5util.getMD5(pass));
                    user.setUser_credit(80);
                    user.setUser_loginState(1);
                    user.setUser_type(0);
                    user.setUser_warning(new ArrayList<Warning>());
                    addBmob(user, picPath);
                    reCollect();
                    readdAddressInfo();
                    reSearchHistory();
                    bt_go.setEnabled(false);
                } else {
                    ToastEx.warning(RegisterActivity.this, "请检查网络！").show();
                }
                break;
        }
    }

    /**
     * 该方法是从底部弹出对话框
     */
    private void showDialog() {
        sheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_take_photo, null);
        TextView tvCamera = view.findViewById(R.id.tvCamera);
        tvCamera.setOnClickListener(this);
        TextView tvPhoto = view.findViewById(R.id.tvPhoto);
        tvPhoto.setOnClickListener(this);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);
        sheetDialog.setContentView(view);
        sheetDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        picPath = data.getStringExtra("data");
        if (requestCode == 101) {
            sss(picPath);
        } else if (requestCode == 102) {
            sss(picPath);
        }
    }

    /**
     * 获取到处理好的图片路径显示到ImageView控件上
     */
    private void sss(String path) {
        File file = new File(path);
        if (file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(path);
            img_headImg.setImageBitmap(bm);
        }
    }

    private void startActForResult(int flag, int requestCode) {
        startActivityForResult(new Intent(RegisterActivity.this, TakePhotoActivity.class).putExtra("flag", flag), requestCode);
    }

    private void sheetDialogDismiss() {
        if (null != sheetDialog && sheetDialog.isShowing()) {
            sheetDialog.dismiss();
        }
    }

    //上传头像
    public void addBmob(final User user, String picPath) {
        dialog = LoadingUtil.loading(RegisterActivity.this, "努力注册中......");
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    user.setUser_headImg(bmobFile.getFileUrl());
                    addBmob_User(user);
                } else {
                    ToastEx.success(RegisterActivity.this, "上传文件失败：" + e.getMessage()).show();
                    bt_go.setEnabled(true);
                    dialog.cancel();
                }
            }

            @Override
            public void onProgress(Integer value) {
                Log.e("value", value + "");
            }
        });
    }

    //注册信息
    public void addBmob_User(final User user) {
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("fleamarket", MODE_PRIVATE);
                    ToastEx.success(RegisterActivity.this, "注册成功！").show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("account", user.getUser_stuNumber());
                    editor.putString("password", user.getUser_password());
                    editor.putString("vibrator", "true");
                    editor.putString("alert", "true");
                    editor.putString("mediaplayer", "true");
                    editor.putString("auto", "true");
                    editor.commit();
                    dialog.cancel();

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
                                Log.e("connect","服务器连接成功！"+BmobIM.getInstance().getCurrentUid());
                                IntentUtil.get().goActivity(RegisterActivity.this, MainActivity.class);
                                finish();
                            } else {
                                //连接失败
                                Log.e("connect","服务器连接失败！"+BmobIM.getInstance().getCurrentUid());
                            }
                        }
                    });

                } else {
                    ToastEx.success(RegisterActivity.this, "创建数据失败：" + e.getMessage()).show();
                    dialog.cancel();
                    bt_go.setEnabled(true);

                }
            }
        });
    }

    //创建收藏表
    public void reCollect() {
        Collect collect = new Collect();
        collect.setUserid(user.getUser_stuNumber());
        collect.setGoodsList(new ArrayList<Goods>());
        collect.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.e("1212", "reCollect" + s);
                } else {
                    Log.e("1212", "reCollect" + e.getMessage());
                }
            }
        });
    }

    //创建地址表
    public void readdAddressInfo() {

        MyAddress myAddress = new MyAddress();
        myAddress.setAddr_userId(user.getUser_stuNumber());
        myAddress.setAddressitems(new ArrayList<MyAddressitem>());
        myAddress.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.e("1212", "" + objectId);
                } else {
                    Log.e("1212", "" + e.getMessage());
                }
            }
        });
    }

    //创建历史搜索信息
    public void reSearchHistory() {

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setUserId(user.getUser_stuNumber());
        searchHistory.setLists(new ArrayList<String>());
        searchHistory.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.e("1212", "reSearchHistory:" + objectId);
                } else {
                    Log.e("1212", "reSearchHistory" + e.getMessage());
                }
            }
        });
    }
}
