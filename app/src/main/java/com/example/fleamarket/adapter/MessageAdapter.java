package com.example.fleamarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.view.activity.ChatActivity;
import com.example.fleamarket.bean.User;

import com.example.fleamarket.utils.GetTimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * Created by 王鹏飞
 * on 2020-02-15
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<BmobIMConversation> data;
    private OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, List<BmobIMConversation> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_frag_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final BmobIMConversation bmobIMConversation = data.get(position);

        List<BmobIMMessage> mess = bmobIMConversation.getMessages();

        holder.userheadimg.setImageURI(Uri.parse(bmobIMConversation.getConversationIcon()));
        holder.usernickname.setText("" +bmobIMConversation.getConversationTitle());
        if (mess.size() > 0) {
            holder.lastmessage.setText("" + mess.get(0).getContent());
            try {
                holder.lastmessagetime.setText("" + GetTimeUtil.longToString(mess.get(0).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.lastmessage.setText("");
        }

        long aaa = BmobIM.getInstance().getUnReadCount(bmobIMConversation.getConversationId());
        if (aaa > 0) {
            holder.unread_num.setText("" + aaa);
        } else {
            holder.unread_num.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(onItemClickListener!=null){
                  onItemClickListener.onItemClickListener(bmobIMConversation);
              }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //用户头像
        private SimpleDraweeView userheadimg;
        //用户昵称
        private TextView usernickname;
        //订单状态
        private TextView billstate;
        //最近一条消息
        private TextView lastmessage;
        //最近一条消息时间
        private TextView lastmessagetime;
        //商品照片
        private TextView unread_num;

        public ViewHolder(View itemView) {
            super(itemView);
            userheadimg = itemView.findViewById(R.id.item_message_userheadimg);
            usernickname = itemView.findViewById(R.id.item_message_usernickname);
            lastmessage = itemView.findViewById(R.id.item_message_message);
            lastmessagetime = itemView.findViewById(R.id.item_message_lastmessagetime);
            unread_num = itemView.findViewById(R.id.item_message_unread_messagenum);

        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(BmobIMConversation bmobIMConversation);

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

    //  添加数据
    public void addData(int position, BmobIMConversation bmobIMConversation) {
        //在list中添加数据，并通知条目加入一条
        data.add(position, bmobIMConversation);
        //添加动画
        notifyItemInserted(position);

    }

}