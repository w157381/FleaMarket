package com.example.fleamarket.adapter.admin;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.ShopList;
import com.example.fleamarket.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminAddBullAdapter extends RecyclerView.Adapter<AdminAddBullAdapter.ViewHolder> {
    private Context context;
    private List<Goods> data;
    private OnItemClickListener onItemClickListener;

    public AdminAddBullAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_bull_add, parent, false);
        return new AdminAddBullAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Goods goods = data.get(position);
        User user = goods.getGoods_user();
        holder.userhead.setImageURI(user.getUser_headImg());
        holder.username.setText(user.getUser_stuNumber()+"  "+user.getUser_name());
        holder.type.setText(goods.getGoods_type());
        holder.goodsimg.setImageURI(goods.getGoods_imgs().get(0));
        holder.info.setText(goods.getGoods_info());
        holder.price.setText(goods.getGoods_price()+"");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position,goods);
                }
            }
        });
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position,goods);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView userhead,goodsimg;
        TextView username,info,price,type;
        Button bt;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            userhead = itemView.findViewById(R.id.item_admin_bull_add_userhead);
            goodsimg = itemView.findViewById(R.id.item_admin_bull_add_goodsimg);
            username = itemView.findViewById(R.id.item_admin_bull_add_username);
            info = itemView.findViewById(R.id.item_admin_bull_add_goodsinfo);
            type = itemView.findViewById(R.id.item_admin_bull_add_goods_type);
            price = itemView.findViewById(R.id.item_admin_bull_add_goodsprice);
            bt = itemView.findViewById(R.id.item_admin_bull_add_go);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, Goods goods);

    }

    public void setOnViewClickListener(AdminAddBullAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminAddBullAdapter.OnItemClickListener) onItemClickListener;
    }

}
