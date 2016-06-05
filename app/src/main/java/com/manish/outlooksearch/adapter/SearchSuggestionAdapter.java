package com.manish.outlooksearch.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.manish.outlooksearch.R;
import com.manish.outlooksearch.callbacks.OnNextCallListerner;
import com.manish.outlooksearch.model.PageValue;
import com.manish.outlooksearch.utils.Constants;

import java.util.ArrayList;

/**
 * Created by manishdewan on 04/06/16.
 */

/**
 * Adapter for Search Suggestion RecycleView
 */
public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PageValue> pageValues;
    private OnNextCallListerner onNextCallListerner;
    private int offset;

    public SearchSuggestionAdapter(Context context, ArrayList<PageValue> pageValues) {
        this.context = context;
        this.pageValues = pageValues;
    }

    //set Listener for registering callback for pagination
    public void setOnNextCallListerner(OnNextCallListerner onNextCallListerner) {
        this.onNextCallListerner = onNextCallListerner;
    }

    @Override
    public SearchSuggestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vhItem = null;
        //Image Type card which shows image in each item of recycleview
        if (viewType == Constants.ITEM_TYPE_IMAGE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
            vhItem = new ViewHolder(v, viewType);

        }//Footer Type card which shows progressbar
        else if (viewType == Constants.ITEM_TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_progress_bar_list, parent, false);
            vhItem = new ViewHolder(v, viewType);
        }
        return vhItem;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == pageValues.size() - 1 && (pageValues.size() == offset) && !(pageValues.size() == Constants.MAX_COUNT_RESULT)) {
            return Constants.ITEM_TYPE_FOOTER;
        }
        return Constants.ITEM_TYPE_IMAGE;
    }

    @Override
    public void onBindViewHolder(SearchSuggestionAdapter.ViewHolder holder, int position) {
        //Image Type card which shows image in each item of recycleview
        if (holder.getItemViewType() == Constants.ITEM_TYPE_IMAGE) {
            PageValue pageValue = pageValues.get(position);
            Glide.with(context)
                    .load(pageValue.getThumbnail() == null ? null : pageValue.getThumbnail().getSource())
                    .dontAnimate()
                    .placeholder(R.drawable.wiki_search_icon)
                    .error(R.drawable.wiki_search_icon)
                    .into(holder.imageViewSearchResult);
        }//Progressbar in footer of recycleview when next page is to be loaded sends callback for next api call
        else if (holder.getItemViewType() == Constants.ITEM_TYPE_FOOTER) {
            onNextCallListerner.onNext();
        }
    }

    @Override
    public int getItemCount() {
        return pageValues.size();
    }


    /**
     * @param pageValues updates the list of pages in adapter and notifies the recycleview
     */
    public void updateListView(ArrayList<PageValue> pageValues) {
        if (pageValues != null)
            this.pageValues.addAll(pageValues);
        else {
            this.pageValues.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * @param offset updates the offet count in adapter
     */
    public void updateOffsetCount(int offset) {
        this.offset = offset;
    }

    /**
     * View Holder for RecycleView Adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewSearchResult;

        public ViewHolder(View itemView, int itemType) {
            super(itemView);
            switch (itemType) {
                case Constants.ITEM_TYPE_IMAGE:
                    imageViewSearchResult = (ImageView) itemView.findViewById(R.id.image_search_result);
                    break;
                case Constants.ITEM_TYPE_FOOTER:
                    break;
            }
        }
    }
}
