package com.carty.riderapp.ui.home.payment_card_setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carty.riderapp.R
import com.carty.riderapp.common.Constants
import com.carty.riderapp.ui.home.payment_card_setting.response.AddCardApiResponse
import com.carty.riderapp.utils.RepoModel

class CardListAdapter(var arrayList: MutableList<AddCardApiResponse.Payload>, var repo: RepoModel) : RecyclerView.Adapter<CardListAdapter.ViewHolder>() {
    var selectionList = HashMap<String, Boolean>()
    fun refreshList(newArrayList: MutableList<AddCardApiResponse.Payload>) {

        arrayList.clear()

        arrayList.addAll(newArrayList)

        for (i in arrayList.indices) {
            selectionList.put("$i", arrayList[i].isDefault)
        }
        notifyDataSetChanged()
    }

    interface CardSelection {
        fun itemClick(position: Int, action: String)
    }

    var cardSelection: CardSelection? = null
    fun setListener(cardSelection: CardSelection) {
        this.cardSelection = cardSelection
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvCardNo = view.findViewById<TextView>(R.id.tvCardNo)
        var radioCash = view.findViewById<ImageView>(R.id.radioCash)
        var imgv_CreditCardDelete = view.findViewById<ImageView>(R.id.imgv_CreditCardDelete)
        var imgv_CreditCard = view.findViewById<ImageView>(R.id.imgv_CreditCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_card_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun onBindViewHolder(holder: CardListAdapter.ViewHolder, position: Int) {

        holder.tvCardNo.text = "**** **** **** ${arrayList[position].last4}"

        if (selectionList.get("" + position)!!) {
            holder.radioCash.setImageResource(R.drawable.radio_selected)
            holder.imgv_CreditCardDelete.visibility = View.GONE
        } else {
            holder.radioCash.setImageResource(R.drawable.radio_unselected)
            holder.imgv_CreditCardDelete.visibility = View.VISIBLE
        }

        if (arrayList[position].brand.contains(Constants.MasterCard, ignoreCase = true)) {
            holder.imgv_CreditCard.setImageResource(R.drawable.ic_mastercard)
        } else if (arrayList[position].brand.contains(Constants.Visa, ignoreCase = true)) {
            holder.imgv_CreditCard.setImageResource(R.drawable.visa)
        } else if (arrayList[position].brand.contains(Constants.Discover, ignoreCase = true)) {
            holder.imgv_CreditCard.setImageResource(R.drawable.discover)
        } else if (arrayList[position].brand.contains(Constants.American_Express, ignoreCase = true)) {
            holder.imgv_CreditCard.setImageResource(R.drawable.american_express)
        } else {
            holder.imgv_CreditCard.setImageResource(R.drawable.ic_credit_card)
        }

        holder.itemView.setOnClickListener {
            for (i in 0 until selectionList.size) {
                if (position == i) {
                    selectionList.put("$i", true)
                    cardSelection!!.itemClick(position, Constants.selected)
                } else {
                    selectionList.put("$i", false)
                }
            }
            notifyDataSetChanged()
        }

        holder.imgv_CreditCardDelete.setOnClickListener {
            cardSelection!!.itemClick(position, Constants.remove)
        }

    }
}