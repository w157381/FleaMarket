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
import com.example.fleamarket.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminUserdapter extends RecyclerView.Adapter<AdminUserdapter.ViewHolder> {
    private Context context;
    private List<User> data;
    private OnItemClickListener onItemClickListener;

    public AdminUserdapter(Context context, List<User> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user, parent, false);
        return new AdminUserdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final User user = data.get(position);
        holder.id.setText(user.getUser_stuNumber());
        holder.name.setText(user.getUser_name());
        holder.depart.setText(user.getUser_department());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, user);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id,name,depart;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.item_admin_user_id);
            name = itemView.findViewById(R.id.item_admin_user_name);
            depart = itemView.findViewById(R.id.item_admin_user_depart);
            view = itemView;

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, User user);

    }

    public void setOnViewClickListener(AdminUserdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminUserdapter.OnItemClickListener) onItemClickListener;
    }

}
