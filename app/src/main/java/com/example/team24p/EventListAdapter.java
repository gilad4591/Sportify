package com.example.team24p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    public List<Events> eventsList;
    public EventListAdapter(List<Events> eventsList){
        this.eventsList = eventsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_events,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(eventsList.get(position).getUsername());
        holder.groundText.setText(eventsList.get(position).getGround());
        holder.dateText.setText(eventsList.get(position).getDate().toString());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView nameText;
        public TextView groundText;
        public TextView dateText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            nameText = (TextView)mView.findViewById(R.id.nameView);
            groundText = (TextView)mView.findViewById(R.id.groundView);
            dateText = (TextView)mView.findViewById(R.id.dateView);
        }
    }

}
