package com.example.letsconnectfinal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventData;

    public EventAdapter(List<Event> listData) {
        this.eventData = listData;
        }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event,parent,false);
        return new EventViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event ld= eventData.get(position);
        holder.title.setText(ld.getTitle());
        holder.location.setText(ld.getLocation());
        holder.date.setText(ld.getDate());
        holder.link.setText(ld.getLink());
        holder.description.setText(ld.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(v.getContext(),addEvent.class);
                v.getContext().startActivity(intent);
        }
        });
        }

        @Override
        public int getItemCount() {
        return eventData.size();
        }

    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView title,date,location,link,description;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_title);
            location = itemView.findViewById(R.id.event_location);
            date= itemView.findViewById(R.id.startDate);
            link = itemView.findViewById(R.id.event_link);
            description = itemView.findViewById(R.id.event_desc);
        }
    }
}
