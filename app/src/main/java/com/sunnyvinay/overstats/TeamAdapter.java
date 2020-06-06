package com.sunnyvinay.overstats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    private ArrayList<String> teamNames;
    private ArrayList<String> teamIcons;
    private LayoutInflater mInflater;
    private final Context context;

    // data is passed into the constructor
    TeamAdapter(Context context, ArrayList<String> teamNames, ArrayList<String> teamIcons) {
        this.mInflater = LayoutInflater.from(context);
        this.teamNames = teamNames;
        this.teamIcons = teamIcons;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.team_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SharedPreferences settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        holder.name.setText(teamNames.get(position));
        Picasso.get().load(teamIcons.get(position)).into(holder.icon);
        if (settings.getBoolean("Theme", true)) {
            holder.name.setTextColor(context.getResources().getColor(R.color.shockWhite));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.defiantBlack));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return 20;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                String teamName = teamNames.get(getAdapterPosition());
                int teamNum = getAdapterPosition();
                Intent intent = new Intent(context, TeamActivity.class);
                intent.putExtra("Team", teamName);
                intent.putExtra("TeamNum", teamNum);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
        }
    }
}
