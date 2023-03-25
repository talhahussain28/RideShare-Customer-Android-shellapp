package com.herride.customer.ui.home.faq.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.herride.customer.R
import com.herride.customer.ui.home.faq.response.FaqListApiResponse
import com.herride.customer.utils.RepoModel

class FaqListAdapter(var arrayList: MutableList<FaqListApiResponse.Payload>, var repo: RepoModel) : RecyclerView.Adapter<FaqListAdapter.ViewHolder>() {
    var selectionList = HashMap<String, Boolean>()
    fun refreshList(newArrayList: MutableList<FaqListApiResponse.Payload>) {

        Log.e("FaqListAdapter","newArrayList.zise = ${newArrayList.size}")
        arrayList.clear()

        arrayList.addAll(newArrayList)
        notifyDataSetChanged()
        Log.e("FaqListAdapter","arrayList.zise = ${arrayList.size}")
        Log.e("FaqListAdapter","arrayList.data = ${Gson().toJson(arrayList)}")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqListAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_faq_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.e("FaqListAdapter","getItemCount = ${arrayList.size}")
        return arrayList.size
    }


    override fun onBindViewHolder(holder: FaqListAdapter.ViewHolder, position: Int) {
        var paylod = arrayList[position]
        Log.e("FaqListAdapter","paylod.title = ${paylod.title}")
        Log.e("FaqListAdapter","paylod.helpCategoryId = ${paylod.helpCategoryId}")
        holder.tvFaqTitle.text = paylod.title
        holder.itemView.setOnClickListener {
            holder.tvFaqTitle.performClick()
        }
        holder.tvFaqTitle.setOnClickListener {
            faqSelection!!.itemClick(position, "")
        }
    }
}