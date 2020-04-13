package com.example.fleamarket.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.BulletinText;

import java.util.List;

import me.bakumon.library.adapter.BulletinAdapter;

public class SaleAdapter extends BulletinAdapter{

    private List<BulletinText> data;
    public SaleAdapter(Context context, List<BulletinText> data) {
        super(context, data);
        this.data = data;
    }

    @Override
    public View getView(int position) {
        // 获取 item 根 view
        View view = getRootView(R.layout.item_bulltext);
        // 实例化子 View
        TextView tVSaleTitle = (TextView) view.findViewById(R.id.item_bulltext_title);
        // 获取当前 bean
        BulletinText bulletinText = data.get(position);
        // 设置 view 显示的值
        tVSaleTitle.setText(""+bulletinText.getTitle());
        return view;
    }
}