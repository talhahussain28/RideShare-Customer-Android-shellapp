package com.carty.riderapp.ui.home.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import com.carty.riderapp.R
import com.carty.riderapp.common.GlobalMethods
import com.carty.riderapp.ui.home.setting.response.EmergencyContactApiResponse

public class EmergencyContactAdapter(private var arrayList: MutableList<EmergencyContactApiResponse.Payload>) :
        RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder>() {
    lateinit var contextNotification: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        contextNotification = parent.context
        return ContactViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.listrow_contact_list,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = arrayList.size

    fun updateList(updateList: List<EmergencyContactApiResponse.Payload>) {
        arrayList.clear()
        arrayList.addAll(updateList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.tvUserName.text = arrayList.get(position).name


        var tempNumber = arrayList.get(position).mobile
        var convertedNumber = ""
        if (tempNumber.length >= 10) {
            convertedNumber = ""
            convertedNumber =
                    "(${tempNumber[0]}${tempNumber[1]}${tempNumber[2]}) ${tempNumber[3]}" +
                            "${tempNumber[4]}${tempNumber[5]}-${tempNumber[6]}${tempNumber[7]}${tempNumber[8]}" +
                            "${tempNumber[9]}"
        }

        holder.tvMobileNumber.text = "${arrayList.get(position).mobileCountryCode}${convertedNumber}"
        //holder.imgUserProfile.setImageResource(arrayList.get(position).imgProfile)
        GlobalMethods().loadImagesWithNoProgressbar(
                arrayList.get(position).profilePicture,
                holder.imgUserProfile,
                R.drawable.ic_user,
                300,
                300
        )
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUserName = itemView.findViewById(R.id.tvUserName) as TextView
        var tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber) as TextView
        var imgUserProfile = itemView.findViewById(R.id.imgUserProfile) as CircularImageView
    }

}