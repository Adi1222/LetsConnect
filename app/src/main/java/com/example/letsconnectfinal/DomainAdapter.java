package com.example.letsconnectfinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DomainAdapter  extends RecyclerView.Adapter<DomainAdapter.DomainViewHolder>{

    private List<Domain> domainData;
    private Context mContext;

    public DomainAdapter(List<Domain> listData) {
        this.domainData = listData;
    }

    @NonNull
    @Override
    public DomainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.domain,parent,false);
        return new DomainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DomainViewHolder holder, int position) {
        Domain ld=domainData.get(position);
        holder.name.setText(ld.getName());
        final String dd = ld.getName();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IOTPage.class).putExtra("domName", dd);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return domainData.size();
    }


    class DomainViewHolder extends RecyclerView.ViewHolder{

        TextView name,description;

        public DomainViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
