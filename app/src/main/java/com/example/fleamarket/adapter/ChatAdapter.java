package com.example.fleamarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleamarket.R;
import com.example.fleamarket.utils.GetTimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private BmobIMConversation bmobIMConversation;
    private Context context;

    private String curr_userId;
    private OnItemClickListener onItemClickListener;

    //布局类型
    private static final int VIEW_TYPE_LEFT_TEXT = 1;
    private static final int VIEW_TYPE_RIGHT_TEXT = 2;
    private static final int VIEW_TYPE_LEFT_IMG = 3;
    private static final int VIEW_TYPE_RIGHT_IMG = 4;
    private static final int VIEW_TYPE_LEFT_VOICE = 5;
    private static final int VIEW_TYPE_RIGHT_VOICE = 6;
    private static final int VIEW_TYPE_LEFT_PLAYER = 7;
    private static final int VIEW_TYPE_RIGHT_PLAYER = 8;
    private LayoutInflater inflater;
    List<BmobIMMessage> messa;

    public ChatAdapter(Context context, BmobIMConversation bmobIMConversation) {
        this.context = context;
        this.bmobIMConversation = bmobIMConversation;
        inflater = LayoutInflater.from(context);
        curr_userId = BmobIM.getInstance().getCurrentUid();
        messa = bmobIMConversation.getMessages();
        Collections.reverse(messa);
    }

    @Override
    public int getItemViewType(int position) {

        BmobIMMessage message = messa.get(position);
        if (message.getFromId().equals(curr_userId)) {
            String type = message.getMsgType();
            if (type.equals("txt")) {
                //发送文本
                return VIEW_TYPE_RIGHT_TEXT;
            } else if (type.equals("image")) {
                //发送图片
                return VIEW_TYPE_RIGHT_IMG;
            } else if (type.equals("")) {
                //发送语音
                return VIEW_TYPE_RIGHT_VOICE;
            } else {
                //发送视频
                return VIEW_TYPE_RIGHT_PLAYER;

            }

        } else {
            String type = message.getMsgType();
            if (type.equals("txt")) {
                //发送文本
                return VIEW_TYPE_LEFT_TEXT;
            } else if (type.equals("image")) {
                //发送图片
                return VIEW_TYPE_LEFT_IMG;
            } else if (type.equals("")) {
                //发送语音
                return VIEW_TYPE_LEFT_VOICE;
            } else {
                //发送视频
                return VIEW_TYPE_LEFT_PLAYER;

            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {

            case VIEW_TYPE_LEFT_TEXT://1
                viewHolder = new ViewHolderOne(inflater.inflate(R.layout.item_chat_01, parent, false));
                break;
            case VIEW_TYPE_RIGHT_TEXT://2
                viewHolder = new ViewHolderTwo(inflater.inflate(R.layout.item_chat_02, parent, false));
                break;
            case VIEW_TYPE_LEFT_IMG://3
                viewHolder = new ViewHolderThree(inflater.inflate(R.layout.item_chat_03, parent, false));
                break;
            case VIEW_TYPE_RIGHT_IMG://4
                viewHolder = new ViewHolderFour(inflater.inflate(R.layout.item_chat_04, parent, false));
                break;
            case VIEW_TYPE_LEFT_VOICE://5
                viewHolder = new ViewHolderFive(inflater.inflate(R.layout.item_chat_05, parent, false));
                break;
            case VIEW_TYPE_RIGHT_VOICE://6
                viewHolder = new ViewHolderSix(inflater.inflate(R.layout.item_chat_06, parent, false));
                break;
            case VIEW_TYPE_LEFT_PLAYER://7
                viewHolder = new ViewHolderSeven(inflater.inflate(R.layout.item_chat_07, parent, false));
                break;
            case VIEW_TYPE_RIGHT_PLAYER://8
                viewHolder = new ViewHolderEight(inflater.inflate(R.layout.item_chat_08, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final BmobIMMessage message = bmobIMConversation.getMessages().get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_LEFT_TEXT://1
                try {
                    ((ViewHolderOne) holder).time.setText("" + GetTimeUtil.longToString(message.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((ViewHolderOne) holder).head.setImageURI(Uri.parse(message.getBmobIMUserInfo().getAvatar()));
                ((ViewHolderOne) holder).messtext.setText("" + message.getContent());
                ((ViewHolderOne) holder).messtext.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        return false;
                    }
                });
                break;
            case VIEW_TYPE_RIGHT_TEXT://2
                Log.e("1212","getReceiveStatus："+message.getReceiveStatus());
                Log.e("1212","getSendStatus："+message.getSendStatus());
                try {
                    ((ViewHolderTwo) holder).time.setText("" + GetTimeUtil.longToString(message.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((ViewHolderTwo) holder).head.setImageURI(Uri.parse(BmobIM.getInstance().getUserInfo(curr_userId).getAvatar()));
                ((ViewHolderTwo) holder).messtext.setText("" + message.getContent());
                ((ViewHolderTwo) holder).messtext.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
                break;
            case VIEW_TYPE_LEFT_IMG://3
                try {
                    ((ViewHolderThree) holder).time.setText("" + GetTimeUtil.longToString(message.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((ViewHolderThree) holder).head.setImageURI(Uri.parse(BmobIM.getInstance().getUserInfo(message.getFromId()).getAvatar()));
                ((ViewHolderThree) holder).img.setImageURI(message.getContent());

                break;
            case VIEW_TYPE_RIGHT_IMG://4
                try {
                    ((ViewHolderFour) holder).time.setText("" + GetTimeUtil.longToString(message.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ((ViewHolderFour) holder).head.setImageURI(Uri.parse(BmobIM.getInstance().getUserInfo(message.getFromId()).getAvatar()));
                ((ViewHolderFour) holder).img.setImageURI(message.getContent());

                break;
            case VIEW_TYPE_LEFT_VOICE://5

                break;
            case VIEW_TYPE_RIGHT_VOICE://6

                break;
            case VIEW_TYPE_LEFT_PLAYER://7

                break;
            case VIEW_TYPE_RIGHT_PLAYER://8

                break;
        }
    }

    @Override
    public int getItemCount() {
        return bmobIMConversation.getMessages().size();
    }

    /**
     * 第一种布局类型ViewHolder
     */
    public static class ViewHolderOne extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderOne(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_01_time);
            head = itemView.findViewById(R.id.item_chat_01_head);
            messtext = itemView.findViewById(R.id.item_chat_01_messtext);
        }
    }

    /**
     * 第二种布局类型ViewHolder
     */
    public static class ViewHolderTwo extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_02_time);
            head = itemView.findViewById(R.id.item_chat_02_head);
            messtext = itemView.findViewById(R.id.item_chat_02_messtext);
        }
    }
    /**
     * 第三种布局类型ViewHolder
     */
    public static class ViewHolderThree extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private SimpleDraweeView img;

        public ViewHolderThree(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_03_time);
            head = itemView.findViewById(R.id.item_chat_03_head);
            img = itemView.findViewById(R.id.item_chat_03_mess_img);
        }
    }
    /**
     * 第四种布局类型ViewHolder
     */
    public static class ViewHolderFour extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private SimpleDraweeView img;

        public ViewHolderFour(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_04_time);
            head = itemView.findViewById(R.id.item_chat_04_head);
            img = itemView.findViewById(R.id.item_chat_04_mess_img);
        }
    }
    /**
     * 第五种布局类型ViewHolder
     */
    public static class ViewHolderFive extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderFive(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_02_time);
            head = itemView.findViewById(R.id.item_chat_02_head);
            messtext = itemView.findViewById(R.id.item_chat_02_messtext);
        }
    }
    /**
     * 第六种布局类型ViewHolder
     */
    public static class ViewHolderSix extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderSix(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_02_time);
            head = itemView.findViewById(R.id.item_chat_02_head);
            messtext = itemView.findViewById(R.id.item_chat_02_messtext);
        }
    }
    /**
     * 第七种布局类型ViewHolder
     */
    public static class ViewHolderSeven extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderSeven(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_02_time);
            head = itemView.findViewById(R.id.item_chat_02_head);
            messtext = itemView.findViewById(R.id.item_chat_02_messtext);
        }
    }
    /**
     * 第八种布局类型ViewHolder
     */
    public static class ViewHolderEight extends RecyclerView.ViewHolder {
        private TextView time;
        private SimpleDraweeView head;
        private TextView messtext;

        public ViewHolderEight(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.item_chat_02_time);
            head = itemView.findViewById(R.id.item_chat_02_head);
            messtext = itemView.findViewById(R.id.item_chat_02_messtext);
        }
    }

    public interface OnItemClickListener {
        public void onItemClickListener(int position);
    }

    public void setOnViewClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = (OnItemClickListener) onItemClickListener;
    }

    /**
     * 添加一条数据并刷新界面
     *
     * @param message
     */
    public void addData(BmobIMMessage message) {
        bmobIMConversation.getMessages().add(bmobIMConversation.getMessages().size(), message);
        notifyItemInserted(bmobIMConversation.getMessages().size());
    }
}
