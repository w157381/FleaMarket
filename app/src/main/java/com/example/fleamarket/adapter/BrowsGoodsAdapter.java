package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.BrowsGoodsHistory;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class BrowsGoodsAdapter extends RecyclerView.Adapter<BrowsGoodsAdapter.ViewHolder> {
    private Context context;
    private List<BrowsGoodsHistory> data;
    private OnItemClickListener onItemClickListener;

    public BrowsGoodsAdapter(Context context, List<BrowsGoodsHistory> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brows, parent, false);
        return new BrowsGoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final BrowsGoodsHistory browsGoodsHistory = data.get(position);
        final Goods goods = browsGoodsHistory.getGoods_info();
        User user = browsGoodsHistory.getGoodsUser_info();
        holder.time.setText(browsGoodsHistory.getCreatedAt()+"");
        holder.user_head.setImageURI(Uri.parse(user.getUser_headImg()));
        holder.nickname.setText(user.getUser_nickName());
        holder.goods_img.setImageURI(Uri.parse(goods.getGoods_imgs().get(0)));
        holder.info.setText(goods.getGoods_info());
        holder.price.setText("￥："+goods.getGoods_price());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener(position,goods.getObjectId());
                }
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener_del(position,browsGoodsHistory);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView user_head, goods_img;
        ImageView del;
        TextView time, nickname, info, price;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            time = itemView.findViewById(R.id.item_brows_time);
            user_head = itemView.findViewById(R.id.item_brows_user_head);
            nickname = itemView.findViewById(R.id.item_brows_user_nickname);
            goods_img = itemView.findViewById(R.id.item_brows_goods_img);
            info = itemView.findViewById(R.id.item_brows_goods_info);
            price = itemView.findViewById(R.id.item_brows_goods_price);
            del = itemView.findViewById(R.id.item_brows_del);

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, String object);
        public void onItemClickListener_del(int position,BrowsGoodsHistory browsGoodsHistory);

    }

    public void setOnViewClickListener(BrowsGoodsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (BrowsGoodsAdapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    public void removeAllData(){
        data.clear();
        notifyDataSetChanged();
    }
}
