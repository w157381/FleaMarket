package com.example.fleamarket.view.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.adapter.CommListAdapter;
import com.example.fleamarket.adapter.CommentAdapter;
import com.example.fleamarket.adapter.ImgsUrlAdapter;
import com.example.fleamarket.bean.BrowsGoodsHistory;
import com.example.fleamarket.bean.Collect;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.MessageContent;
import com.example.fleamarket.bean.MessageContentItem;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.dialog.InputTextMsgDialog;
import com.example.fleamarket.utils.CheckCreditUtil;
import com.example.fleamarket.utils.GetNetImgUtil;
import com.example.fleamarket.utils.GetTimeUtil;
import com.example.fleamarket.utils.IntentUtil;
import com.example.fleamarket.utils.LoadingUtil;
import com.example.fleamarket.view.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;
//import com.mob.MobSDK;
import com.zia.toastex.ToastEx;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
//import cn.sharesdk.onekeyshare.OnekeyShare;

public class GoodsInfoActivity extends BaseActivity implements View.OnClickListener {
    private ImageView img_back, img_share, img_analyze;
    private SimpleDraweeView sim_head;
    private TextView nickname;
    private TextView sex;
    private TextView credit;
    private TextView department;
    private TextView location;
    private TextView price;
    private TextView inprice;
    private TextView post;
    private TextView gooodsInfo;
    private RecyclerView imgs_relist;
    private TextView zan_num;
    private TextView want_num;
    private TextView time;
    private TextView allComm;
    private RecyclerView comm_list;
    private ImageView zan;
    private ImageView comment;
    private ImageView collect;
    private Button bt_go_it, bt_buy;

    //goods objectId
    private String good_id;
    //当前用户id
    private String userid;
    //goods
    private Goods goods;
    private User curr_user;
    private Collect collectinfo;
    private ZLoadingDialog dialog;
    private CommentAdapter commentAdapter;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1001) {
                dialog.cancel(); //等待窗口关闭
                addBrowsInfo(); //添加浏览信息

                if (collectinfo.getGoodsList() != null) {
                    for (int i = 0; i < collectinfo.getGoodsList().size(); i++) {
                        if (collectinfo.getGoodsList().get(i).getObjectId().equals(goods.getObjectId())) {
                            collect.setImageResource(R.drawable.goods_collect1);
                            break;
                        } else {
                            collect.setImageResource(R.drawable.goods_collect0);
                        }
                    }
                } else {
                    collect.setImageResource(R.drawable.goods_collect0);
                }
                if (goods.getGoods_like() != null) {
                    for (int i = 0; i < goods.getGoods_like().size(); i++) {
                        if (goods.getGoods_like().get(i).getUser_stuNumber().equals(curr_user.getUser_stuNumber())) {
                            zan.setImageResource(R.drawable.goods_zan1);
                            break;
                        } else {
                            zan.setImageResource(R.drawable.goods_zan0);
                        }
                    }
                } else {
                    zan.setImageResource(R.drawable.goods_zan0);
                }


                final User user = goods.getGoods_user();
                GetNetImgUtil.roadingNetImg(sim_head, user.getUser_headImg());
                nickname.setText(user.getUser_nickName());
                sex.setText(user.getUser_sex());
                int a = user.getUser_credit();
                CheckCreditUtil.getCreditStr(credit, a);
                department.setText(user.getUser_department());
                location.setText("发布于 " + goods.getGoods_loca());

                price.setText("￥" + goods.getGoods_price());
                inprice.setText("入手价" + goods.getGoods_inPrice() + "");
                inprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                if (goods.getGoods_post() > 0) {
                    post.setText("运费：" + goods.getGoods_post());
                } else {
                    post.setText("包邮");
                }
                gooodsInfo.setText(goods.getGoods_info());
                LinearLayoutManager layoutManager = new LinearLayoutManager(GoodsInfoActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                ImgsUrlAdapter imgsUrlAdapter = new ImgsUrlAdapter(GoodsInfoActivity.this, goods.getGoods_imgs());
                imgs_relist.setLayoutManager(layoutManager);
                imgs_relist.setAdapter(imgsUrlAdapter);
                imgs_relist.setNestedScrollingEnabled(false);
                zan_num.setText(goods.getGoods_like().size() + "个人喜欢");
                want_num.setText(goods.getGoods_want().size() + "个人想要");
                time.setText("发布于" + goods.getCreatedAt().substring(0, 10));
                allComm.setText("全部留言" + "(" + goods.getGoods_messCon().size() + ")");
                allComm.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManagercomm = new LinearLayoutManager(GoodsInfoActivity.this);
                layoutManagercomm.setOrientation(LinearLayoutManager.VERTICAL);
                commentAdapter = new CommentAdapter(GoodsInfoActivity.this, goods.getGoods_messCon(), curr_user);
                comm_list.setLayoutManager(layoutManagercomm);
                comm_list.setAdapter(commentAdapter);
                comm_list.setHasFixedSize(true);
                comm_list.setNestedScrollingEnabled(false);
                commentAdapter.setOnViewClickListener(new CommentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener_comm(final int position, final CommentAdapter.MyViewHolder holder, final List<MessageContent> messageContent) {
                        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(GoodsInfoActivity.this, R.style.dialog_center);
                        inputTextMsgDialog.show();
                        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                            @Override
                            public void onTextSend(String msga) {
                                CommListAdapter commListAdapter = holder.commListAdapter;
                                MessageContentItem messageContentItem = new MessageContentItem();
                                messageContentItem.setCurr_user(curr_user);
                                messageContentItem.setTo_user(messageContent.get(position).getFrom_user());
                                List<User> list = new ArrayList<>();
                                messageContentItem.setLike(list);
                                messageContentItem.setContent(msga);
                                messageContentItem.setMess_dis(true);
                                messageContentItem.setTime(GetTimeUtil.gettime());
                                if (messageContent.get(position).getList() == null) {
                                    commListAdapter.addData(0, messageContentItem);
                                } else {
                                    commListAdapter.addData(messageContent.get(position).getList().size(), messageContentItem);
                                }
                                ToastEx.success(GoodsInfoActivity.this, "评论成功！").show();
                            }
                        });
                    }

                    @Override
                    public void onItemClickListener_zan(int positiona, CommentAdapter.MyViewHolder holder, List<MessageContent> data) {
                        List<User> users = data.get(positiona).getLike();
                        if (users.size() > 0) {
                            //判断点赞是否存在
                            boolean idexit = false;
                            User user1 = new User();
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getUser_stuNumber().equals(curr_user.getUser_stuNumber())) {
                                    idexit = true;
                                    user1 = users.get(i);
                                    Log.e("info", "点赞账号存在！");
                                    break;
                                } else {
                                    Log.e("info", "点赞账号不存在！");
                                    idexit = false;
                                }
                            }
                            if (idexit) {
                                holder.zan_bt.setImageResource(R.drawable.goods_zan0);
                                holder.zan_num.setText(users.size() - 1 + "");
                                users.remove(user1);
                                Log.e("info", "点赞账号存在执行语句。取消点赞！" + data.get(positiona).getLike().size());
                                ToastEx.success(GoodsInfoActivity.this, "取消点赞！").show();

                            } else {
                                holder.zan_bt.setImageResource(R.drawable.goods_zan1);
                                holder.zan_num.setText(users.size() + 1 + "");
                                users.add(curr_user);
                                Log.e("info", "点赞账号不存在执行语句。点赞成功！" + data.get(positiona).getLike().size());
                                ToastEx.success(GoodsInfoActivity.this, "点赞成功！").show();
                            }
                        } else {
                            holder.zan_bt.setImageResource(R.drawable.goods_zan1);
                            holder.zan_num.setText(users.size() + 1 + "");
                            users.add(curr_user);
                            data.get(positiona).setLike(users);
                            ToastEx.success(GoodsInfoActivity.this, "点赞成功！" + data.get(positiona).getLike().size()).show();
                            Log.e("info", "第一条数据");
                        }

                    }
                });

            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_goods_info;
    }

    @Override
    protected void initView() {
        img_back = findViewById(R.id.info_back);
        img_analyze = findViewById(R.id.info_analyze);
        img_share = findViewById(R.id.info_share);
        sim_head = findViewById(R.id.info_userhead);
        nickname = findViewById(R.id.info_nickname);
        sex = findViewById(R.id.info_sex);
        credit = findViewById(R.id.info_credit);
        department = findViewById(R.id.info_department);
        location = findViewById(R.id.info_location);
        price = findViewById(R.id.info_price);
        inprice = findViewById(R.id.info_inprice);
        post = findViewById(R.id.info_post);
        gooodsInfo = findViewById(R.id.info_goodsinfo);
        imgs_relist = findViewById(R.id.info_goodsimgs);
        zan_num = findViewById(R.id.info_zan_num);
        want_num = findViewById(R.id.info_want_num);
        time = findViewById(R.id.info_time);
        allComm = findViewById(R.id.info_text_all_comment);
        zan = findViewById(R.id.info_img_zan);
        comment = findViewById(R.id.info_img_comment);
        collect = findViewById(R.id.info_img_collect);
        bt_go_it = findViewById(R.id.info_go_bt);
        bt_buy = findViewById(R.id.info_buy_bt);
        comm_list = findViewById(R.id.info_commentlist);
    }

    @Override
    protected void initData() {
        //分享
        // MobSDK.submitPolicyGrantResult(true, null);
        //接收上个界面的参数
        Intent intent = getIntent();
        good_id = intent.getStringExtra("goodsobjectId");
        userid = BmobIM.getInstance().getCurrentUid();

        //监听器
        img_back.setOnClickListener(this);
        img_analyze.setOnClickListener(this);
        img_share.setOnClickListener(this);
        zan.setOnClickListener(this);
        comment.setOnClickListener(this);
        collect.setOnClickListener(this);
        bt_go_it.setOnClickListener(this);
        bt_buy.setOnClickListener(this);


        //初始化
        goods = new Goods();
        curr_user = new User();
        collectinfo = new Collect();
        //查询商品信息-->
        selectGoodsInfo(good_id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_back:
                finish();
                break;
            case R.id.info_analyze:
                Intent intenta = new Intent(GoodsInfoActivity.this, AnalyzeActivity.class);
                intenta.putExtra("name", goods.getGoods_name());
                startActivity(intenta);
                break;
            case R.id.info_share:
//                //java
//                OnekeyShare oks = new OnekeyShare();
//                // title标题，微信、QQ和QQ空间等平台使用
//                oks.setTitle(getString(R.string.share));
//                // titleUrl QQ和QQ空间跳转链接
//                oks.setTitleUrl("http://sharesdk.cn");
//                // text是分享文本，所有平台都需要这个字段
//                oks.setText("快来看一下这个宝贝吧！");
//                oks.setImageUrl("http://cloud.taest.club/2020/04/07/2bb2b10e40ad759e809ff96c46744376.jpg");
//
//                // 启动分享GUI
//                oks.show(this);

                break;
            case R.id.info_img_zan:
                boolean isexita = false;
                User user = new User();
                if (goods.getGoods_like() != null) {
                    //遍历
                    for (int i = 0; i < goods.getGoods_like().size(); i++) {
                        if (goods.getGoods_like().get(i).getUser_stuNumber().equals(curr_user.getUser_stuNumber())) {
                            isexita = true;
                            user = goods.getGoods_like().get(i);
                            break;
                        } else {
                            isexita = false;

                        }
                    }
                    if (isexita) {
                        List<User> list = goods.getGoods_like();
                        list.remove(user);
                        goods.setGoods_like(list);
                        zan.setImageResource(R.drawable.goods_zan0);
                    } else {
                        List<User> list = goods.getGoods_like();
                        list.add(curr_user);
                        goods.setGoods_like(list);
                        zan.setImageResource(R.drawable.goods_zan1);
                    }
                } else {
                    List<User> list = new ArrayList<>();
                    list.add(curr_user);
                    goods.setGoods_like(list);
                    zan.setImageResource(R.drawable.goods_zan1);
                }

                break;
            case R.id.info_img_comment:
                InputTextMsgDialog aaaa = new InputTextMsgDialog(GoodsInfoActivity.this, R.style.dialog_center);
                aaaa.show();
                aaaa.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msga) {
                        MessageContent mess1 = new MessageContent();
                        mess1.setFrom_user(curr_user);
                        List<User> list = new ArrayList<>();
                        mess1.setLike(list);
                        mess1.setContent(msga);
                        mess1.setMess_dis(true);
                        mess1.setTime(GetTimeUtil.gettime());
                        List<MessageContentItem> lista = new ArrayList<>();
                        mess1.setList(lista);
                        commentAdapter.addData(goods.getGoods_messCon().size(), mess1);
                        ToastEx.success(GoodsInfoActivity.this, "评论成功！").show();
                        allComm.setText("全部留言" + "(" + goods.getGoods_messCon().size() + ")");
                    }
                });

                break;
            case R.id.info_img_collect:

                boolean isexit = false;
                Goods goodsa = new Goods();
                if (collectinfo.getGoodsList() != null) {
                    for (int i = 0; i < collectinfo.getGoodsList().size(); i++) {
                        if (collectinfo.getGoodsList().get(i).getObjectId().equals(goods.getObjectId())) {
                            goodsa = collectinfo.getGoodsList().get(i);
                            Log.e("isexita", "存在");
                            isexit = true;
                            break;
                        } else {
                            isexit = false;
                            Log.e("isexita", "不存在");
                        }
                    }
                    if (isexit) {
                        List<Goods> ll = collectinfo.getGoodsList();
                        ll.remove(goodsa);
                        collectinfo.setGoodsList(ll);
                        Log.e("isexita", "取消collec2");
                        collect.setImageResource(R.drawable.goods_collect0);
                    } else {
                        List<Goods> ll = collectinfo.getGoodsList();
                        ll.add(goods);
                        collectinfo.setGoodsList(ll);
                        collect.setImageResource(R.drawable.goods_collect1);
                        Log.e("isexita", "取消collec3");
                    }
                } else {
                    List<Goods> ll = new ArrayList<>();
                    ll.add(goods);
                    collectinfo.setGoodsList(ll);
                    collect.setImageResource(R.drawable.goods_collect1);
                    Log.e("isexita", "取消collec4");
                }

                break;
            case R.id.info_go_bt:
                if (goods.getGoods_user().getUser_stuNumber().equals(BmobIM.getInstance().getCurrentUid())) {
                    ToastEx.warning(GoodsInfoActivity.this, "这是自己的商品！").show();
                    return;
                }
                BmobIMUserInfo userInfo = new BmobIMUserInfo();
                User user1 = goods.getGoods_user();
                userInfo.setId(Long.parseLong(user1.getUser_stuNumber()));
                userInfo.setAvatar(user1.getUser_headImg());
                userInfo.setName(user1.getUser_nickName());
                userInfo.setUserId(user1.getUser_stuNumber());
                BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(userInfo, null);
                conversationEntrance.setConversationIcon(user1.getUser_headImg());
                conversationEntrance.setConversationId(user1.getUser_stuNumber());
                conversationEntrance.setConversationTitle(user1.getUser_nickName());
                BmobIM.getInstance().updateConversation(conversationEntrance);
                Intent intent = new Intent(GoodsInfoActivity.this, ChatActivity.class);
                intent.putExtra("conversationId", conversationEntrance.getConversationId());
                startActivity(intent);
                break;
            case R.id.info_buy_bt:
                if (goods.getGoods_user().getUser_stuNumber().equals(BmobIM.getInstance().getCurrentUid())) {
                    ToastEx.warning(GoodsInfoActivity.this, "这是自己的商品！").show();
                    return;
                }
                Intent intent1 = new Intent(this, BuyGoodsActivity.class);
                intent1.putExtra("goods",goods);
                startActivityForResult(intent1, 0);
                break;
        }
    }

    //goods数据查询
    private void selectGoodsInfo(String good_id) {
        dialog = LoadingUtil.loading(GoodsInfoActivity.this, "Loading...");
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addWhereEqualTo("objectId", good_id);
        query.include("goods_user");
        query.findObjects(new FindListener<Goods>() {

            @Override
            public void done(List<Goods> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        goods = object.get(0);
                        selectUserinfo(userid);
                    } else {
                        ToastEx.error(GoodsInfoActivity.this, "商品走丢了。。。" + e.getMessage()).show();
                        finish();
                    }

                } else {
                    ToastEx.error(GoodsInfoActivity.this, "商品走丢了。。。").show();
                    finish();
                }
            }
        });

    }

    //当前用户信息
    private void selectUserinfo(final String account) {
        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("user_stuNumber", account);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        curr_user = object.get(0);
                        selectCollect(account);
                    } else {
                        ToastEx.error(GoodsInfoActivity.this, "失败：" + e.getMessage()).show();
                    }
                } else {
                    ToastEx.error(GoodsInfoActivity.this, "失败：" + e.getMessage()).show();
                }
            }
        });
    }

    //收藏信息
    private void selectCollect(String account) {
        BmobQuery<Collect> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userid", account);
        categoryBmobQuery.findObjects(new FindListener<Collect>() {
            @Override
            public void done(List<Collect> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        collectinfo = object.get(0);
                        mHandle.sendEmptyMessage(1001);
                    } else {
                        ToastEx.error(GoodsInfoActivity.this, "失败：" + e.getMessage()).show();
                    }
                } else {
                    ToastEx.error(GoodsInfoActivity.this, "失败：" + e.getMessage()).show();
                }
            }
        });
    }

    //添加浏览信息
    private void addBrowsInfo() {
        BrowsGoodsHistory browsGoodsHistory = new BrowsGoodsHistory();
        browsGoodsHistory.setUserId(BmobIM.getInstance().getCurrentUid());
        browsGoodsHistory.setGoods_info(goods);
        browsGoodsHistory.setGoodsUser_info(goods.getGoods_user());
        browsGoodsHistory.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        goods.update(goods.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("更新成功:", goods.getUpdatedAt());
                } else {
                    Log.e("更新失败:", e.getMessage());
                }
            }
        });
        collectinfo.update(collectinfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("更新成功:", goods.getUpdatedAt());
                } else {
                    Log.e("更新失败:", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || resultCode == 0) {
            if (data.getStringExtra("aa").equals("no")) {
                ToastEx.warning(GoodsInfoActivity.this,"订单取消！").show();
            }
            if (data.getStringExtra("aa").equals("ok")) {
                final String objectId = data.getStringExtra("objectId");
                goods.setGoods_state(0);
                goods.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Intent intent = new Intent(GoodsInfoActivity.this,BillInfoActivity.class);
                            intent.putExtra("objectId",objectId);
                            startActivity(intent);
                            finish();
                        }else{

                        }
                    }
                });



            }
        }
    }

}
