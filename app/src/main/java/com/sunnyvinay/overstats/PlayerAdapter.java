package com.sunnyvinay.overstats;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private ArrayList<Player> mData;
    private LayoutInflater mInflater;
    //private ViewHolder.ItemClickListener mClickListener;

    // data is passed into the constructor
    public PlayerAdapter(Context context, ArrayList<Player> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.player_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player player = mData.get(position);
        Picasso.get().load(player.getIconURL()).into(holder.icon);
        holder.name.setText(player.getUsername());

        if (player.getTank() == 0) {
            holder.tankSR.setText("0000");
            holder.tankIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.tankSR.setText(Integer.toString(player.getTank()));
            Picasso.get().load(player.getTankURL()).into(holder.tankIcon);
        }

        if (player.getDamage() == 0) {
            holder.damageSR.setText("0000");
            holder.damageIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.damageSR.setText(Integer.toString(player.getDamage()));
            Picasso.get().load(player.getDamageURL()).into(holder.damageIcon);
        }

        if (player.getSupport() == 0) {
            holder.supportSR.setText("0000");
            holder.supportIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.supportSR.setText(Integer.toString(player.getSupport()));
            Picasso.get().load(player.getSupportURL()).into(holder.supportIcon);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder { //implements View.OnClickListener {
        ImageView icon;
        TextView name;
        TextView tankSR;
        TextView damageSR;
        TextView supportSR;
        ImageView tankIcon;
        ImageView damageIcon;
        ImageView supportIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            tankSR = itemView.findViewById(R.id.tankSR);
            damageSR = itemView.findViewById(R.id.damageSR);
            supportSR = itemView.findViewById(R.id.supportSR);
            tankIcon = itemView.findViewById(R.id.tankIcon);
            damageIcon = itemView.findViewById(R.id.damageIcon);
            supportIcon = itemView.findViewById(R.id.supportIcon);
            //itemView.setOnClickListener(this);
        }
        /*
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
         */
    }

    // convenience method for getting data at click position
    Player getItem(int id) {
        return mData.get(id);
    }
    /*
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
     */
}
