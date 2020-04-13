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

public class ChooseLocaAdapter extends RecyclerView.Adapter<ChooseLocaAdapter.ViewHolder> {
    private Context context;
    private List<MyAddressitem> data;
    private OnItemClickListener onItemClickListener;

    public ChooseLocaAdapter(Context context, List<MyAddressitem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MyAddressitem myAddressitem = data.get(position);
        holder.name_phone.setText( myAddressitem.getAddr_name()+" "+myAddressitem.getAddr_phone() );
        holder.location.setText(myAddressitem.getAddr_address_a() +myAddressitem.getAddr_address_b());
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
        TextView name_phone,location;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name_phone = itemView.findViewById(R.id.item_chooseloca_namephone);
            location = itemView.findViewById(R.id.item_chooseloca_location);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, MyAddressitem myAddressitem);

    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }


}
