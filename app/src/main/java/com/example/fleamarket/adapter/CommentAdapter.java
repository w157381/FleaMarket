package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fleamarket.R;
import com.example.fleamarket.bean.MessageContent;
import com.example.fleamarket.bean.MessageContentItem;
import com.example.fleamarket.bean.User;
import com.example.fleamarket.dialog.InputTextMsgDialog;
import com.example.fleamarket.utils.GetTimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zia.toastex.ToastEx;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<MessageContent> data;
    private User c_user;
    private OnItemClickListener onItemClickListener;
    private boolean check;
    public CommentAdapter(Context context, List<MessageContent> data, User c_user){
        this.context = context;
        this.data = data;
        this.c_user =c_user;
    }
    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.MyViewHolder holder, final int position) {
        if (data.size()>0) {
            holder.img.setImageURI(Uri.parse(data.get(position).getFrom_user().getUser_headImg()));
            holder.nickname.setText(data.get(position).getFrom_user().getUser_nickName());
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
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener_comm(position, holder, data);
                    }
                }
            });
            holder.zan_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener_zan(position, holder, data);
                    }
                }
            });
            LinearLayoutManager layoutManagercomm = new LinearLayoutManager(context);
            layoutManagercomm.setOrientation(LinearLayoutManager.VERTICAL);
            holder.commListAdapter = new CommListAdapter(context, data.get(position).getList(),c_user);
            holder.list.setLayoutManager(layoutManagercomm);
            holder.list.setAdapter(holder.commListAdapter);
            holder.list.setNestedScrollingEnabled(false);
            holder.commListAdapter.setOnViewClickListener(new CommListAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener_comm(final int positions, CommListAdapter.MyViewHolder myViewHolder, final List<MessageContentItem> data) {

                    InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                    inputTextMsgDialog.show();
                    inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                        @Override
                        public void onTextSend(String msga) {
                            MessageContentItem messageContentItem = new MessageContentItem();
                            messageContentItem.setCurr_user(c_user);
                            messageContentItem.setTo_user(data.get(positions).getCurr_user());
                            List<User> list = new ArrayList<>();
                            messageContentItem.setLike(list);
                            messageContentItem.setContent(msga);
                            messageContentItem.setMess_dis(true);
                            messageContentItem.setTime(GetTimeUtil.gettime());
                            holder.commListAdapter.addData(data.size(), messageContentItem);
                        }
                    });
                }

                @Override
                public void onItemClickListener_zan(int positiona, CommListAdapter.MyViewHolder holder, List<MessageContentItem> data) {
                    List<User> users = data.get(positiona).getLike();
                    if(users.size()>0){
                        //判断点赞是否存在
                        boolean idexit = false;
                        User user1 = new User();
                        for (int i=0;i<users.size();i++){
                            if (users.get(i).getUser_stuNumber().equals(c_user.getUser_stuNumber())){
                                idexit = true;
                                user1 = users.get(i);
                                Log.e("info","点赞账号存在！");
                                break;
                            }else {
                                Log.e("info","点赞账号不存在！");
                                idexit = false;
                            }
                        }
                        if(idexit){
                            holder.zan_bt.setImageResource(R.drawable.goods_zan0);
                            holder.zan_num.setText(users.size()-1+"");
                            users.remove(user1);
                            Log.e("info","点赞账号存在执行语句。取消点赞！"+data.get(positiona).getLike().size());
                            ToastEx.success(context,"取消点赞！").show();

                            return;

                        }else {
                            holder.zan_bt.setImageResource(R.drawable.goods_zan1);
                            holder.zan_num.setText(users.size()+1+"");
                            users.add(c_user);
                            Log.e("info","点赞账号不存在执行语句。点赞成功！"+data.get(positiona).getLike().size());
                            ToastEx.success(context,"点赞成功！").show();
                        }



                    }else{
                        holder.zan_bt.setImageResource(R.drawable.goods_zan1);
                        holder.zan_num.setText(users.size()+1+"");
                        users.add(c_user);
                        data.get(positiona).setLike(users);
                        ToastEx.success(context,"点赞成功！"+data.get(positiona).getLike().size()).show();
                        Log.e("info","第一条数据");
                    }

                }
            });
        }else {
            holder.view.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView img;
        public TextView nickname;
        public TextView content;
        public TextView time;
        public ImageView zan_bt;
        public TextView zan_num;
        public RecyclerView list;
        public View view;
        public CommListAdapter commListAdapter;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            img = itemView.findViewById(R.id.item_comm_headimg);
            nickname = itemView.findViewById(R.id.item_comm_nickname);
            content = itemView.findViewById(R.id.item_comm_content);
            time = itemView.findViewById(R.id.item_comm_time);
            zan_bt = itemView.findViewById(R.id.item_comm_zan_bt);
            zan_num = itemView.findViewById(R.id.item_comm_zan_num);
            list = itemView.findViewById(R.id.item_comm_list);

        }
    }

    public interface OnItemClickListener{
        public void onItemClickListener_comm(int position,CommentAdapter.MyViewHolder holder, List<MessageContent> messageContent);
        public void onItemClickListener_zan(int position,CommentAdapter.MyViewHolder holder, List<MessageContent> messageContent);
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
    public void addData(int position,MessageContent messageContent) {
        //在list中添加数据，并通知条目加入一条
        data.add(position,messageContent);
        //添加动画
        notifyItemInserted(position);
    }
}