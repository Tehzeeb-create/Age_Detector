package com.example.agedetector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsageAdapter(private val usageList: List<UsageData>) : RecyclerView.Adapter<UsageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage, parent, true)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usageData = usageList[position]

        holder.usernameTextView.text = "Username: ${usageData.username}"
        holder.dateTextView.text = "Date: ${usageData.date}"
        holder.timeTextView.text = "Time: ${usageData.time}"
    }

    override fun getItemCount(): Int {
        return usageList.size
    }
}
