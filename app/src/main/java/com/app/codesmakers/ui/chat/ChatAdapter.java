package com.app.codesmakers.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.chat.ChatItem;
import com.app.codesmakers.databinding.LayoutChatItemsBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<ChatItem> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private MessagesListener selectionListener;

    ChatAdapter(Context context, List<ChatItem> list, MessagesListener selectionListener) {
        this.selectionListener = selectionListener;
        this.mContext = context;
        this.mList = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutChatItemsBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_chat_items, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ChatItem chatItem = mList.get(position);
        boolean isMemberMessage = chatItem.getType() == ChatItem.ChatItemType.MESSAGE_IN;
        if (chatItem.getImage().equalsIgnoreCase("uri")) {
            if (isMemberMessage)
                Picasso.get().load(chatItem.getUriImage()).into(holder.binding.imageMember);
            else
                Picasso.get().load(chatItem.getUriImage()).into(holder.binding.imageUser);
        } else {
            Log.e("second", "else ");
            boolean isImage = chatItem.isImage();
            if (isImage) {
                String chatImage = chatItem.getImage();
                if (isMemberMessage)
                    Picasso.get().load(chatImage).into(holder.binding.imageMember);
                else
                    Picasso.get().load(chatImage).into(holder.binding.imageUser);
            } else {
                if (isMemberMessage)
                    holder.binding.messageMessageTextView.setText(chatItem.getMessage());
                else
                    holder.binding.messageMemberTextView.setText(chatItem.getMessage());
            }


        }
        holder.binding.setModel(chatItem);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.messageMemberTextView.setBackgroundTintList(ViewColorsUtils.getColorTint(AppConstants.TAB_NORMAL));
            holder.binding.messageMessageTextView.setBackgroundTintList(ViewColorsUtils.getColorTint(AppConstants.TEXT_COLOR_PRIMARY));
        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final LayoutChatItemsBinding binding;

        public MyViewHolder(final LayoutChatItemsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            this.binding.cardMember.setOnClickListener(view -> {
                selectionListener.onMessageSelected(mList.get(getAdapterPosition()));
            });
            this.binding.cardUser.setOnClickListener(view -> {
                selectionListener.onMessageSelected(mList.get(getAdapterPosition()));
            });

        }
    }

    interface MessagesListener {
        void onMessageSelected(ChatItem chatModel);
    }

}
