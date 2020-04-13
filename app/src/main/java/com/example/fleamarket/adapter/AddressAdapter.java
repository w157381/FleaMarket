package com.example.fleamarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.MyAddressitem;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context;
    private List<MyAddressitem> data;
    private OnItemClickListener onItemClickListener;

    public AddressAdapter(Context context, List<MyAddressitem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MyAddressitem myAddressitem = data.get(position);
        holder.name.setText("收货人：" + myAddressitem.getAddr_name());
        if (!myAddressitem.getAddr_type().equals("")) {
            holder.type.setText("" + myAddressitem.getAddr_type());
        } else {
            holder.type.setVisibility(View.GONE);
        }

        if (myAddressitem.isIsdefault() == true) {
            holder.defa.setText("默认地址");
        } else {
            holder.defa.setVisibility(View.GONE);
        }
        holder.phone.setText("" + myAddressitem.getAddr_phone());
        holder.addr.setText("收货地址：" + myAddressitem.getAddr_address_a() + " " + myAddressitem.getAddr_address_b());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, myAddressitem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phone;
        TextView addr;
        TextView type;
        TextView defa;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.item_addr_name);
            type = itemView.findViewById(R.id.item_addr_type);
            defa = itemView.findViewById(R.id.item_addr_default);
            phone = itemView.findViewById(R.id.item_addr_phone);
            addr = itemView.findViewById(R.id.item_addr_addr);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, MyAddressitem myAddressitem);

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

    public void addData(MyAddressitem myAddressitem) {
        //在list中添加数据，并通知条目加入一条
        data.add(data.size(), myAddressitem);
        //添加动画
        notifyItemInserted(data.size());
    }

    public void update(int position, MyAddressitem myAddressitem) {
        removeData(position);
        data.add(position, myAddressitem);
        //添加动画
        notifyItemInserted(position);
    }
}
