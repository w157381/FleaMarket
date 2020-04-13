package com.example.fleamarket.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.MessAlert;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {
    private Context context;
    private List<MessAlert> data;
    private OnItemClickListener onItemClickListener;

    public AlertAdapter(Context context, List<MessAlert> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false);
        return new AlertAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.head.setImageURI(data.get(position).getMessageEvent().getFromUserInfo().getAvatar());
        holder.num.setText(""+data.get(position).getNum());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position,data.get(position).getMessageEvent().getConversation().getConversationId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView head;
        TextView num;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            head = itemView.findViewById(R.id.alert_head);
            num = itemView.findViewById(R.id.alert_num);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, String conversationId);

    }

    public void setOnViewClickListener(AlertAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AlertAdapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
