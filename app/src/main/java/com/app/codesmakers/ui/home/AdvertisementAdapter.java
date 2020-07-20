package com.app.codesmakers.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.ads.ADsModel;
import com.app.codesmakers.databinding.LayoutAdvertismentItemBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdvertisementAdapter<T> extends SliderViewAdapter<AdvertisementAdapter<T>.MyViewHolder<T>> {

    List<ADsModel> mList;
    List<T> mLdssdist;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public AdvertisementAdapter(Context mContext, List<ADsModel> list) {
        this.mContext = mContext;
        this.mList = list;
    }



    @Override
    public MyViewHolder<T> onCreateViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutAdvertismentItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_advertisment_item, parent, false);
        myViewHolder = new MyViewHolder<>(binding);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder<T> holder, int position) {
        /*if (response != null) {
            T t = response;
        }*/
        holder.bind(position);
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    class MyViewHolder<T> extends SliderViewAdapter.ViewHolder {

        private LayoutAdvertismentItemBinding binding = null;

        MyViewHolder(final LayoutAdvertismentItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.getRoot().setOnClickListener(view -> {
                //clickHandler.onClickHandler(getAdapterPosition(), response);
            });
        }

        public void bind(int position) {
            binding.setModel(mList.get(position));
            Picasso.get().load(mList.get(position).getPhoto()).into(binding.songImage);
            //ViewColorsUtils.changeTextColorSubHeading(binding.titleTv);
        }

        @SuppressLint("SetTextI18n")
        public void bind(T t) {
            // Picasso.get().load(mList.get(getAdapterPosition())).into(binding.songImage);
        }
    }

    public void updateAdapter(List<ADsModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
