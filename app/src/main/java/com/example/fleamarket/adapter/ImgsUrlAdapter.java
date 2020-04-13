package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fleamarket.R;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

public class ImgsUrlAdapter extends RecyclerView.Adapter<ImgsUrlAdapter.MyViewHolder> {
    private Context context;
    private List<String> data;
    private OnItemClickListener onItemClickListener;
    public ImgsUrlAdapter(Context context,List<String> data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public ImgsUrlAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_img_url,parent,false);
        return new ImgsUrlAdapter.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ImgsUrlAdapter.MyViewHolder holder, final int position) {

        holder.img.setImageURI(Uri.parse(data.get(position)));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null) {
                    onItemClickListener.onItemClickListener(position,data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface OnItemClickListener{
        public void onItemClickListener(int position,String url);
    }
    public void setOnViewClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_imgurl_img);
        }
    }
}
