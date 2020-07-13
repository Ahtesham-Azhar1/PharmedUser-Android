package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zealsoft.pharmed.R

class TimingsListKAdapter (val activity: Activity, private val timingsList: List<String>, val nightMode: Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<TimingListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TimingListViewHolder {
        return TimingListViewHolder(LayoutInflater.from(activity).inflate(R.layout.single_timing_item, p0, false))
    }

    override fun getItemCount(): Int {
        return timingsList.size
    }

    override fun onBindViewHolder(holder: TimingListViewHolder, p1: Int) {

        val item : String = timingsList[p1]

        val day = item.substring(0, item.indexOf(":") + 1)
        val timing = item.substring(item.indexOf(":") + 1)

        if (nightMode) {
            holder.day.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.timing.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
        }

        holder.day.text = day.trim { it <= ' ' }
        holder.timing.text = timing
    }

}