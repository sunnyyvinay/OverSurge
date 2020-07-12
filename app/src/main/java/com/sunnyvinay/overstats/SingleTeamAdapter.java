package com.sunnyvinay.overstats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SingleTeamAdapter extends RecyclerView.Adapter<SingleTeamAdapter.ViewHolder> {
    private ArrayList<String> headshots;
    private ArrayList<String> names;
    private ArrayList<String> roles;
    private ArrayList<String> realNames;
    private LayoutInflater mInflater;
    private final Context context;

    SingleTeamAdapter(Context context, ArrayList<String> names, ArrayList<String> realNames, ArrayList<String> headshots, ArrayList<String> roles) {
        this.mInflater = LayoutInflater.from(context);
        this.names = names;
        this.headshots = headshots;
        this.roles = roles;
        this.context = context;
        this.realNames = realNames;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_team_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.get().load(headshots.get(position)).into(holder.headshot);
        holder.name.setText(names.get(position));
        if (roles.get(position).equals("offense")) {
            holder.role.setText("DAMAGE");
        } else {
            holder.role.setText(roles.get(position).toUpperCase());
        }

        holder.realName.setText(realNames.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return names.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView headshot;
        TextView name;
        TextView role;
        TextView realName;

        ViewHolder(View itemView) {
            super(itemView);
            headshot = itemView.findViewById(R.id.headshot);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            realName = itemView.findViewById(R.id.realName);
        }
    }
}
