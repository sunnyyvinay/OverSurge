package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<Player> mData;
    private LayoutInflater mInflater;
    private final Context context;
    private SharedPreferences settings;

    // data is passed into the constructor
    public PlayerAdapter(Context context, ArrayList<Player> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.player_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        Player player = mData.get(position);
        Picasso.get().load(player.getIconURL()).into(holder.icon);
        holder.name.setText(player.getUsername());

        Picasso.get().load(R.drawable.delete_ic).into(holder.playerDelete);

        holder.tankSR.setVisibility(View.VISIBLE);
        holder.tankIcon.setVisibility(View.VISIBLE);
        holder.damageSR.setVisibility(View.VISIBLE);
        holder.damageIcon.setVisibility(View.VISIBLE);
        holder.supportSR.setVisibility(View.VISIBLE);
        holder.supportIcon.setVisibility(View.VISIBLE);

        if (player.getUsername().length() >= 10) {
            holder.name.setTextSize(15);
        } else {
            holder.name.setTextSize(18);
        }

        if (player.getTank() == 0 && player.getDamage() == 0 && player.getSupport() == 0) {
            // quick play account
            Picasso.get().load(R.drawable.quickplaybolt).into(holder.tankIcon);
            holder.tankSR.setText(player.getGamesWon() + " games won");

            holder.damageIcon.setVisibility(View.INVISIBLE);
            holder.damageSR.setVisibility(View.GONE);
            holder.supportSR.setVisibility(View.GONE);
            holder.supportIcon.setVisibility(View.GONE);

        } else {
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
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
                saveArrayList(mData, "Players");
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
                saveArrayList(mData, "Players");
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {
        TypedValue a = new TypedValue();
        int color = 0;
        context.getTheme().resolveAttribute(android.R.attr.actionBarItemBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            color = a.data;
        }
        myViewHolder.itemView.setBackgroundColor(color);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView name;
        TextView tankSR;
        TextView damageSR;
        TextView supportSR;
        ImageView tankIcon;
        ImageView damageIcon;
        ImageView supportIcon;
        ImageView playerDelete;

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
            playerDelete = itemView.findViewById(R.id.playerDelete);

            playerDelete.setOnClickListener(this);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.equals(playerDelete)) {
                removeAt(getAdapterPosition());
            } else if (view.equals(icon)) {
                Player player = mData.get(getAdapterPosition());
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("CONSOLE", player.getConsole());
                intent.putExtra("USERNAME", player.getUsername());
                intent.putExtra("BATTLE_TAG", player.getTag());
                intent.putExtra("Fragment", "");
                context.startActivity(intent);
            }
        }
    }

    public void removeAt(int position) {
        mData.remove(position);
        saveArrayList(mData, "Players");
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, mData.size());
    }

    // convenience method for getting data at click position
    Player getItem(int id) { return mData.get(id); }

    public void saveArrayList(ArrayList<Player> players, String key){
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(players);
        editor.putString(key, json);
        editor.apply();
    }
}
