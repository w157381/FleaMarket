package com.example.fleamarket.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.constants.Constants;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminGoodsAdapter extends RecyclerView.Adapter<AdminGoodsAdapter.ViewHolder> {
    private Context context;
    private List<Goods> data;
    private OnItemClickListener onItemClickListener;

    public AdminGoodsAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_goods, parent, false);
        return new AdminGoodsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //商品状态 -2:删除 -1:下架，0：售出，1：出售中  2:待审核 ,3 :已驳回
        final Goods goods = data.get(position);
        User user = goods.getGoods_user();
        holder.userhead.setImageURI(user.getUser_headImg());
        holder.username.setText(user.getUser_stuNumber()+"  "+user.getUser_name());
        int state = goods.getGoods_state();

        if(state==-2){//删除
            holder.state.setText(Constants.GOODS_STATE_F2);
            holder.state.setTextColor(context.getResources().getColor(R.color.app_gray80));
            holder.bt_goPass.setVisibility(View.GONE);
            holder.bt_del.setVisibility(View.VISIBLE);

        }
        if(state==-1){//下架
            holder.state.setText(Constants.GOODS_STATE_F1);
            holder.state.setTextColor(context.getResources().getColor(R.color.app_gray80));
            holder.bt_goPass.setVisibility(View.GONE);
            holder.bt_del.setVisibility(View.GONE);

        }
        if(state==0){//售出
            holder.state.setText(Constants.GOODS_STATE_0);
            holder.state.setTextColor(context.getResources().getColor(R.color.app_black));
            holder.bt_goPass.setVisibility(View.GONE);
            holder.bt_del.setVisibility(View.GONE);

        }
        if(state==1){//出售中
            holder.state.setText(Constants.GOODS_STATE_1);
            holder.bt_goPass.setVisibility(View.GONE);
            holder.bt_del.setVisibility(View.GONE);
        }
        if(state==2){//待审核
            holder.state.setText(Constants.GOODS_STATE_2);
            holder.state.setTextColor(context.getResources().getColor(R.color.app_orange));
            holder.bt_del.setVisibility(View.GONE);
        }
        if(state==3){//已驳回
            holder.state.setText(Constants.GOODS_STATE_3);
            holder.state.setTextColor(context.getResources().getColor(R.color.app_red));
            holder.bt_goPass.setVisibility(View.GONE);
            holder.bt_del.setVisibility(View.GONE);

        }
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
        holder.bt_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_del(position,goods);
                }
            }
        });
        holder.bt_goPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_gopass(position,goods);
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
        TextView username,state,info,price;
        Button bt_goPass,bt_del;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            userhead = itemView.findViewById(R.id.item_admin_goods_userhead);
            goodsimg = itemView.findViewById(R.id.item_admin_goods_img);
            username = itemView.findViewById(R.id.item_admin_goods_username);
            info = itemView.findViewById(R.id.item_admin_goods_info);
            state = itemView.findViewById(R.id.item_admin_goods_state);
            price = itemView.findViewById(R.id.item_admin_goods_price);
            bt_goPass = itemView.findViewById(R.id.item_admin_goods_bt_gopass);
            bt_del = itemView.findViewById(R.id.item_admin_goods_bt_del);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, Goods goods);
        public void onItemClickListener_gopass(int position, Goods goods);
        public void onItemClickListener_del(int position, Goods goods);

    }

    public void setOnViewClickListener(AdminGoodsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminGoodsAdapter.OnItemClickListener) onItemClickListener;
    }
    public void updData(int position,Goods goods){
        data.remove(position);
        notifyItemRemoved(position);
        data.add(position, goods);
        //添加动画
        notifyItemInserted(position);
    }
    public void removeData(int position){
        data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
