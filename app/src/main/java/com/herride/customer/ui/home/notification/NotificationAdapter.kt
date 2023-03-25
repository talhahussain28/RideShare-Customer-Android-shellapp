package com.herride.customer.ui.home.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.herride.customer.R
import com.herride.customer.ui.home.notification.response.NotificationApiResponse

public class NotificationAdapter(private var arrayList: MutableList<NotificationApiResponse.Payload>) :
        RecyclerView.Adapter<NotificationAdapter.ContactViewHolder>() {
    lateinit var contextNotification: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        contextNotification = parent.context
        return ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.listrow_notification_list,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = arrayList.size

    fun updateList(updateList: List<NotificationApiResponse.Payload>) {
        arrayList.clear()
        arrayList.addAll(updateList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.tvDate.text = arrayList.get(position).createdAt
        holder.tvDescription.text = arrayList.get(position).description
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate = itemView.findViewById(R.id.tvDate) as TextView
        var tvDescription = itemView.findViewById(R.id.tvDescription) as TextView
    }

}