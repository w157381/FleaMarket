package com.example.fleamarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.PhoneDto;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context context;
    private List<PhoneDto> phoneDtos;
    private OnItemClickListener onItemClickListener;

    public ContactAdapter(Context context, List<PhoneDto> phoneDtos) {
        this.context = context;
        this.phoneDtos = phoneDtos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        PhoneDto phoneDto = phoneDtos.get(position);
        holder.name.setText(""+phoneDto.getName());
        holder.phone.setText(""+phoneDto.getTelPhone());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position,phoneDtos.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return phoneDtos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone;

        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           view = itemView;
           name = itemView.findViewById(R.id.item_contact_name);
           phone = itemView.findViewById(R.id.item_contact_phone);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position,PhoneDto phoneDtos);

    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }
}
