package com.example.saveit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.SavedLink;

public class LinkAdapter extends RecyclerView.Adapter<LinkViewHolder>{

    Context context;
    List<SavedLink> savedLinks;

    public LinkAdapter(Context context, List<SavedLink> savedLinks) {
        this.context = context;
        this.savedLinks = savedLinks;
    }

    @NonNull
    @Override
    public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_link, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinkViewHolder holder, int position) {
        SavedLink currentItem = savedLinks.get(position);
        long timeStamp = currentItem.getCreatedAt();
        String formattedDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date(timeStamp));

        holder.dateview.setText(formattedDate);
        holder.linkview.setText(currentItem.getLink());

    }

    @Override
    public int getItemCount() {
        return savedLinks.size();
    }
}
