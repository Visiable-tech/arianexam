package com.onlinetalentsearchexam.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.R
import com.onlinetalentsearchexam.maharaj.data.models.Question

class PageIndicatorAdapter(var context: Context,listener: PageIndicatorListener,var data:List<Question>  = listOf()): RecyclerView.Adapter<PageIndicatorAdapter.ViewHolder>() {
    lateinit var listener: PageIndicatorListener
    var selectedPosition=0
    init {
        this.listener=listener
        this.context=context
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootview=itemView.findViewById<CardView>(R.id.indicator_root_cardView)
        val number=itemView.findViewById<TextView>(R.id.indicator_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_page_indicator,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun updatePosition(pos:Int){
        selectedPosition=pos
        notifyDataSetChanged()
    }
    fun updateData(data:List<Question>){
        this.data=data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text= (position+1).toString()
        holder.rootview.setOnClickListener(View.OnClickListener {
            listener.onPressIndicator(position)
            selectedPosition=position
        })

        if (data.get(position).selectedAnsId=="0"){
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.lightred1,null))
        }else{
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.green,null))
        }
        if(selectedPosition==position){
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.gray,null))
        }
    }
}
interface PageIndicatorListener{
    fun onPressIndicator(pos:Int)
}