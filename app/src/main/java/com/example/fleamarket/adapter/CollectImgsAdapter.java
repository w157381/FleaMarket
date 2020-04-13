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

public class CollectImgsAdapter extends RecyclerView.Adapter<CollectImgsAdapter.ViewHolder> {
    private Context context;
    private List<String> data;

    public CollectImgsAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_collect_img,parent,false);
        return new CollectImgsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageURI(Uri.parse(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView img;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_collimg_img);
        }
    }
}
