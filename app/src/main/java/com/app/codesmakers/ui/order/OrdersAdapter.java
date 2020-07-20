package com.app.codesmakers.ui.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.myorders.OrderModel;
import com.app.codesmakers.databinding.LayoutOrdersItemBinding;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OrdersAdapter<T> extends RecyclerView.Adapter<OrdersAdapter<T>.MyViewHolder<T>> {

    List<OrderModel> mList;
    List<T> mLdssdist;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private OrdersListener ordersListener;

    public OrdersAdapter(Context mContext, List<OrderModel> list, OrdersListener ordersListener) {
        this.mContext = mContext;
        this.mList = list;
        this.ordersListener = ordersListener;
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutOrdersItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_orders_item, parent, false);
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

        private LayoutOrdersItemBinding binding = null;

        MyViewHolder(final LayoutOrdersItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.getRoot().setOnClickListener(view -> {
                if(ordersListener != null)
                    ordersListener.onClick(mList.get(getAdapterPosition()));
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));
            String url = mList.get(getAdapterPosition()).getIcon();
            if (url == null) {
                url = mList.get(getAdapterPosition()).getIcon();
            }
            Log.e("URL", "LIST " + url);
            Picasso.get().load(url).transform(new CircleTransform()).into(binding.storeImage);

            //ViewColorsUtils.changeTextColorHeading(binding.titleTv);
            //ViewColorsUtils.changeTextColorSubHeading(binding.subTitleTv);
        }

    }

    public void updateAdapter(List<OrderModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
