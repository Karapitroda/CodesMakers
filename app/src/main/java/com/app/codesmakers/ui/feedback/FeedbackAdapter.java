package com.app.codesmakers.ui.feedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.feedback.FeedbackModel;
import com.app.codesmakers.databinding.LayoutFeedbackItemBinding;
import com.app.codesmakers.utils.Utilities;

import java.util.List;


public class FeedbackAdapter<T> extends RecyclerView.Adapter<FeedbackAdapter<T>.MyViewHolder<T>> {

    List<FeedbackModel> mList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public FeedbackAdapter(Context mContext, List<FeedbackModel> list) {
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
        LayoutFeedbackItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_feedback_item, parent, false);
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

        private LayoutFeedbackItemBinding binding = null;

        MyViewHolder(final LayoutFeedbackItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
            this.binding.getRoot().setOnClickListener(view -> {
                //clickHandler.onClickHandler(getAdapterPosition(), response);
            });
        }

        public void bind() {
            binding.setModel(mList.get(getAdapterPosition()));
            String rate = mList.get(getAdapterPosition()).getRate();
            Float rating = Utilities.getRatingFromString(rate);
            binding.ratingBar.setRating(rating);
           // ViewColorsUtils.changeTextColorHeading(binding.contentTv);
        }
    }

    public void updateAdapter(List<FeedbackModel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
}
