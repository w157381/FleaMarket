package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.view.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zia.toastex.ToastEx;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class BuyGoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back;
    private SimpleDraweeView goods_img;
    private RelativeLayout rela;
    private TextView tv_goods_info, tv_price, tv_name_phone, tv_location, tv_post, tv_sumPrice;
    private Button bt_sumbit;

    private Intent date;
    private Goods goods;
    private User curr_user;
    private String pay_type;

    private MyAddressitem myAddressitem;

    @Override
    protected int initLayout() {
        return R.layout.activity_buygoods;
    }

    @Override
    protected void initView() {

        img_back = findViewById(R.id.orderform_back);
        goods_img = findViewById(R.id.orderform_goods_img);
        tv_goods_info = findViewById(R.id.orderform_goods_info);
        tv_price = findViewById(R.id.orderform_goods_price);
        rela = findViewById(R.id.orderform_rela);
        tv_name_phone = findViewById(R.id.orderform_name_phone);
        tv_location = findViewById(R.id.orderform_location);
        tv_post = findViewById(R.id.orderform_post);
        tv_sumPrice = findViewById(R.id.orderform_sum_price);
        bt_sumbit = findViewById(R.id.orderform_sumbit);


    }

    @Override
    protected void initData() {

        img_back.setOnClickListener(this);
        rela.setOnClickListener(this);
        bt_sumbit.setOnClickListener(this);
        bt_sumbit.setEnabled(false);
        myAddressitem = new MyAddressitem();
        myAddressitem =null;
        goods = new Goods();
        curr_user = new User();
        date = new Intent();
        Intent intent = getIntent();
        goods = (Goods) intent.getSerializableExtra("goods");
        goods_img.setImageURI(Uri.parse(goods.getGoods_imgs().get(0)));
        tv_goods_info.setText(goods.getGoods_info());
        int a = goods.getGoods_price();
        int b = goods.getGoods_post();
        tv_price.setText(a + "元");
        tv_post.setText(b + "元");
        tv_sumPrice.setText((a + b) + "元");
        //
        selectUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderform_back:
                date.putExtra("aa", "no");
                setResult(0, date);
                finish();
                break;
            case R.id.orderform_rela:
                Intent intent1 = new Intent(this, ChooseLocationActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.orderform_sumbit:
                if (myAddressitem==null){
                    ToastEx.warning(BuyGoodsActivity.this, "请选择收货地址！").show();
                    return;
                }
                Intent intentgo = new Intent(BuyGoodsActivity.this, PayActivity.class);
                intentgo.putExtra("money", "" + tv_sumPrice.getText());
                startActivityForResult(intentgo, 2);
                break;
        }

    }

    //查询当前信息
    private void selectUserInfo() {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", BmobIM.getInstance().getCurrentUid());
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    curr_user = object.get(0);
                    bt_sumbit.setEnabled(true);

                } else {
                    ToastEx.warning(BuyGoodsActivity.this, "错误：" + e.getMessage()).show();
                    date.putExtra("aa", "no");
                    setResult(0, date);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == 1) {
            if (data.getStringExtra("aa").equals("ok")) {
                myAddressitem = (MyAddressitem) data.getSerializableExtra("myAddressitem");
                tv_name_phone.setText(myAddressitem.getAddr_name() + "," + myAddressitem.getAddr_phone());
                tv_location.setText(myAddressitem.getAddr_address_a() + " " + myAddressitem.getAddr_address_b());
            }
            if (data.getStringExtra("aa").equals("no")) {

            }
        }
        if (requestCode == 2 || resultCode == 2) {
            if (data.getStringExtra("aa").equals("ok")) {
                pay_type = data.getStringExtra("paytype");
                OrderForm orderForm = new OrderForm();
                orderForm.setBill_buyUserId(curr_user.getUser_stuNumber());
                orderForm.setBill_buyUserInfo(curr_user);
                orderForm.setBill_sellUserId(goods.getGoods_user().getUser_stuNumber());
                orderForm.setBill_sellUserInfo(goods.getGoods_user());
                orderForm.setBill_buyAddr(myAddressitem);
                orderForm.setBill_sellAddr(null);
                orderForm.setBill_goodsInfo(goods);
                orderForm.setBill_LogInfNum("");
                orderForm.setBill_LogInftype("");
                orderForm.setBill_remark("");
                orderForm.setBill_paytype(pay_type);
                orderForm.setBill_paynum(tv_sumPrice.getText()+"");
                orderForm.setBill_payState(true);
                orderForm.setBill_fahuoState(0);
                orderForm.setBill_state(0);
                orderForm.setDis_buy(true);
                orderForm.setDis_sell(true);
                orderForm.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {

                            ToastEx.success(BuyGoodsActivity.this, "付款成功！").show();
                            date.putExtra("aa", "ok");
                            data.putExtra("objectId",s);
                            setResult(0, date);
                            finish();

                        } else {

                        }

                    }
                });
            }
            if (data.getStringExtra("aa").equals("no")) {

            }

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            date.putExtra("aa", "no");
            setResult(0, date);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
