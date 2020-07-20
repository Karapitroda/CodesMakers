package com.app.codesmakers.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.store.StoreModel;
import com.app.codesmakers.databinding.LayoutStoreItemBinding;
import com.app.codesmakers.utils.customviews.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class StoreAdapter<T> extends RecyclerView.Adapter<StoreAdapter<T>.MyViewHolder<T>> implements Filterable {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private CallbackListener onClickHandler;
    private List<StoreModel> mList;
    private List<StoreModel> mListFiltered;

    public void setList(Context mContext, final List<StoreModel> list, CallbackListener onClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = onClickHandler;
        if (this.mList == null) {
            this.mList = list;
            this.mListFiltered = list;
            notifyItemChanged(0, mListFiltered.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return StoreAdapter.this.mList.size();
                }

                @Override
                public int getNewListSize() {
                    return mList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return StoreAdapter.this.mList.get(oldItemPosition).getName() == mList.get(newItemPosition).getName();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    StoreModel newMovie = StoreAdapter.this.mList.get(oldItemPosition);

                    StoreModel oldMovie = mList.get(newItemPosition);

                    return newMovie.getName() == oldMovie.getName();
                }
            });
            this.mList = list;
            this.mListFiltered = list;
            result.dispatchUpdatesTo(this);
        }
    }

    public StoreAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MyViewHolder<T> myViewHolder;
        LayoutStoreItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_store_item, parent, false);
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

        if (mList != null) {
            return mListFiltered.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mListFiltered = mList;
                } else {
                    List<StoreModel> filteredList = new ArrayList<>();
                    for (StoreModel movie : mList) {
                        if (movie.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie);
                        }
                    }
                    mListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListFiltered = (ArrayList<StoreModel>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder<T> extends RecyclerView.ViewHolder {

        private LayoutStoreItemBinding binding = null;

        MyViewHolder(final LayoutStoreItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.getRoot().setOnClickListener(view -> {
                onClickHandler.onItemClicked(mList.get(getAdapterPosition()));
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));
            String url = mList.get(getAdapterPosition()).getPhoto();
            if (url == null) {
                url = mList.get(getAdapterPosition()).getPhotoCap();
            }
            if (url != null || !url.isEmpty())
                Picasso.get().load(url).placeholder(R.drawable.ic_store_placeholder).transform(new CircleTransform()).into(binding.storeImage);
           /* ViewColorsUtils.changeTextColorHeading(binding.titleTv);
            ViewColorsUtils.changeTextColorSubHeading(binding.locationTv);
            ViewColorsUtils.changeTextColorSubHeading(binding.statusTv);*/
        }
    }

    public void updateAdapter(List<StoreModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public interface CallbackListener {
        void onItemClicked(StoreModel storeModel);
    }
}