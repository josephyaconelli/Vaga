package com.josephyaconelli.vaga.com.josephyaconelli.vaga;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josephyaconelli.vaga.R;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.Directions;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.data.RecentLocation;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Route;
import com.josephyaconelli.vaga.com.josephyaconelli.vaga.utils.Step;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by josep on 3/18/2018.
 */

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.DirectionsAdapterViewHolder> {


    private Step[] mSteps;
    private final double METERS_TO_MILES = 0.000621371;
    DecimalFormat mileFormat = new DecimalFormat("#0.0");

    public DirectionsAdapter(){

    }

    @Override
    public DirectionsAdapter.DirectionsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForListItem = R.layout.direction_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);

        return new DirectionsAdapter.DirectionsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(DirectionsAdapter.DirectionsAdapterViewHolder holder, int position) {
        holder.mDistanceTv.setText(mSteps[position].distance.text);
        holder.mMainDirectionTv.setText(Html.fromHtml(mSteps[position].html_instructions));

        int iconId = android.R.drawable.btn_star;


        switch (mSteps[position].maneuver){
            case TURN_RIGHT:
                iconId = android.R.drawable.arrow_up_float;
                break;
            case TURN_LEFT:
                iconId = android.R.drawable.arrow_down_float;
                break;
            case UNKNOWN:
                iconId = android.R.drawable.btn_star;
                break;
        }

        holder.mDirectionsIconView.setBackgroundResource(iconId);
    }

    @Override
    public int getItemCount() {
        return mSteps == null ? 0 : mSteps.length;
    }

    public void setSteps(Step[] steps){
        mSteps = steps.clone();
        notifyDataSetChanged();
    }

    public class DirectionsAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView mMainDirectionTv;
        public final TextView mSecondaryDirectionTv;
        public final View mDirectionsIconView;
        public final TextView mDistanceTv;


        public DirectionsAdapterViewHolder(View itemView) {
            super(itemView);

            mMainDirectionTv = (TextView) itemView.findViewById(R.id.main_direction_tv);
            mSecondaryDirectionTv = (TextView) itemView.findViewById(R.id.secondary_direction_tv);
            mDirectionsIconView = (View) itemView.findViewById(R.id.direction_icon_view);
            mDistanceTv = (TextView) itemView.findViewById(R.id.distance_tv);

        }
    }


}
