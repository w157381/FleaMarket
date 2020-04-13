package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {
    private Context context;
    private List<Goods> data;
    private OnItemClickListener onItemClickListener;

    public CollectAdapter(Context context, List<Goods> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collect,parent,false);
        return new CollectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        User user = data.get(position).getGoods_user();
        holder.img.setImageURI(Uri.parse(user.getUser_headImg()));
        holder.nickname.setText(""+user.getUser_nickName());
        holder.time.setText("发布时间 "+data.get(position).getCreatedAt());
        holder.price.setText("￥"+data.get(position).getGoods_price());
        holder.info.setText(""+data.get(position).getGoods_info());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.collectImgsAdapter = new CollectImgsAdapter(context,data.get(position).getGoods_imgs());
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(holder.collectImgsAdapter);

        holder.location.setText("发布于 "+data.get(position).getGoods_loca());
        holder.quxiao.setText("取消收藏");
        holder.quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_quxiao(position,data.get(position));
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_view(position,data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView img;
        public TextView nickname;
        public TextView time;
        public TextView price;
        public RecyclerView recyclerView;
        public TextView info;
        public TextView location;
        public TextView quxiao;
        public CollectImgsAdapter collectImgsAdapter;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_coll_head);
            nickname = itemView.findViewById(R.id.item_coll_name);
            time = itemView.findViewById(R.id.item_coll_time);
            price = itemView.findViewById(R.id.item_coll_price);
            recyclerView = itemView.findViewById(R.id.item_coll_list);
            info = itemView.findViewById(R.id.item_coll_info);
            location = itemView.findViewById(R.id.item_coll_location);
            quxiao = itemView.findViewById(R.id.item_coll_quxiao);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener_quxiao(int position, Goods goods);
        public void onItemClickListener_view(int position, Goods goods);

    }

    public void setOnViewClickListener(CollectAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (CollectAdapter.OnItemClickListener) onItemClickListener;
    }
    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
