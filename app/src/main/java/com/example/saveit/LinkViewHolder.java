package com.example.saveit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinkViewHolder extends RecyclerView.ViewHolder {

    TextView linkview, dateview, titleview;
    public LinkViewHolder(@NonNull View itemView) {
        super(itemView);
        linkview = itemView.findViewById(R.id.link);
        dateview = itemView.findViewById(R.id.date);
        titleview = itemView.findViewById(R.id.title);
    }
}
