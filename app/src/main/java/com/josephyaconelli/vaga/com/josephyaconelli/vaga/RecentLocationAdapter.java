package com.josephyaconelli.vaga.com.josephyaconelli.vaga;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josephyaconelli.vaga.R;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.RecentLocation;

import org.w3c.dom.Text;

/**
 * Created by josep on 3/15/2018.
 */

public class RecentLocationAdapter extends RecyclerView.Adapter<RecentLocationAdapter.RecentLocationAdapterViewHolder> {

    private RecentLocation[] mRecentLocations;

    public RecentLocationAdapter(){

    }

    @Override
    public RecentLocationAdapter.RecentLocationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForListItem = R.layout.recent_location_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);

        return new RecentLocationAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecentLocationAdapter.RecentLocationAdapterViewHolder holder, int position) {
        RecentLocation location = mRecentLocations[position];
        holder.mRecentLocationTextView.setText(location.getLocationName());
        holder.mRecentLocationAddressTextView.setText(location.getLocationAddress());
    }

    @Override
    public int getItemCount() {
        return mRecentLocations == null ? 0 : mRecentLocations.length;
    }

    public void setRecentLocations(RecentLocation[] locations){
        mRecentLocations = locations.clone();
        notifyDataSetChanged();
    }

    public class RecentLocationAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mRecentLocationTextView;
        public final TextView mRecentLocationAddressTextView;


        public RecentLocationAdapterViewHolder(View itemView) {
            super(itemView);
            mRecentLocationTextView = (TextView) itemView.findViewById(R.id.tv_recent_location);
            mRecentLocationAddressTextView = (TextView) itemView.findViewById(R.id.tv_recent_location_address);
        }
    }

}
