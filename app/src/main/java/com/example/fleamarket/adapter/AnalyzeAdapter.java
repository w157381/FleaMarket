package com.example.fleamarket.adapter;

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
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.ShopList;
import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import java.util.List;

public class AnalyzeAdapter extends RecyclerView.Adapter<AnalyzeAdapter.ViewHolder> {
    private Context context;
    private List<ShopList> data;
    private OnItemClickListener onItemClickListener;

    public AnalyzeAdapter(Context context, List<ShopList> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_analyze, parent, false);
        return new AnalyzeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShopList shopList = data.get(position);
        holder.shopType.setText(shopList.getShopType());
        holder.shopimg.setImageURI(Uri.parse(shopList.getShopImg()));
        holder.shopTitle.setText(shopList.getShopTitle());
        holder.shopPrice.setText(shopList.getShopPrice());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, shopList.getShopAddr());
                }
            }
        });
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, shopList.getShopAddr());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView shopimg;
        TextView shopType, shopTitle, shopPrice;
        Button bt;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            shopType = itemView.findViewById(R.id.iem_analyze_shopType);
            shopimg = itemView.findViewById(R.id.iem_analyze_shopImg);
            shopTitle = itemView.findViewById(R.id.iem_analyze_shopTitle);
            shopPrice = itemView.findViewById(R.id.iem_analyze_shopPrice);
            bt = itemView.findViewById(R.id.iem_analyze_go);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, String addrUrl);

    }

    public void setOnViewClickListener(AnalyzeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AnalyzeAdapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
