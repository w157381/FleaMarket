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
import com.example.fleamarket.bean.BulletinText;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminBulldapter extends RecyclerView.Adapter<AdminBulldapter.ViewHolder> {
    private Context context;
    private List<BulletinText> data;
    private OnItemClickListener onItemClickListener;

    public AdminBulldapter(Context context, List<BulletinText> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_bull, parent, false);
        return new AdminBulldapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final BulletinText bulletinText = data.get(position);
        holder.img.setImageURI(Uri.parse(bulletinText.getImgUrl()));
        holder.title.setText(bulletinText.getTitle());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_del(position, bulletinText);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, bulletinText);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        TextView title;
        Button del;

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_admin_bull_img);
            title = itemView.findViewById(R.id.item_admin_bull_title);
            del = itemView.findViewById(R.id.item_admin_bull_del);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, BulletinText bulletinText);
        public void onItemClickListener_del(int position, BulletinText bulletinText);

    }

    public void setOnViewClickListener(AdminBulldapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminBulldapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    //添加数据
    public void addData(BulletinText bulletinText) {
        //在list中添加数据，并通知条目加入一条
        data.add(data.size(), bulletinText);
        //添加动画
        notifyItemInserted(data.size());
    }
}
