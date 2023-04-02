package com.carty.riderapp.ui.home.setting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.home.setting.response.AddressGetAddDeleteApiResponse
import com.carty.riderapp.utils.RepoModel

class AddressListAdapter(var repo: RepoModel, var arraList: MutableList<AddressGetAddDeleteApiResponse.Payload>) : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {

    fun refreshAddressList(newArraList: MutableList<AddressGetAddDeleteApiResponse.Payload>) {
        arraList.clear()
        arraList.addAll(newArraList)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTittleOther = view.findViewById<TextView>(R.id.tvTittleOther)
        var tvAddressFullOther = view.findViewById<TextView>(R.id.tvAddressFullOther)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_address_other, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arraList.size
    }

    override fun onBindViewHolder(holder: AddressListAdapter.ViewHolder, position: Int) {
        var paylod = arraList[position]
        holder.tvTittleOther.text = paylod.title
        holder.tvAddressFullOther.text = paylod.address
        holder.itemView.setOnClickListener {
            addressClickListener!!.addressSelect(position, Constants.selected)
        }
    }


    var addressClickListener: AddressClickListener? = null

    fun setListener(addressClickListener: AddressClickListener) {
        this.addressClickListener = addressClickListener
    }

    interface AddressClickListener {
        fun addressSelect(position: Int, action: String)
    }

}