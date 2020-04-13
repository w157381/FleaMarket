package com.example.fleamarket.view.activity.admin.AdminLunbo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.fleamarket.R;

import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.utils.GlideLoader;

import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lcw.library.imagepicker.ImagePicker;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AdminAddLunBoInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private EditText et_title, et_url;
    private SimpleDraweeView img;
    private Button bt;

    private ZLoadingDialog dialog;
    private Intent data;
    private ArrayList<String> imgs;
    private LinearLayout linea;
    private Advertising advertising;
    private int REQUEST_CODE = 14;


    @Override
    protected int initLayout() {
        return R.layout.admin_lunbo_add;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.admin_addlunbo_back);
        et_title = findViewById(R.id.admin_addlunbo_et_title);
        et_url = findViewById(R.id.admin_addlunbo_et_url);
        img = findViewById(R.id.admin_addlunbo_img);
        linea = findViewById(R.id.admin_addlunbo_liner);
        bt = findViewById(R.id.admin_addlunbo_sumbit);
    }

    @Override
    protected void initData() {
        advertising = new Advertising();
        imgs = new ArrayList<>();
        data = new Intent();
        img_back.setOnClickListener(this);
        linea.setOnClickListener(this);
        bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_addlunbo_back:
                data.putExtra("aa", "no");
                setResult(0, data);
                finish();
                break;
            case R.id.admin_addlunbo_liner:
                ImagePicker.getInstance()
                        .setTitle("选择照片")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(AdminAddLunBoInfoActivity.this, REQUEST_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                break;
            case R.id.admin_addlunbo_sumbit:
                String title = et_title.getText().toString().trim();
                String url = et_url.getText().toString().trim();
                if (title.equals("")) {
                    ToastEx.warning(AdminAddLunBoInfoActivity.this, "请添加标题！").show();
                    return;
                }
                if (url.equals("")) {
                    ToastEx.warning(AdminAddLunBoInfoActivity.this, "请添加访问地址！").show();
                    return;
                }
                if (imgs.get(0).equals("")) {
                    ToastEx.warning(AdminAddLunBoInfoActivity.this, "请添加主题照片！").show();
                    return;
                }
                advertising.setAdv_title(title);
                advertising.setAdv_url(url);
                addFile();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            imgs = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            img.setImageURI("file://" + imgs.get(0));
        }
    }

    private void addFile() {
        dialog = LoadingUtil.loading(AdminAddLunBoInfoActivity.this, "数据上传中......");
        final BmobFile bmobFile = new BmobFile(new File(imgs.get(0)));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                advertising.setAdv_imgurl(bmobFile.getFileUrl());
                advertising.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null){
                            data.putExtra("aa","ok");
                            data.putExtra("advertising",advertising);
                            setResult(0,data);
                            finish();
                        }else{

                        }

                    }
                });
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

