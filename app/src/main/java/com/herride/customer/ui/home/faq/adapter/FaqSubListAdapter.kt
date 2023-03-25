package com.herride.customer.ui.home.faq.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.herride.customer.R
import com.herride.customer.ui.home.faq.response.FaqSubListApiResponse
import com.herride.customer.utils.RepoModel

class FaqSubListAdapter(var arrayList: MutableList<FaqSubListApiResponse.Payload>, var repo: RepoModel) : RecyclerView.Adapter<FaqSubListAdapter.ViewHolder>() {
    var selectionList = HashMap<String, Boolean>()
    fun refreshList(newArrayList: MutableList<FaqSubListApiResponse.Payload>) {

        arrayList.clear()

        arrayList.addAll(newArrayList)

        notifyDataSetChanged()
    }

    interface FaqSelection {
        fun itemClick(position: Int, action: String)
    }

    var faqSelection: FaqSelection? = null
    fun setListener(faqSelection: FaqSelection) {
        this.faqSelection = faqSelection
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvFaqTitle = view.findViewById<TextView>(R.id.tvFaqTitle)
        var imgv_Faq = view.findViewById<ImageView>(R.id.imgv_Faq)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqSubListAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_faq_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    override fun onBindViewHolder(holder: FaqSubListAdapter.ViewHolder, position: Int) {
        var paylod = arrayList[position]

        holder.tvFaqTitle.text = paylod.question
        holder.itemView.setOnClickListener {
            holder.tvFaqTitle.performClick()
        }
        holder.tvFaqTitle.setOnClickListener {
            faqSelection!!.itemClick(position, "")
        }
    }
}