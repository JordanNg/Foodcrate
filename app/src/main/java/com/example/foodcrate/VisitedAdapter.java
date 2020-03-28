package com.example.foodcrate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodcrate.data.YelpItem;

import java.util.ArrayList;
import java.util.List;

public class VisitedAdapter extends RecyclerView.Adapter<VisitedAdapter.visitedViewHolder>{

    private List<YelpItem> mYelpItemList;

    public VisitedAdapter() {
        mYelpItemList = new ArrayList<>();
    }

    public void updateSearchResults(List<YelpItem> searchResultsList) {
        mYelpItemList = searchResultsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public visitedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.visited_item, parent, false);

        visitedViewHolder viewHolder = new visitedViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull visitedViewHolder holder, int position) {
        YelpItem yelpItem = mYelpItemList.get(mYelpItemList.size() - position - 1);
        holder.bind(yelpItem);
    }

    @Override
    public int getItemCount() {
        return mYelpItemList.size();
    }

    class visitedViewHolder extends RecyclerView.ViewHolder {
        private ImageView mYelpItemIV;
        private TextView mYelpItemNameTV;
        private TextView mYelpItemPriceTV;
        private TextView mYelpItemRatingTV;
        private TextView mYelpItemVisitedDateTV;

        public visitedViewHolder(@NonNull View itemView) {
            super(itemView);
            mYelpItemNameTV = itemView.findViewById(R.id.tv_business_name);
            mYelpItemPriceTV = itemView.findViewById(R.id.tv_business_price);
            mYelpItemIV = itemView.findViewById(R.id.iv_visited_image);
            mYelpItemRatingTV = itemView.findViewById(R.id.tv_business_rating);
            mYelpItemVisitedDateTV = itemView.findViewById(R.id.tv_business_date_visited);
        }

        void bind(YelpItem yelpItem) {
            mYelpItemNameTV.setText(yelpItem.name);
            mYelpItemPriceTV.setText(yelpItem.price);
            mYelpItemRatingTV.setText(String.valueOf(yelpItem.rating));
            mYelpItemVisitedDateTV.setText(yelpItem.date);
            /* Insert the business Yelp image into the Image View */
            Glide.with(mYelpItemIV)
                    .load(yelpItem.imageUrl)
                    .apply(new RequestOptions().override(500,500).centerCrop())
                    .into(mYelpItemIV);
        }
    }
}
