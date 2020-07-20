package com.app.codesmakers.ui.favplaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.CMApplication;
import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.ResponseBody;
import com.app.codesmakers.api.pojo.config.ConfigurationRequest;
import com.app.codesmakers.api.pojo.favplaces.PlaceModel;
import com.app.codesmakers.databinding.LayoutFavplaceItemBinding;
import com.app.codesmakers.utils.AppConstants;
import com.app.codesmakers.utils.session.SessionManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;


public class FavPlacesAdapter<T> extends RecyclerView.Adapter<FavPlacesAdapter<T>.MyViewHolder<T>> {

    List<PlaceModel> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public FavPlacesAdapter(Context mContext, List<PlaceModel> list) {
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
        LayoutFavplaceItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_favplace_item, parent, false);
        myViewHolder = new MyViewHolder<>(binding);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder<T> holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder<T> extends RecyclerView.ViewHolder {

        private LayoutFavplaceItemBinding binding = null;

        MyViewHolder(final LayoutFavplaceItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.deleteTv.setOnClickListener(view -> {
                updateFav(mList.get(getAdapterPosition()), getAdapterPosition());
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));
            //ViewColorsUtils.changeTextColorHeading(binding.titleLocation);
            //ViewColorsUtils.changeTextColorSubHeading(binding.nameLocation);
        }
    }

    private void updateFav(PlaceModel placeModel, int adapterPosition) {
        if (((FPlacesActivity) mContext).checkConnection()) {
            ((FPlacesActivity) mContext).viewModel.getShowProgress().postValue(true);
            ConfigurationRequest request1 = SessionManager.getInstallationResquest();
            if (request1 == null)
                return;
            Observable<List<ResponseBody>> observable = CMApplication.getInstance().instantiateRetroInterface().unFavPlace(request1.getApiKey(), request1.getAppModelType(), request1.getAppModelVersion(), request1.getDeviceID(), request1.getOSPlayerID(),
                    request1.getUserID(), request1.getDeviceType(), request1.getUserLocation(), AppConstants.STATUS_FAV_INACTIVE, placeModel.getId(), request1.getAppName());
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(result -> result)
                    .subscribe(new BlockingBaseObserver<List<ResponseBody>>() {
                        @Override
                        public void onNext(List<ResponseBody> responseBodies) {
                            ((FPlacesActivity) mContext).viewModel.getShowProgress().postValue(false);
                            removeAt(adapterPosition);
                        }

                        @Override
                        public void onError(Throwable e) {
                            ((FPlacesActivity) mContext).viewModel.getShowProgress().postValue(false);

                        }
                    });
        }
    }

    public void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public void updateAdapter(List<PlaceModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
