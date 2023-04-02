package com.carty.riderapp.ui.home.notification

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.ui.home.trips.response.TripApiResponse

public class TripsAdapter(private var arrayList: MutableList<TripApiResponse.Payload>) :
        RecyclerView.Adapter<TripsAdapter.ContactViewHolder>() {
    lateinit var contextNotification: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        contextNotification = parent.context
        return ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.listrow_trips_list,
                        parent,
                        false
                )
        )
    }

    var tripListClick: TripListClick? = null

    fun setListener(newTripListClick: TripListClick) {
        tripListClick = newTripListClick
    }

    interface TripListClick {
        fun clickItem(pos: Int, action: String)
    }


    override fun getItemCount(): Int = arrayList.size

    fun updateList(updateList: List<TripApiResponse.Payload>) {
        arrayList.clear()
        arrayList.addAll(updateList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.tvDate.text = arrayList.get(position).createdAt
        holder.tvFrom.text = arrayList.get(position).startAddress
        holder.tvTo.text = arrayList.get(position).finishAddress
        holder.tvPrice.text = "${String.format("%.2f", arrayList.get(position).total)}"

        GlobalMethods().loadImagesWithNoProgressbar(
                arrayList.get(position).profilePicture,
                holder.imgUserProfile,
                R.drawable.ic_user,
                300,
                300
        )

        holder.itemView.setOnClickListener {
//            if (tripListClick!=null)
//            {
            tripListClick!!.clickItem(position, Constants.selected)
//            }
        }
        holder.imgcDesputRide.visibility = View.VISIBLE
        holder.imgcDesputRide.setOnClickListener {
            tripListClick!!.clickItem(position, Constants.dispute)
        }

    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvFrom = itemView.findViewById(R.id.tvFrom) as TextView
        var tvTo = itemView.findViewById(R.id.tvTo) as TextView
        var tvDate = itemView.findViewById(R.id.tvDate) as TextView
        var tvPrice = itemView.findViewById(R.id.tvPrice) as TextView
        var imgcDesputRide = itemView.findViewById(R.id.imgcDesputRide) as ImageView
        var imgUserProfile = itemView.findViewById(R.id.imgUserProfile) as CircularImageView
    }

}