package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fleamarket.R;
import com.example.fleamarket.bean.MessageContentItem;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.utils.GetTimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;

public class CommListAdapter extends RecyclerView.Adapter<CommListAdapter.MyViewHolder> {
    private Context context;
    private List<MessageContentItem> data;
    private User c_user;
    private OnItemClickListener onItemClickListener;
    public CommListAdapter(Context context,List<MessageContentItem> data,User c_user){
        this.context = context;
        this.data = data;
        this.c_user =c_user;
    }

    @NonNull
    @Override
    public CommListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment_item,parent,false);
        return new CommListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommListAdapter.MyViewHolder holder, final int position) {
        if (data.size()>0){
            holder.img.setImageURI(Uri.parse(data.get(position).getCurr_user().getUser_headImg()));
            holder.nickname.setText(data.get(position).getCurr_user().getUser_nickName());
            holder.onickname.setText("回复:"+data.get(position).getTo_user().getUser_nickName());
            holder.content.setText(data.get(position).getContent());
            holder.time.setText(GetTimeUtil.getTimeExpend(data.get(position).getTime(),GetTimeUtil.gettime()));
            List<User> users = data.get(position).getLike();
            boolean isexit = false;
            for (int i=0;i<users.size();i++){
                if(users.get(i).getUser_stuNumber().equals(c_user.getUser_stuNumber())){
                    isexit = true;
                    break;
                }else{
                    isexit = false;
                }
            }
            if(isexit){
                holder.zan_bt.setImageResource(R.drawable.goods_zan1);
            }else{
                holder.zan_bt.setImageResource(R.drawable.goods_zan0);
            }

            holder.zan_num.setText(""+data.get(position).getLike().size());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener!=null) {
                        onItemClickListener.onItemClickListener_comm(position,holder,data);
                    }
                 }
              });
            holder.zan_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null) {
                        onItemClickListener.onItemClickListener_zan(position,holder,data);
                    }
                }
            });
            if (data.get(position).isMess_dis()){
                holder.view.setVisibility(View.VISIBLE);
            }else{
                holder.view.setVisibility(View.GONE);
            }

    }else{
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;
        TextView nickname;
        TextView onickname;
        TextView content;
        TextView time;
        ImageView zan_bt;
        TextView zan_num;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_commitem_headimg);
            nickname = itemView.findViewById(R.id.item_commitem_nickname);
            onickname = itemView.findViewById(R.id.item_commitem_onickname);
            content = itemView.findViewById(R.id.item_commitem_content);
            time = itemView.findViewById(R.id.item_commitem_time);
            zan_bt = itemView.findViewById(R.id.item_commitem_zan_bt);
            zan_num = itemView.findViewById(R.id.item_commitem_zan_num);

        }
    }
    public interface OnItemClickListener{
        public void onItemClickListener_comm(int position,MyViewHolder myViewHolder,List<MessageContentItem> data);
        public void onItemClickListener_zan(int position,MyViewHolder myViewHolder,List<MessageContentItem> data);
    }
    public void setOnViewClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    //  删除数据
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //  添加数据
    public void addData(int position,MessageContentItem messageContentItem) {
        //在list中添加数据，并通知条目加入一条
        data.add(position,messageContentItem);
        //添加动画
        notifyItemInserted(position);
    }
}
