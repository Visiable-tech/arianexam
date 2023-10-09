package com.arianinstitute.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.Note
import com.arianinstitute.R
import com.arianinstitute.StartTestActivity

class PageIndicatorAdapter(context: Context,listener: PageIndicatorListener): RecyclerView.Adapter<PageIndicatorAdapter.ViewHolder>() {
    private var allNotes = ArrayList<Note>()
    lateinit var listener: PageIndicatorListener
    var selectedPosition=0
    lateinit var context: Context
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
        return allNotes.size
    }
    fun updateData(notes:List<Note>){
        allNotes.clear(); allNotes.addAll(notes)
        notifyDataSetChanged()
    }
    fun updatePosition(pos:Int){
        selectedPosition=pos
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.number.text= (position+1).toString()
        holder.rootview.setOnClickListener(View.OnClickListener {
            listener.onPressIndicator(position)
            selectedPosition=position
        })

        if (allNotes.get(position).SelAnsID=="301"){
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.lightred1,null))
        }else{
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.green,null))
        }
        if(selectedPosition==position){
            holder.rootview.setCardBackgroundColor(context.resources.getColor(R.color.themecolor,null))
        }
    }
}
interface PageIndicatorListener{
    fun onPressIndicator(pos:Int)
}