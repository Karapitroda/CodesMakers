package com.app.codesmakers.ui.ordersummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.menu.MenuModel;
import com.app.codesmakers.databinding.LayoutConfirmationItemBinding;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ConfirmationAdapter<T> extends RecyclerView.Adapter<ConfirmationAdapter<T>.MyViewHolder<T>> {
    List<MenuModel> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ConfirmationAdapter(Context mContext, List<MenuModel> list) {
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
        LayoutConfirmationItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_confirmation_item, parent, false);
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

        private LayoutConfirmationItemBinding binding = null;

        MyViewHolder(final LayoutConfirmationItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            this.binding.tvAdd.setOnClickListener(view -> {
                MenuModel model = mList.get(getAdapterPosition());
                int qty = model.getQuantity();
                qty++;
                model.setQuantity(qty);
                updateListItem(model);
            });
            this.binding.tvRemove.setOnClickListener(view -> {
                MenuModel model = mList.get(getAdapterPosition());
                int qty = model.getQuantity();
                if (qty > 1)
                    qty--;
                model.setQuantity(qty);
                updateListItem(model);
            });

            this.binding.cancelAction.setOnClickListener(view -> {
                removeAt(getAdapterPosition());

            });
        }

        public void bind() {
            MenuModel model = mList.get(getAdapterPosition());
            model.setPosition(getAdapterPosition());
            binding.setModel(model);

            String url = mList.get(getAdapterPosition()).getPhoto();
            if (!url.isEmpty())
                Picasso.get().load(url).transform(new CircleTransform()).into(binding.storeImage);

            /*ViewColorsUtils.changeTextColorHeading(binding.titleTv);
            ViewColorsUtils.changeTextColorSubHeading(binding.subTitleTv);
            ViewColorsUtils.changeTextColorPrimary(binding.qtyTv);
            ViewColorsUtils.changeTextColorPrimary(binding.tvRemove);
            ViewColorsUtils.changeTextColorPrimary(binding.tvAdd);

            GradientDrawable drawable = (GradientDrawable) binding.linearOptions.getBackground();
            drawable.setStroke(3, Color.parseColor(AppConstants.TEXT_COLOR_PRIMARY));*/
        }
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

    private void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
        if (mList.size() == 0) {
            ((OrderSummaryActivity) mContext).finish();
        }
    }

    private void updateListItem(MenuModel item) {
        int pos = item.getPosition();
        this.mList.set(pos, item);
        notifyItemChanged(pos);
    }

}
