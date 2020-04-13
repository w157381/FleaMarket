package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.bean.Goods;
import com.example.fleamarket.bean.OrderForm;
import com.example.fleamarket.bean.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MyBuyAdapter extends RecyclerView.Adapter<MyBuyAdapter.ViewHolder> {
    private Context context;
    private List<OrderForm> data;
    private OnItemClickListener onItemClickListener;

    public MyBuyAdapter(Context context, List<OrderForm> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mybuy, parent, false);
        return new MyBuyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final OrderForm orderForm = data.get(position);
        Goods goods = orderForm.getBill_goodsInfo();
        User user = orderForm.getBill_buyUserInfo();
        holder.head.setImageURI(Uri.parse(user.getUser_headImg()));
        holder.nickname.setText(user.getUser_nickName());
        //订单状态 -1:交易失败  0：交易中 1：交易成功
        int a = orderForm.getBill_state();
        if(a==-1){
            holder.state.setText("交易失败");
        }
        if(a==0){
            holder.state.setText("交易中");
        }
        if(a==1){
            holder.state.setText("交易成功");
        }
        holder.img.setImageURI(Uri.parse(goods.getGoods_imgs().get(0)));
        holder.info.setText(""+goods.getGoods_info());
        holder.sumprice.setText("￥"+orderForm.getBill_paynum());


        holder.quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_del(position, orderForm);
                }
            }
        });
        holder.quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_quxiao(position, orderForm);
                }
            }
        });
        holder.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_sure(position, orderForm,holder);
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener_view(position, data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, state, info, sumprice;
        public SimpleDraweeView head,img;
        public Button quxiao, sure,del;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            head = itemView.findViewById(R.id.item_mybuy_user_img);
            nickname = itemView.findViewById(R.id.item_mybuy_nickname);
            state = itemView.findViewById(R.id.item_mybuy_state);
            img = itemView.findViewById(R.id.item_mybuy_img);
            info = itemView.findViewById(R.id.item_mybuy_info);
            sumprice = itemView.findViewById(R.id.item_mybuy_sumprice);
            quxiao = itemView.findViewById(R.id.item_mybuy_bt_cancel);
            sure = itemView.findViewById(R.id.item_mybuy_bt_sure);
            del = itemView.findViewById(R.id.item_mybuy_bt_del);

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener_quxiao(int position, OrderForm orderForm);
        public void onItemClickListener_del(int position, OrderForm orderForm);
        public void onItemClickListener_sure(int position, OrderForm orderForm,ViewHolder viewHolder);
        public void onItemClickListener_view(int position, OrderForm orderForm);

    }

    public void setOnViewClickListener(MyBuyAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (MyBuyAdapter.OnItemClickListener) onItemClickListener;
    }

    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
