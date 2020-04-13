package com.example.fleamarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.MyAddressitem;
import com.example.fleamarket.bean.SearchHistory;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private Context context;
    private List<String> data;
    private OnItemClickListener onItemClickListener;

    public SearchHistoryAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.text.setText(data.get(position));
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener_text(position,data.get(position).toString().trim());
                }
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClickListener_del(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView del;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.item_search_history_text);
            del = itemView.findViewById(R.id.item_search_history_del);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener_text(int position, String text);

        public void onItemClickListener_del(int position);

    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void addData(String aaa) {
        //在list中添加数据，并通知条目加入一条
        data.add(data.size(), aaa);
        //添加动画
        notifyItemInserted(data.size());
    }


}
