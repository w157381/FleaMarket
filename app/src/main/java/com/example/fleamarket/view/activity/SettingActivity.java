package com.example.fleamarket.view.activity;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.widget.SwitchCompat;

import com.example.fleamarket.R;
import com.example.fleamarket.view.BaseActivity;
import com.zcw.togglebutton.ToggleButton;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private ToggleButton sw_vibrator,sw_mediaPlayer;
    //轻量级的存储类 账号密码储存
    private SharedPreferences sharedPreferences;

    @Override
    protected int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.setting_back);
        sw_vibrator = findViewById(R.id.setting_vibrator);
        sw_mediaPlayer = findViewById(R.id.setting_mediaPlayer);

    }

    @Override
    protected void initData() {
        img_back.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("fleamarket", MODE_PRIVATE);

        if (sharedPreferences != null) {
            String str_vibrator = sharedPreferences.getString("vibrator", "");
            String str_mediaplayer = sharedPreferences.getString("mediaplayer", "");
            if (str_vibrator.equals("true")) {
                sw_vibrator.setToggleOn();
            }else{
                sw_vibrator.setToggleOff();
            }
            if (str_mediaplayer.equals("true")) {
                sw_mediaPlayer.setToggleOn();
            }else{
                sw_mediaPlayer.setToggleOff();
            }

        }
        sw_vibrator.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("vibrator", "true");
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("vibrator", "false");
                    editor.commit();
                }

            }
        });
        sw_mediaPlayer.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mediaplayer", "true");
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mediaplayer", "false ");
                    editor.commit();
                }

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_back:
                finish();
                break;
        }

    }
}
