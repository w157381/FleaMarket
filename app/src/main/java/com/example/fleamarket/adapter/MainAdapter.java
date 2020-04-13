package com.example.fleamarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.constants.Constants;
import com.example.fleamarket.utils.CheckCreditUtil;
import com.example.fleamarket.utils.GetNetImgUtil;
import com.example.fleamarket.utils.GlideImageLoader;
import com.example.fleamarket.view.activity.AnalyzeActivity;
import com.example.fleamarket.view.activity.AnalyzeWebActivity;
import com.example.fleamarket.view.activity.ClassifyGoodsActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> images;
    private List<String> titles;
    private List<String> urls;
    private OnItemClickListener onItemClickListener;

    //布局类型
    private static final int VIEW_TYPE_ONE = 1;
    private static final int VIEW_TYPE_TWO = 2;
    private static final int VIEW_TYPE_THREE = 3;
    private LayoutInflater inflater;
    private Context context;
    private List<Goods> data;
    private List<Advertising> adv_data;

    public MainAdapter(Context context, List<Goods> data, List<Advertising> adv_data) {
        this.context = context;
        this.data = data;
        this.adv_data = adv_data;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPE_ONE;
        } else if (position == 1) {
            return VIEW_TYPE_TWO;
        } else {
            return VIEW_TYPE_THREE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ONE:
                viewHolder = new ViewHolderOne(inflater.inflate(R.layout.item_frag_main1, parent, false));
                break;
            case VIEW_TYPE_TWO:
                viewHolder = new ViewHolderTwo(inflater.inflate(R.layout.item_frag_main2, parent, false));
                break;
            case VIEW_TYPE_THREE:
                viewHolder = new ViewHolderThree(inflater.inflate(R.layout.item_frag_main3, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        switch (getItemViewType(position)) {
            case VIEW_TYPE_ONE:
                images = new ArrayList<>();
                titles = new ArrayList<>();
                urls = new ArrayList<>();

                for (int i=0;i<adv_data.size();i++){
                    titles.add(adv_data.get(i).getAdv_title());
                    images.add(adv_data.get(i).getAdv_imgurl());
                    urls.add(adv_data.get(i).getAdv_url());
                }
                ((ViewHolderOne) holder).banner_one.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                ((ViewHolderOne) holder).banner_one.setImageLoader(new GlideImageLoader());
                //设置图片集合
                ((ViewHolderOne) holder).banner_one.setImages(images);
                //设置banner动画效果
                ((ViewHolderOne) holder).banner_one.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
                ((ViewHolderOne) holder).banner_one.setBannerTitles(titles);
                //设置自动轮播，默认为true
                ((ViewHolderOne) holder).banner_one.isAutoPlay(true);
                //设置轮播时间
                ((ViewHolderOne) holder).banner_one.setDelayTime(2500);
                //设置指示器位置（当banner模式中有指示器时）
                ((ViewHolderOne) holder).banner_one.setIndicatorGravity(BannerConfig.RIGHT);
                //设置图片集合监听
                ((ViewHolderOne) holder).banner_one.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent = new Intent(context, AnalyzeWebActivity.class);
                        intent.putExtra("url", urls.get(position));
                        context.startActivity(intent);
                    }
                });
                //banner设置方法全部调用完毕时最后调用
                ((ViewHolderOne) holder).banner_one.start();
                break;
            case VIEW_TYPE_TWO:
                ((ViewHolderTwo) holder).imageView01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type01);
                        context.startActivity(intent);

                    }
                });
                ((ViewHolderTwo) holder).imageView02.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type02);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView03.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type03);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView04.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type04);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type05);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView06.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type06);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView07.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type07);
                        context.startActivity(intent);

                    }
                });
                ((ViewHolderTwo) holder).imageView08.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type08);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView09.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type09);
                        context.startActivity(intent);
                    }
                });
                ((ViewHolderTwo) holder).imageView10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ClassifyGoodsActivity.class);
                        intent.putExtra("title", Constants.GOODS_type10);
                        context.startActivity(intent);
                    }
                });
                break;
            case VIEW_TYPE_THREE:
                int aa = position - 2;
                final Goods goods = data.get(aa);

                GetNetImgUtil.roadingNetImg(((ViewHolderThree) holder).goods_img, goods.getGoods_imgs().get(0));
                ((ViewHolderThree) holder).goods_info.setText(goods.getGoods_info() + "");
                ((ViewHolderThree) holder).goods_price.setText(goods.getGoods_price() + "");
                if (data.get(aa).getGoods_like().equals("") || goods.getGoods_like().equals(null)) {
                    ((ViewHolderThree) holder).like_of_people.setText(0 + "人喜欢");
                } else {
                    ((ViewHolderThree) holder).like_of_people.setText(goods.getGoods_like().size() + "人喜欢");
                }
                GetNetImgUtil.roadingNetImg(((ViewHolderThree) holder).user_headImg, goods.getGoods_user().getUser_headImg());

                ((ViewHolderThree) holder).user_nickName.setText(goods.getGoods_user().getUser_nickName());

                CheckCreditUtil.getCreditStr(((ViewHolderThree) holder).user_credit, goods.getGoods_user().getUser_credit());

                ((ViewHolderThree) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClickListener(position, goods);
                        }
                    }
                });

                break;
        }


    }

    @Override
    public int getItemCount() {
        return data.size() + 2;
    }

    /**
     * 第一种布局类型ViewHolder
     */
    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        private Banner banner_one;

        public ViewHolderOne(View itemView) {
            super(itemView);
            banner_one = (Banner) itemView.findViewById(R.id.item_main_banner);
        }
    }

    /**
     * 第二种布局类型ViewHolder
     */
    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        private ImageView imageView01;
        private ImageView imageView02;
        private ImageView imageView03;
        private ImageView imageView04;
        private ImageView imageView05;
        private ImageView imageView06;
        private ImageView imageView07;
        private ImageView imageView08;
        private ImageView imageView09;
        private ImageView imageView10;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            imageView01 = (ImageView) itemView.findViewById(R.id.item_main2_img1);
            imageView02 = (ImageView) itemView.findViewById(R.id.item_main2_img2);
            imageView03 = (ImageView) itemView.findViewById(R.id.item_main2_img3);
            imageView04 = (ImageView) itemView.findViewById(R.id.item_main2_img4);
            imageView05 = (ImageView) itemView.findViewById(R.id.item_main2_img5);
            imageView06 = (ImageView) itemView.findViewById(R.id.item_main2_img6);
            imageView07 = (ImageView) itemView.findViewById(R.id.item_main2_img7);
            imageView08 = (ImageView) itemView.findViewById(R.id.item_main2_img8);
            imageView09 = (ImageView) itemView.findViewById(R.id.item_main2_img9);
            imageView10 = (ImageView) itemView.findViewById(R.id.item_main2_img10);
        }
    }

    /**
     * 第三种布局类型viewholder
     */

    public static class ViewHolderThree extends RecyclerView.ViewHolder {
        //物品照片
        private SimpleDraweeView goods_img;
        //物品信息
        private TextView goods_info;
        //物品价格
        private TextView goods_price;
        //多少人喜欢
        private TextView like_of_people;
        //发布者头像
        private SimpleDraweeView user_headImg;
        //发布者名字
        private TextView user_nickName;
        //发布者信誉度
        private TextView user_credit;

        public ViewHolderThree(View itemView) {
            super(itemView);
            goods_img = itemView.findViewById(R.id.item_main_goodsimg);
            goods_info = itemView.findViewById(R.id.item_main_goodinfo);
            goods_price = itemView.findViewById(R.id.item_main_goodsprice);
            like_of_people = itemView.findViewById(R.id.item_main_toget);
            user_headImg = itemView.findViewById(R.id.item_main_userimg);
            user_nickName = itemView.findViewById(R.id.item_main_usernickname);
            user_credit = itemView.findViewById(R.id.item_main_usercredit);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, Goods goods);
    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }
}