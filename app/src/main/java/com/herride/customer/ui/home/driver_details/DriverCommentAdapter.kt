package com.herride.customer.ui.home.driver_details

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import com.herride.customer.R
import com.herride.customer.common.GlobalMethods
import com.herride.customer.ui.home.driver_details.response.DriverDetailApiResponse
import com.herride.customer.ui.home.ratetheride.RateTheRideModel
import com.herride.customer.utils.RepoModel

public class DriverCommentAdapter(var repo: RepoModel, var arrayList: MutableList<DriverDetailApiResponse.Payload.Comment>) :
        RecyclerView.Adapter<DriverCommentAdapter.ContactViewHolder>() {
    lateinit var onSelect: OnSelectRateItem
    lateinit var contextNotification: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        contextNotification = parent.context
        return ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_comments_driver,
                        parent,
                        false
                )
        )
    }

    //    override fun getItemCount(): Int = arrayList.size
    override fun getItemCount(): Int = arrayList.size

    fun updateList(updateList: List<DriverDetailApiResponse.Payload.Comment>) {
        Log.e("getDriverDetailApi","11 arrayList.size = ${arrayList.size} && updateList.size = ${updateList.size}")
        arrayList.clear()
        Log.e("getDriverDetailApi","22 arrayList.size = ${arrayList.size}")
        arrayList.addAll(updateList)
        Log.e("getDriverDetailApi","33 arrayList.size = ${arrayList.size}")
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        var payload = arrayList[position]

        holder.tvUserName.text = "${payload.name}"
        holder.tvComment.text = "${payload.comment}"

        GlobalMethods().loadImagesWithProgressbar(
                payload.profilePicture,
                holder.imgUserProfile,
                R.drawable.ic_user,
                holder.pbLoadingComment,
                300,
                300
        )
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
        var tvComment = itemView.findViewById(R.id.tvComment) as TextView

        var imgUserProfile = itemView.findViewById(R.id.imgUserProfile) as CircularImageView
        var pbLoadingComment = itemView.findViewById(R.id.pbLoadingComment) as ProgressBar
    }

    fun setClickListener(onSelect: OnSelectRateItem) {
        this.onSelect = onSelect
    }

    interface OnSelectRateItem {
        fun getRateItem(
                basketItemModel: RateTheRideModel,
                position: Int
        )
    }
}