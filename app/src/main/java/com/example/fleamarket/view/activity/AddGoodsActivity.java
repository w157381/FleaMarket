package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.OptionsPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.GlideLoader;
import com.example.fleamarket.R;
import com.example.fleamarket.adapter.ImgsBmAdapter;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.MessageContent;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.dialog.CreateUserDialog;
import com.example.fleamarket.utils.CheckNetWorkUtil;
import com.example.fleamarket.utils.GetLocationUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.lcw.library.imagepicker.ImagePicker;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class AddGoodsActivity extends BaseActivity implements View.OnClickListener {
    //取消   确认  商品类型   宝贝位置信息  价格信息
    private TextView tv_quit_bt, tv_sure_bt, tv_type, tv_location, tv_price;
    //商品名字
    private EditText et_name, et_intrtoduce;
    //商品类型选择 价格选择
    private RelativeLayout rela01, rela02;
    //添加图片 宝贝位置 --定位
    private ImageView img_addimg, img_dingwei;
    //图片list
    private RecyclerView list;
    //价格01 - 价格  入手价格  邮费
    private String str_price01, str_price02, str_price03;

    //价格设置--弹窗
    private CreateUserDialog createUserDialog;
    //图片集合
    private ArrayList<String> imgs;
    //图片适配器
    private ImgsBmAdapter imgsBmAdapter;
    private List<MessageContent> lll;
    //等待界面
    private ZLoadingDialog dialog;
    //商品信息
    private Goods goods;
    //请求码
    private int REQUEST_CODE = 14;
    private Message msg1, msg2;
    private Handler mHandler = new Handler() {
        //接收消息
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {
                //取出消息
                LinearLayoutManager layoutManager = new LinearLayoutManager(AddGoodsActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                imgsBmAdapter = new ImgsBmAdapter(AddGoodsActivity.this, imgs);
                imgsBmAdapter.setOnViewClickListener(new ImgsBmAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        imgsBmAdapter.removeData(position);
                    }
                });
                list.setLayoutManager(layoutManager);
                list.setAdapter(imgsBmAdapter);
            }
            if (msg.what == 1002) {
                upBmob(imgs);
            }

        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_addgoodsinfo;
    }

    @Override
    protected void initView() {
        tv_quit_bt = findViewById(R.id.addgoods_back);
        tv_sure_bt = findViewById(R.id.addgoods_sure_bt);
        tv_type = findViewById(R.id.addgoods_type);
        rela01 = findViewById(R.id.addgoods_rela01);
        et_name = findViewById(R.id.addgoods_name);
        et_intrtoduce = findViewById(R.id.addgoods_jieshaoinfo);
        img_addimg = findViewById(R.id.addgoods_addimg_bt);
        list = findViewById(R.id.addgoods_relist);
        tv_location = findViewById(R.id.addgoods_location);
        img_dingwei = findViewById(R.id.addgoods_location_bt);
        tv_price = findViewById(R.id.addgoods_price);
        rela02 = findViewById(R.id.addgoods_rela02);
    }

    @Override
    protected void initData() {
        rela01.setOnClickListener(this);
        rela02.setOnClickListener(this);
        img_addimg.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        img_dingwei.setOnClickListener(this);
        tv_quit_bt.setOnClickListener(this);
        tv_sure_bt.setOnClickListener(this);

        imgs = new ArrayList<>();
        goods = new Goods();
        tv_location.setText("" + GetLocationUtil.getLocation(AddGoodsActivity.this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addgoods_rela01: //选择商品标签类型
                hideSoftInput();
                OptionsPickerView<String> mOptionsPickerView = new OptionsPickerView<>(this);
                mOptionsPickerView.setTitle("商品类型选择");
                final ArrayList<String> list = new ArrayList<>();
                String[] type = new String[]{Constants.GOODS_type01, Constants.GOODS_type02,Constants.GOODS_type03, Constants.GOODS_type04, Constants.GOODS_type05,
                        Constants.GOODS_type06, Constants.GOODS_type07, Constants.GOODS_type08, Constants.GOODS_type09,Constants.GOODS_type10 };
                for (int i = 0; i < type.length; i++) {
                    list.add(type[i]);
                }
                // 设置数据
                mOptionsPickerView.setPicker(list);
                // 设置选项单位
                mOptionsPickerView.setLabels("");
                mOptionsPickerView.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int option1, int option2, int option3) {
                        String type = list.get(option1);
                        tv_type.setText(type);
                    }
                });
                mOptionsPickerView.show();
                break;
            case R.id.addgoods_addimg_bt:  //选择照片
                ImagePicker.getInstance()
                        .setTitle("选择照片")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                        .setImagePaths(imgs)//保存上一次选择图片的状态，如果不需要可以忽略
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(AddGoodsActivity.this, REQUEST_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                break;
            case R.id.addgoods_rela02: //填写价格
                createUserDialog = new CreateUserDialog(this, R.style.Dialog_Common, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        str_price01 = createUserDialog.text_name.getText().toString().trim();
                        str_price02 = createUserDialog.text_mobile.getText().toString().trim();
                        str_price03 = createUserDialog.text_info.getText().toString().trim();
                        if (str_price01.equals("")) {
                            ToastEx.warning(v.getContext(), "价格").show();
                            return;
                        }
                        if (str_price02.equals("")) {
                            ToastEx.warning(v.getContext(), "入手价格").show();
                            return;
                        }
                        if (str_price03.equals("")) {
                            ToastEx.warning(v.getContext(), "运费").show();
                            return;
                        }
                        int a = Integer.parseInt(str_price01);
                        int b = Integer.parseInt(str_price02);
                        int c = Integer.parseInt(str_price03);
                        if (a < 0 || b < 0 || c < 0) {
                            ToastEx.warning(v.getContext(), "信息输入要大于0！").show();
                            return;
                        }
                        if (a > b) {
                            ToastEx.warning(v.getContext(), "标价要小于入手价格！").show();
                            return;
                        }
                        tv_price.setText("价格:" + str_price01 + ",入手价:" + str_price02 + "，邮费:" + str_price03);
                        createUserDialog.cancel();
                    }
                });
                createUserDialog.show();
                break;
            case R.id.addgoods_location:
                CityPickerView mCityPickerView = new CityPickerView(this);
                // 设置点击外部是否消失
                mCityPickerView.setCancelable(true);
                // 设置滚轮字体大小
                mCityPickerView.setTextSize(18f);
                // 设置标题
                mCityPickerView.setTitle("选择宝贝发布位置");
                // 设置取消文字
                mCityPickerView.setCancelText("取消");
                // 设置取消文字颜色
                mCityPickerView.setCancelTextColor(Color.GRAY);
                // 设置取消文字大小
                mCityPickerView.setCancelTextSize(14f);
                // 设置确定文字
                mCityPickerView.setSubmitText("确定");
                // 设置确定文字颜色
                mCityPickerView.setSubmitTextColor(Color.BLACK);
                // 设置确定文字大小
                mCityPickerView.setSubmitTextSize(14f);
                // 设置头部背景
                mCityPickerView.setHeadBackgroundColor(Color.WHITE);
                mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener() {
                    @Override
                    public void onCitySelect(String prov, String city, String area) {
                        // 省、市、区 分开获取
                        Log.e("11111111", "省: " + prov + " 市: " + city + " 区: " + area);
                    }

                    @Override
                    public void onCitySelect(String str) {
                        tv_location.setText(str);
                    }
                });
                mCityPickerView.show();
                break;
            case R.id.addgoods_location_bt:
                tv_location.setText("" + GetLocationUtil.getLocation(AddGoodsActivity.this));
                break;
            case R.id.addgoods_sure_bt:
                if (!CheckNetWorkUtil.netWorkCheck(AddGoodsActivity.this)) {
                    ToastEx.warning(AddGoodsActivity.this, "请检查网络设置").show();
                    return;
                }
                String str_type = tv_type.getText().toString().trim();
                String str_name = et_name.getText().toString().trim();
                String str_introduce = et_intrtoduce.getText().toString().trim();
                String str_location = tv_location.getText().toString().trim();
                int int_price = Integer.parseInt(str_price01);
                int int_inprice = Integer.parseInt(str_price02);
                int int_post = Integer.parseInt(str_price03);
                if (str_type.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品标签！").show();
                    return;
                }
                if (str_introduce.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品描述！").show();
                    return;
                }
                if (imgs.size() == 0) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品照片！").show();
                    return;
                }
                if (str_location.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品位置信息！").show();
                    return;
                }
                if (str_price01.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品价格！").show();
                    return;
                }
                if (str_price02.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品入手价格！").show();
                    return;
                }
                if (str_price03.equals("")) {
                    ToastEx.warning(AddGoodsActivity.this, "请添加商品运费！").show();
                    return;
                }

                goods.setGoods_type(str_type);
                goods.setGoods_name(str_name);
                goods.setGoods_info(str_introduce);
                goods.setGoods_loca(str_location);
                goods.setGoods_price(int_price);
                goods.setGoods_inPrice(int_inprice);
                goods.setGoods_post(int_post);
                goods.setGoods_state(2);
                goods.setGoods_messCon(new ArrayList<MessageContent>());

                selecgUser(BmobIM.getInstance().getCurrentUid());
                break;
            case R.id.addgoods_back:
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            imgs = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            mHandler.sendEmptyMessage(1001);
        }
    }

    public void selecgUser(String account) {
        dialog = LoadingUtil.loading(AddGoodsActivity.this, "数据上传中...");
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        User user = object.get(0);
                        goods.setGoods_user(user);
                        goods.setUserId(user.getUser_stuNumber());
                        goods.setUserId(user.getUser_stuNumber());
                        List<User> list = new ArrayList<>();
                        goods.setGoods_like(list);
                        goods.setGoods_want(list);
                        lll = new ArrayList<>();
                        goods.setGoods_messCon(lll);

                        mHandler.sendEmptyMessage(1002);
                    } else {
                        Log.e("BMOB", "null");
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    public void upBmob(ArrayList<String> imgs) {
        final String[] filePaths = imgs.toArray(new String[0]);

        for (int i = 0; i < filePaths.length; i++) {
            filePaths[i] = imgs.get(i);
        }
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    goods.setGoods_imgs(urls);
                    goods.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null) {
                                dialog.cancel();
                                ToastEx.success(AddGoodsActivity.this, "发布成功！").show();
                                Intent intent = new Intent(AddGoodsActivity.this, MainActivity.class);
                                //将选中的item对应的信息配上键值装入Extra中供传递用
                                intent.putExtra("select", "success");
                                //设置3为结果码
                                setResult(3, intent);
                                finish();//结束当前Activity，返回上一个activity

                            } else {
                                dialog.cancel();
                                ToastEx.error(AddGoodsActivity.this, "发布失败！" + e.getMessage()).show();
                            }
                        }
                    });
                    //do something
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                ToastEx.error(AddGoodsActivity.this, "错误码" + statuscode + ",错误描述：" + errormsg).show();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }
}