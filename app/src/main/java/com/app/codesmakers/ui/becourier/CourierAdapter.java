package com.app.codesmakers.ui.becourier;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.carriers.CarrierModel;
import com.app.codesmakers.databinding.LayoutCarrierItemBinding;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CourierAdapter<T> extends RecyclerView.Adapter<CourierAdapter<T>.MyViewHolder<T>> {

    List<CarrierModel> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    CarrierListener carrierListener;

    public CourierAdapter(Context mContext, List<CarrierModel> list, CarrierListener carrierListener) {
        this.mContext = mContext;
        this.mList = list;
        this.carrierListener = carrierListener;
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutCarrierItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_carrier_item, parent, false);
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

        private LayoutCarrierItemBinding binding = null;

        MyViewHolder(final LayoutCarrierItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            this.binding.getRoot().setOnClickListener(view -> {
                carrierListener.onItemClicked(mList.get(getAdapterPosition()));
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));

            String status = mList.get(getAdapterPosition()).getStatues();
            if (status.equalsIgnoreCase(AppConstants.STATUS_ORDER_NEW) || status.equalsIgnoreCase(AppConstants.STATUS_ORDER_OFFERED)) {
                binding.cardview.setVisibility(View.VISIBLE);
            } else {
                binding.cardview.setVisibility(View.GONE);
            }
            String url = mList.get(getAdapterPosition()).getStorePic();
            if (url == null) {
                url = mList.get(getAdapterPosition()).getStorePic();
            }
            Log.e("URL", "LIST " + url);
            Picasso.get().load(url).transform(new CircleTransform()).into(binding.storeImage);

            //ViewColorsUtils.changeTextColorHeading(binding.storeNameTv);
            //ViewColorsUtils.changeTextColorSubHeading(binding.contentTv)
        }

    }

    public void updateAdapter(List<CarrierModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public interface CarrierListener {
        void onItemClicked(CarrierModel carrierModel);
    }
}
