package com.example.fleamarket.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.dialog.UpdateTextDialog;
import com.example.fleamarket.utils.GetLocationUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.io.File;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private TextView save;
    private SimpleDraweeView headimg;
    private ImageView img_headimg;
    private TextView name;
    private TextView stuId;
    private TextView nickname;
    private ImageView img_nickname;
    private TextView sex;
    private TextView birthday;
    private TextView introduce;
    private ImageView img_introduce;
    private TextView credit;
    private TextView univircity;
    private TextView department;
    private TextView classa;
    private TextView major;
    private TextView grade;
    private TextView location;
    private TextView nowlocation;
    private UpdateTextDialog updateTextDialog;
    private String picPath ="";
    private BottomSheetDialog sheetDialog;
    //等待界面
    private ZLoadingDialog dialog;
    //数据信息
    private User user;
    private Message msg1,msg2;

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1001){
                headimg.setImageURI(Uri.parse(user.getUser_headImg()));
                stuId.setText(""+user.getUser_stuNumber());
                name.setText(""+user.getUser_name());
                nickname.setText(""+user.getUser_nickName());
                sex.setText(""+user.getUser_sex());
                birthday.setText(""+user.getUser_birthday());
                introduce.setText(""+user.getUser_introduce());
                credit.setText(""+user.getUser_credit());
                univircity.setText(""+user.getUser_univercity());
                department.setText(""+user.getUser_department());
                classa.setText(""+user.getUser_class());
                major.setText(""+user.getUser_major());
                grade.setText(""+user.getUser_grade());
                location.setText(""+user.getUser_locationLive());
                nowlocation.setText(""+user.getUser_locationNow());
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_myinfo;
    }

    @Override
    protected void initView() {
        back = findViewById(R.id.myinfo_back);
        save = findViewById(R.id.myinfo_save);
        headimg = findViewById(R.id.myinfo_headimg);
        img_headimg = findViewById(R.id.myinfo_img_head);
        name = findViewById(R.id.myinfo_name);
        stuId = findViewById(R.id.myinfo_stuid);
        nickname = findViewById(R.id.myinfo_nickname);
        img_nickname = findViewById(R.id.myinfo_img_nickname);
        sex = findViewById(R.id.myinfo_sex);
        birthday = findViewById(R.id.myinfo_birthday);
        introduce = findViewById(R.id.myinfo_introduce);
        img_introduce = findViewById(R.id.myinfo_img_introduce);
        credit = findViewById(R.id.myinfo_credit);
        univircity = findViewById(R.id.myinfo_univircity);
        department = findViewById(R.id.myinfo_department);
        classa = findViewById(R.id.myinfo_class);
        major = findViewById(R.id.myinfo_major);
        grade = findViewById(R.id.myinfo_grade);
        location = findViewById(R.id.myinfo_location);
        nowlocation = findViewById(R.id.myinfo_nowlocation);
    }
    @Override
    protected void initData() {
        //监听器注册
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        img_headimg.setOnClickListener(this);
        img_nickname.setOnClickListener(this);
        img_introduce.setOnClickListener(this);
        nowlocation.setOnClickListener(this);
        //初始化
        user = new User();
        msg1 = new Message();
        msg1.what=1001;
        msg2 = new Message();
        msg2.what=1002;
        selectUserInfo(BmobIM.getInstance().getCurrentUid());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

            case R.id.myinfo_back:
                finish();
                break;
            case R.id.myinfo_save:
                dialog =  LoadingUtil.loading(MyInfoActivity.this,"请稍后...");
                if(picPath.equals("")){
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setUserId(user.getUser_stuNumber());
                    info.setName(user.getUser_nickName());
                    info.setAvatar(user.getUser_headImg());
                    BmobIM.getInstance().updateUserInfo(info);
                    upBmob_User(user);
                    dialog.cancel();
                }else{
                    addBmob(user,picPath);
                }

                break;
            case R.id.myinfo_img_head:
                showDialog();
                break;
            case R.id.myinfo_img_nickname:
                updateTextDialog = new UpdateTextDialog(this, R.style.Dialog_Common, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = updateTextDialog.text.getText().toString().trim();
                        if (text.equals("")){
                            ToastEx.warning(v.getContext(),"输入不能为空！").show();
                            return;
                        }
                        nickname.setText(""+text);
                        user.setUser_nickName(text);
                        updateTextDialog.cancel();
                    }
                });
                updateTextDialog.show();
                break;
            case R.id.myinfo_img_introduce:
                updateTextDialog = new UpdateTextDialog(this, R.style.Dialog_Common, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = updateTextDialog.text.getText().toString().trim();
                        if (text.equals("")){
                            ToastEx.warning(v.getContext(),"输入不能为空！").show();
                            return;
                        }
                        introduce.setText(""+text);
                        user.setUser_introduce(text);
                        updateTextDialog.cancel();
                    }
                });
                updateTextDialog.show();
                break;
            case R.id.myinfo_nowlocation:
               String aa =  GetLocationUtil.getLocation(MyInfoActivity.this);
               user.setUser_locationNow(aa);
               nowlocation.setText(""+aa);
               break;
        }
    }
    /**
     *该方法是从底部弹出对话框
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
    public void selectUserInfo(String account){
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber",account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if(object.size()>0){
                        user = object.get(0);
                        mHandle.sendMessage(msg1);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    /**
     *获取到处理好的图片路径显示到ImageView控件上
     */
    private void sss(String path){
        File file = new File(path);
        Log.e("123123","123123");
        if(file.exists()){
            String uri= "file://"+path;
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setTapToRetryEnabled(true)
                    .build();
            headimg.setController(controller);
        }
    }
    private void startActForResult(int flag, int requestCode) {
        startActivityForResult(new Intent(MyInfoActivity.this, TakePhotoActivity.class).putExtra("flag", flag), requestCode);
    }

    private void sheetDialogDismiss() {
        if (null != sheetDialog && sheetDialog.isShowing()) {
            sheetDialog.dismiss();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        picPath = data.getStringExtra("data");
        if (requestCode == 101) {
            sss(picPath);
        } else if (requestCode == 102) {
            sss(picPath);
        }
    }
    //上传头像
    public void addBmob(final User user, String picPath){
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    user.setUser_headImg(bmobFile.getFileUrl());
                    BmobIMUserInfo info = new BmobIMUserInfo();
                    info.setUserId(user.getUser_stuNumber());
                    info.setName(user.getUser_nickName());
                    info.setAvatar(user.getUser_headImg());
                    BmobIM.getInstance().updateUserInfo(info);
                    upBmob_User(user);
                    dialog.cancel();
                }else{
                    ToastEx.error(MyInfoActivity.this,"上传文件失败：" + e.getMessage()).show();
                    save.setEnabled(true);
                    dialog.cancel();
                }
            }
            @Override
            public void onProgress(Integer value) {
                Log.e("value", value+"");
            }
        });
    }
    public void upBmob_User(final User user){
        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastEx.success(MyInfoActivity.this,"更新成功").show();
                    finish();
                }else{
                    ToastEx.error(MyInfoActivity.this,"更新失败").show();
                }
            }
        });
    }
}