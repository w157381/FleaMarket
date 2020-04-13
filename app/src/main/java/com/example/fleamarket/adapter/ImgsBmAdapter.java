package com.example.fleamarket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fleamarket.R;
import com.example.fleamarket.bean.MessageContent;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ImgsBmAdapter extends RecyclerView.Adapter<ImgsBmAdapter.MyViewHolder> {
    private List<String> data;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public ImgsBmAdapter(Context context, List<String> data){
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public ImgsBmAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_img_bm,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgsBmAdapter.MyViewHolder holder, final int position) {
        String url = data.get(position);
        if(url.substring(0,4).equals("http")){
            holder.img.setImageURI(Uri.parse(url));
        }else{
            holder.img.setImageURI("file://"+data.get(position));
        }
        if(position==0){
            holder.star.setVisibility(View.VISIBLE);
        }else{
            holder.star.setVisibility(View.GONE);
        }
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener{
        public void onItemClickListener(int position);
    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        ImageView del;
        TextView star;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            img = itemView.findViewById(R.id.item_img_img);
            del = itemView.findViewById(R.id.item_img_img_del);
            star = itemView.findViewById(R.id.item_img_img_star);

        }
    }
    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    //  添加数据
    public void addData(String url) {
        //在list中添加数据，并通知条目加入一条
        data.add(data.size(),url);
        //添加动画
        notifyItemInserted(data.size());
    }
}
