package com.app.codesmakers.ui.orderdetails.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.databinding.LayoutMenuItemBinding;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MenuAdapter<T> extends RecyclerView.Adapter<MenuAdapter<T>.MyViewHolder<T>> {

    List<MenuModel> mList;
    List<T> mLdssdist;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private CallbackListener onClickHandler;

    public MenuAdapter(Context mContext, List<MenuModel> list, CallbackListener onClickHandler) {
        this.mContext = mContext;
        this.mList = list;
        this.onClickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutMenuItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_menu_item, parent, false);
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

        private LayoutMenuItemBinding binding = null;

        MyViewHolder(final LayoutMenuItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            this.binding.fabAdd.setOnClickListener(view -> {
                onClickHandler.onItemClicked(mList.get(getAdapterPosition()), MenuAction.ADD);
            });
            this.binding.storeImage.setOnClickListener(view -> {
                this.binding.fabAdd.performClick();
            });
            this.binding.llMiddle.setOnClickListener(view -> {
                this.binding.fabAdd.performClick();
            });
            this.binding.cancelAction.setOnClickListener(view -> {
                onClickHandler.onItemClicked(mList.get(getAdapterPosition()), MenuAction.CANCEL);
            });
        }

        public void bind() {
            MenuModel model = mList.get(getAdapterPosition());
            model.setPosition(getAdapterPosition());
            binding.setModel(model);

            String url = mList.get(getAdapterPosition()).getPhoto();
            if (!url.isEmpty())
                Picasso.get().load(url).transform(new CircleTransform()).into(binding.storeImage);

            //ViewColorsUtils.changeTextColorHeading(binding.titleTv);
            //ViewColorsUtils.changeTextColorSubHeading(binding.subTitleTv);
            //binding.fabAdd.setBackgroundTintList(ViewColorsUtils.getColorTint(AppConstants.TEXT_COLOR_PRIMARY));
        }
    }

    public void updateAdapter(List<MenuModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }


    public List<MenuModel> getSelectedItems() {
        List<MenuModel> selectedItems = new ArrayList<>();
        for (int i = 0; i < this.mList.size(); i++) {
            MenuModel model = this.mList.get(i);
            if (model.isAdded)
                selectedItems.add(model);
        }
        return selectedItems;
    }


    public void updateListItem(MenuModel item) {
        int pos = item.getPosition();
        this.mList.set(pos, item);
        Log.e("POS", "upda"+pos);
        notifyItemChanged(pos);
    }

    public interface CallbackListener {
        void onItemClicked(MenuModel MenuModel, MenuAction menuAction);
    }

    public enum MenuAction{
        CANCEL,
        ADD
    }
}
