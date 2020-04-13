package com.example.fleamarket.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.util.ArrayList;

public class TakePhotoActivity extends AppCompatActivity implements TakePhoto.TakeResultListener, InvokeListener {
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private int flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        flag = getIntent().getIntExtra("flag", 0);
        File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
        Uri uri = Uri.fromFile(file);
        int size = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        CropOptions cropOptions = new CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create();
        if (flag == 1) {
            Log.v("wht", "flag :" + flag);
            //相机获取照片并剪裁
            takePhoto.onPickFromCaptureWithCrop(uri, cropOptions);
            //相机获取不剪裁
            //takePhoto.onPickFromCapture(uri);
        } else if (flag == 2) {
            //相册获取照片并剪裁
            takePhoto.onPickFromGalleryWithCrop(uri, cropOptions);
            //相册获取不剪裁
//      takePhoto.onPickFromGallery();
        } else if (flag == 3) {
            //多选，并剪裁
            takePhoto.onPickMultipleWithCrop(9, cropOptions);
            //多选，不剪裁
//      takePhoto.onPickMultiple(9);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        //setMaxPixel(100)设置裁剪图片的大小
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(100).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (flag != 3) {
            String compressPath = result.getImage().getCompressPath();
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", null != compressPath ? compressPath : result.getImage().getOriginalPath()));
            Log.v("wht", "compressPath:" + compressPath);
            Log.v("wht", "OriginalPath:" + result.getImage().getOriginalPath());
        } else {
            ArrayList<TImage> images = result.getImages();
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", images));
        }
        finish();
    }
    @Override
    public void takeFail(TResult result, String msg) {
        Log.v("wht", "takeFail:" + msg);
        if (flag != 3) {
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", ""));
        } else {
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", new ArrayList()));
        }
        finish();
    }

    @Override
    public void takeCancel() {
        //Log.v("wht", getResources().getString(R.string.msg_operation_canceled));
        if (flag != 3) {
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", ""));
        } else {
            setResult(Activity.RESULT_OK, new Intent().putExtra("data", new ArrayList()));
        }
        finish();
    }
}