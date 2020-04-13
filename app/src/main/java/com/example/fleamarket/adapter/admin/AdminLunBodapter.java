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
import com.example.fleamarket.bean.Advertising;
import com.example.fleamarket.bean.User;

import java.util.List;

public class AdminLunBodapter extends RecyclerView.Adapter<AdminLunBodapter.ViewHolder> {
    private Context context;
    private List<Advertising> data;
    private OnItemClickListener onItemClickListener;

    public AdminLunBodapter(Context context, List<Advertising> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_lunbo, parent, false);
        return new AdminLunBodapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Advertising advertising = data.get(position);
        holder.title.setText((position+1)+"、 "+advertising.getAdv_title());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_del(position,advertising);
                }
            }
        });
        holder.upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_upd(position,advertising);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, advertising);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        Button del,upd;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.item_admin_lunbo_title);
            del = itemView.findViewById(R.id.item_admin_lunbo_del);
            upd = itemView.findViewById(R.id.item_admin_lunbo_upd);

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, Advertising advertising);
        public void onItemClickListener_del(int position, Advertising advertising);
        public void onItemClickListener_upd(int position, Advertising advertising);

    }

    public void setOnViewClickListener(AdminLunBodapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminLunBodapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    public void addData(Advertising advertising) {
        //在list中添加数据，并通知条目加入一条
        data.add(data.size(), advertising);
        //添加动画
        notifyItemInserted(data.size());
    }
    public void updData(int position,Advertising advertising){
        data.remove(position);
        notifyItemRemoved(position);
        data.add(position, advertising);
        //添加动画
        notifyItemInserted(position);
    }

}
