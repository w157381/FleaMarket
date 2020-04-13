package com.example.fleamarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.utils.CheckCreditUtil;
import com.example.fleamarket.utils.GetNetImgUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private Context context;
    private List<Goods> data;
    private OnItemClickListener onItemClickListener;

    public GoodsAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_frag_main3, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Goods goods = data.get(position);
        GetNetImgUtil.roadingNetImg(holder.goods_img, goods.getGoods_imgs().get(0));
        holder.goods_info.setText(goods.getGoods_info());
        holder.goods_price.setText(goods.getGoods_price() + "");
        if (goods.getGoods_like().equals("") || goods.getGoods_like().equals(null)) {
            holder.like_of_people.setText(0 + "人喜欢");
        } else {
            holder.like_of_people.setText(goods.getGoods_like().size() + "人喜欢");
        }
        GetNetImgUtil.roadingNetImg(holder.user_headImg, goods.getGoods_user().getUser_headImg());

        holder.user_nickName.setText(goods.getGoods_user().getUser_nickName());

        CheckCreditUtil.getCreditStr(holder.user_credit, goods.getGoods_user().getUser_credit());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, goods);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
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
