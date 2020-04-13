package com.example.fleamarket.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.constants.Constants;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class AdminBillAdapter extends RecyclerView.Adapter<AdminBillAdapter.ViewHolder> {
    private Context context;
    private List<OrderForm> data;
    private OnItemClickListener onItemClickListener;

    public AdminBillAdapter(Context context, List<OrderForm> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_bill, parent, false);
        return new AdminBillAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final OrderForm orderForm = data.get(position);
        holder.goods_type.setText(orderForm.getBill_goodsInfo().getGoods_type());
        int state = orderForm.getBill_state();
        //-1:交易失败  0：交易中 1：交易成功
        if (state == -1) {
            holder.billstate.setText(Constants.BIll_F1);
        }
        if (state == 0) {
            holder.billstate.setText(Constants.BIll_0);
        }
        if (state == 1) {
            holder.billstate.setText(Constants.BIll_1);
        }
        holder.img.setImageURI(orderForm.getBill_goodsInfo().getGoods_imgs().get(0));
        holder.goods_info.setText(orderForm.getBill_goodsInfo().getGoods_info());
        holder.goods_price.setText("￥" + orderForm.getBill_goodsInfo().getGoods_price());
        holder.buyer.setText(orderForm.getBill_buyUserId() + " " + orderForm.getBill_buyUserInfo().getUser_name());
        holder.seller.setText(orderForm.getBill_sellUserId() + " " + orderForm.getBill_sellUserInfo().getUser_name());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(position, orderForm);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView img;
        TextView goods_type, billstate, goods_info, goods_price, buyer, seller;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goods_type = itemView.findViewById(R.id.item_admin_bill_goods_type);
            billstate = itemView.findViewById(R.id.item_admin_bill_state);
            img = itemView.findViewById(R.id.item_admin_bill_goods_img);
            goods_info = itemView.findViewById(R.id.item_admin_bill_goods_info);
            goods_price = itemView.findViewById(R.id.item_admin_bill_goods_price);
            buyer = itemView.findViewById(R.id.item_admin_bill_buyer);
            seller = itemView.findViewById(R.id.item_admin_bill_seller);
            view = itemView;

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position, OrderForm orderForm);

    }

    public void setOnViewClickListener(AdminBillAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (AdminBillAdapter.OnItemClickListener) onItemClickListener;
    }

    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
