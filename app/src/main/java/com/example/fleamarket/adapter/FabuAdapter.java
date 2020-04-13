package com.example.fleamarket.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.constants.Constants;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

import cn.bmob.v3.util.V;

public class FabuAdapter extends RecyclerView.Adapter<FabuAdapter.ViewHolder> {

    private Context context;
    private List<Goods> data;
    private OnItemClickListener onItemClickListener;
    public FabuAdapter(Context context,List<Goods> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public FabuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fabu,parent,false);
        return new FabuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FabuAdapter.ViewHolder holder, final int position) {
        holder.img.setImageURI(Uri.parse(data.get(position).getGoods_imgs().get(0)));
        holder.type.setText(""+data.get(position).getGoods_type());
        holder.info.setText("宝贝描述："+data.get(position).getGoods_info());
        holder.price.setText("价位：￥"+data.get(position).getGoods_price());
        holder.time.setText("发布时间："+data.get(position).getCreatedAt());
        int t = data.get(position).getGoods_state();
        if(t==1){//出售中
            holder.state.setText(Constants.GOODS_STATE_1);
            holder.bt_refabu.setVisibility(View.GONE);
            holder.state.setTextColor(holder.state.getResources().getColor(R.color.app_maincolor));
        }
        if(t==0){//已售出
            holder.state.setText(Constants.GOODS_STATE_0);
            holder.state.setTextColor(holder.state.getResources().getColor(R.color.app_gray));
            holder.bt_refabu.setVisibility(View.GONE);
            holder.bt_update.setVisibility(View.GONE);
            holder.bt_xiajia.setVisibility(View.GONE);
        }
        if (t==-1){//已下架
            holder.state.setText(Constants.GOODS_STATE_F1);
            holder.state.setTextColor(holder.state.getResources().getColor(R.color.app_inprice));
            holder.bt_update.setVisibility(View.GONE);
            holder.bt_xiajia.setVisibility(View.GONE);
            holder.bt_refabu.setVisibility(View.VISIBLE);
        }
        if (t==2){//待审核
            holder.state.setText(Constants.GOODS_STATE_2);
            holder.state.setTextColor(holder.state.getResources().getColor(R.color.app_orange));
            holder.bt_update.setVisibility(View.GONE);
            holder.bt_xiajia.setVisibility(View.GONE);
            holder.bt_refabu.setVisibility(View.GONE);
            holder.bt_delete.setVisibility(View.VISIBLE);
        }
        if (t==3){//已驳回
            holder.state.setText(Constants.GOODS_STATE_3);
            holder.state.setTextColor(holder.state.getResources().getColor(R.color.app_red));
            holder.bt_update.setVisibility(View.VISIBLE);
            holder.bt_xiajia.setVisibility(View.GONE);
            holder.bt_refabu.setVisibility(View.GONE);
            holder.bt_delete.setVisibility(View.VISIBLE);
        }

        //item
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener(position,data.get(position));
                }
            }
        });
        //item--重新发布
        holder.bt_refabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener_ref(position,holder,data.get(position));
                }
            }
        });
        //item--下架
        holder.bt_xiajia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener_xia(position,holder,data.get(position));
                }
            }
        });
        //item--编辑
        holder.bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener_upd(position,holder,data.get(position));
                }
            }
        });
        //item--删除
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener_del(position,holder,data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView img;
        public RelativeLayout relativeLayout;
        public TextView price;
        public TextView time;
        public TextView type;
        public TextView info;
        public TextView state;
        public Button bt_xiajia;
        public Button bt_delete;
        public Button bt_update;
        public Button bt_refabu;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view =itemView;
            relativeLayout = itemView.findViewById(R.id.item_fabu_rela);
            img = itemView.findViewById(R.id.item_fabu_img);
            state = itemView.findViewById(R.id.item_fabu_state);
            price = itemView.findViewById(R.id.item_fabu_price);
            time = itemView.findViewById(R.id.item_fabu_time);
            type = itemView.findViewById(R.id.item_fabu_type);
            info = itemView.findViewById(R.id.item_fabu_info);
            bt_xiajia = itemView.findViewById(R.id.item_fabu_bt_xiajia);
            bt_update = itemView.findViewById(R.id.item_fabu_bt_update);
            bt_delete = itemView.findViewById(R.id.item_fabu_bt_delete);
            bt_refabu =  itemView.findViewById(R.id.item_fabu_bt_refabu); }
    }
    public interface OnItemClickListener{
        public void onItemClickListener(int position,Goods goods);
        public void onItemClickListener_ref(int position,ViewHolder holder,Goods goods);
        public void onItemClickListener_xia(int position,ViewHolder holder,Goods goods);
        public void onItemClickListener_upd(int position,ViewHolder holder,Goods goods);
        public void onItemClickListener_del(int position,ViewHolder holder,Goods goods);
    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener= (OnItemClickListener) onItemClickListener;
    }
    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //  添加数据
    public void addData(int position, Goods goods) {
        //在list中添加数据，并通知条目加入一条
        data.add(position,goods);
        //添加动画
        notifyItemInserted(position);
    }
    public void update(){
        notifyDataSetChanged();
    }
}
