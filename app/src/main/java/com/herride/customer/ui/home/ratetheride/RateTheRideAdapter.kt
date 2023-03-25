package com.herride.customer.ui.home.ratetheride

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import com.herride.customer.R
import com.herride.customer.common.GlobalMethods
import com.herride.customer.ui.home.ratetheride.response.RateTheRideUnratedApiResponse

public class RateTheRideAdapter(private var arrayList: MutableList<RateTheRideUnratedApiResponse.Payload>) :
        RecyclerView.Adapter<RateTheRideAdapter.ContactViewHolder>() {
    lateinit var onSelect: OnSelectRateItem
    lateinit var contextNotification: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        contextNotification = parent.context
        return ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.listrow_rate_the_ride_list,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = arrayList.size

    fun updateList(updateList: List<RateTheRideUnratedApiResponse.Payload>) {
        arrayList.clear()
        arrayList.addAll(updateList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.tvDate.text = arrayList.get(position).createdAt
        holder.tvFrom.text = arrayList.get(position).startAddress
        holder.tvTo.text = arrayList.get(position).finishAddress
        holder.tvRate.text = "${String.format("%.2f", arrayList.get(position).total)}"


        GlobalMethods().loadImagesWithNoProgressbar(
                arrayList.get(position).profilePicture,
                holder.imgUserProfile,
                R.drawable.ic_user,
                300,
                300
        )


        //holder.imgUserProfile.setImageResource(arrayList.get(position).imgProfile)
//        /*if (arrayList.get(position).rateStatus) {
//            holder.imgRate.visibility = View.VISIBLE
//            holder.tvRate.visibility = View.GONE
//        } else {
//            holder.imgRate.visibility = View.GONE
//            holder.tvRate.visibility = View.VISIBLE
//        }*/
        holder.itemView.setOnClickListener {
            onSelect.getRateItem(arrayList.get(position), position)
        }
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFrom = itemView.findViewById(R.id.tvFrom) as TextView
        var tvTo = itemView.findViewById(R.id.tvTo) as TextView
        var tvDate = itemView.findViewById(R.id.tvDate) as TextView
        var tvRate = itemView.findViewById(R.id.tvRate) as TextView
        var imgRate = itemView.findViewById(R.id.imgRate) as ImageView
        var imgUserProfile = itemView.findViewById(R.id.imgUserProfile) as CircularImageView
    }

    fun setClickListener(onSelect: OnSelectRateItem) {
        this.onSelect = onSelect
    }

    interface OnSelectRateItem {
        fun getRateItem(
                basketItemModel: RateTheRideUnratedApiResponse.Payload,
                position: Int
        )
    }
}