package com.zealsoft.pharmed.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zealsoft.pharmed.R;

public class TimingListViewHolder extends RecyclerView.ViewHolder {

    public TextView day, timing;

    public TimingListViewHolder(@NonNull View itemView) {
        super(itemView);

        day = itemView.findViewById(R.id.day);
        timing = itemView.findViewById(R.id.timing);
    }
}
