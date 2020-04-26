package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

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

        if (player.getTank() == 0) {
            holder.tankSR.setText("0000");
            holder.tankIcon.setVisibility(View.INVISIBLE);
        } else {
            Log.i("hi", "tank settings");
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
    Player getItem(int id) {
        return mData.get(id);
    }

    public void saveArrayList(ArrayList<Player> players, String key){
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(players);
        editor.putString(key, json);
        editor.apply();
    }
}
