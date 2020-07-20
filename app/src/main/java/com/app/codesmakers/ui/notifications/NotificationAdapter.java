package com.app.codesmakers.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.notification.NotificationModel;
import com.app.codesmakers.databinding.LayoutNotficationItemBinding;

import java.util.List;


public class NotificationAdapter<T> extends RecyclerView.Adapter<NotificationAdapter<T>.MyViewHolder<T>> {

    List<NotificationModel> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Context mContext, List<NotificationModel> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutNotficationItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_notfication_item, parent, false);
        myViewHolder = new MyViewHolder<>(binding);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder<T> holder, int position) {
        /*if (response != null) {
            T t = response;
        }*/
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder<T> extends RecyclerView.ViewHolder {

        private LayoutNotficationItemBinding binding = null;

        MyViewHolder(final LayoutNotficationItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.getRoot().setOnClickListener(view -> {
                //clickHandler.onClickHandler(getAdapterPosition(), response);
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));
            //ViewColorsUtils.changeTextColorHeading(binding.titleTv);
            //ViewColorsUtils.changeTextColorSubHeading(binding.subTitleTv);
        }

    }

    public void updateAdapter(List<NotificationModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
