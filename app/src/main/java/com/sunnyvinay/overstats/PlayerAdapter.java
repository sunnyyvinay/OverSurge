package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
    private boolean combined;

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
        combined = false;
        Player player = mData.get(position);
        try {
            Picasso.get().load(player.getIconURL()).into(holder.icon);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        // if (!player.getIconURL().isEmpty()) Picasso.get().load(player.getIconURL()).into(holder.icon);
        holder.name.setText(player.getUsername());

        Picasso.get().load(R.drawable.delete_ic).into(holder.playerDelete);

        holder.tankSR.setVisibility(View.VISIBLE);
        holder.tankIcon.setVisibility(View.VISIBLE);
        holder.damageSR.setVisibility(View.VISIBLE);
        holder.damageIcon.setVisibility(View.VISIBLE);
        holder.supportSR.setVisibility(View.VISIBLE);
        holder.supportIcon.setVisibility(View.VISIBLE);

        if (player.getUsername().length() >= 10) {
            holder.name.setTextSize(11);
        } else if (player.getUsername().length() >= 7 && player.getUsername().length() <= 9) {
            holder.name.setTextSize(16);
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
                //holder.tankSR.setText("0000");
                holder.tankSR.setVisibility(View.INVISIBLE);
                holder.tankIcon.setVisibility(View.INVISIBLE);
            } else {
                holder.tankSR.setText(Integer.toString(player.getTank()));
                Picasso.get().load(player.getTankURL()).into(holder.tankIcon);
            }

            if (player.getDamage() == 0) {
                //holder.damageSR.setText("0000");
                holder.damageSR.setVisibility(View.INVISIBLE);
                holder.damageIcon.setVisibility(View.INVISIBLE);
            } else {
                holder.damageSR.setText(Integer.toString(player.getDamage()));
                Picasso.get().load(player.getDamageURL()).into(holder.damageIcon);
            }

            if (player.getSupport() == 0) {
                //holder.supportSR.setText("0000");
                holder.supportSR.setVisibility(View.INVISIBLE);
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Player player = mData.get(getAdapterPosition());
            if (view.equals(playerDelete)) {
                removeAt(getAdapterPosition());
            } else if (view.equals(icon)) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("CONSOLE", player.getConsole());
                intent.putExtra("USERNAME", player.getUsername());
                intent.putExtra("BATTLE_TAG", player.getTag());
                intent.putExtra("Fragment", "");
                context.startActivity(intent);
            } else {
                if (player.getTank() != 0 || player.getDamage() != 0 || player.getSupport() != 0) {
                    if (!combined) {
                        combined = true;
                        int numOfRoles = (player.getTank() == 0 ? 0 : 1) + (player.getDamage() == 0 ? 0 : 1) + (player.getSupport() == 0 ? 0 : 1);
                        int combinedSR = (player.getTank() + player.getDamage() + player.getSupport()) / numOfRoles;

                        tankIcon.setVisibility(View.INVISIBLE);
                        tankSR.setVisibility(View.INVISIBLE);
                        damageSR.setVisibility(View.VISIBLE);
                        damageIcon.setVisibility(View.VISIBLE);
                        supportSR.setVisibility(View.INVISIBLE);
                        supportIcon.setVisibility(View.INVISIBLE);
                        damageSR.setText(Integer.toString(combinedSR));
                        Picasso.get().load(getCompIcon(combinedSR)).into(damageIcon);
                    } else {
                        combined = false;
                        tankIcon.setVisibility(View.VISIBLE);
                        tankSR.setVisibility(View.VISIBLE);
                        damageSR.setVisibility(View.VISIBLE);
                        damageIcon.setVisibility(View.VISIBLE);
                        supportSR.setVisibility(View.VISIBLE);
                        supportIcon.setVisibility(View.VISIBLE);

                        if (player.getTank() == 0) {
                            tankSR.setVisibility(View.INVISIBLE);
                            tankIcon.setVisibility(View.INVISIBLE);
                        } else {
                            tankSR.setText(Integer.toString(player.getTank()));
                            Picasso.get().load(player.getTankURL()).into(tankIcon);
                        }

                        if (player.getDamage() == 0) {
                            damageSR.setVisibility(View.INVISIBLE);
                            damageIcon.setVisibility(View.INVISIBLE);
                        } else {
                            damageSR.setText(Integer.toString(player.getDamage()));
                            Picasso.get().load(player.getDamageURL()).into(damageIcon);
                        }

                        if (player.getSupport() == 0) {
                            supportSR.setVisibility(View.INVISIBLE);
                            supportIcon.setVisibility(View.INVISIBLE);
                        } else {
                            supportSR.setText(Integer.toString(player.getSupport()));
                            Picasso.get().load(player.getSupportURL()).into(supportIcon);
                        }
                    }
                }
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

    public String getCompIcon(int rating) {
        if (rating >= 1 && rating <= 1499) {
            //bronze
            return "https://vignette.wikia.nocookie.net/overwatch/images/8/8f/Competitive_Bronze_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023401";
        } else if (rating >= 1500 && rating <= 1999) {
            //silver
            return "https://vignette.wikia.nocookie.net/overwatch/images/f/fe/Competitive_Silver_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023740";
        } else if (rating >= 2000 && rating <= 2499) {
            //gold
            return "https://vignette.wikia.nocookie.net/overwatch/images/4/44/Competitive_Gold_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023755";
        } else if (rating >= 2500 && rating <= 2999) {
            //plat
            return "https://vignette.wikia.nocookie.net/overwatch/images/e/e4/Competitive_Platinum_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023807";
        } else if (rating >= 3000 && rating <= 3499) {
            //diamond
            return "https://vignette.wikia.nocookie.net/overwatch/images/3/3f/Competitive_Diamond_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023818";
        } else if (rating >= 3500 && rating <= 3999) {
            //masters
            return "https://vignette.wikia.nocookie.net/overwatch/images/5/50/Competitive_Master_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023832";
        } else {
            //gm
            return "https://vignette.wikia.nocookie.net/overwatch/images/c/cc/Competitive_Grandmaster_Icon.png/revision/latest/scale-to-width-down/75?cb=20161122023845";
        }
    }
}
