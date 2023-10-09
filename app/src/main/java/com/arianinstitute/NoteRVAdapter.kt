package com.arianinstitute

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

class NoteRVAdapter(
    val context: Context,
    var viewModal: NoteViewModal,
    val noteClickInterface: NoteClickInterface
) :
    RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    //on below line we are creating a variable for our all notes list.
    private var
            allNotes = ArrayList<Note>()
    //on below line we are creating a view holder class.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //on below line we are creating an initializing all our variables which we have added in layout file.
        val noteTV = itemView.findViewById<TextView>(R.id.idTVNote)
        val dateTV = itemView.findViewById<TextView>(R.id.idTVDate)
//        val viewAll = itemView.findViewById<ImageView>(R.id.viewAll)
//        val closeAll = itemView.findViewById<ImageView>(R.id.closeAll)
        val cardBack = itemView.findViewById<CardView>(R.id.cardBack)
        val attempt = itemView.findViewById<ImageView>(R.id.attempt)
        val notAttempt = itemView.findViewById<ImageView>(R.id.notAttempt)
        var radio1= itemView.findViewById<RadioButton>(R.id.radio1)
        var radio2= itemView.findViewById<RadioButton>(R.id.radio2)
        var radio3= itemView.findViewById<RadioButton>(R.id.radio3)
        var radio4= itemView.findViewById<RadioButton>(R.id.radio4)
        val qusNum= itemView.findViewById<TextView>(R.id.qusNum)
        //var next: TextView
        var rdgrp= itemView.findViewById<RadioGroup>(R.id.rdgrp)
        var rd1= itemView.findViewById<TextView>(R.id.rd1)
        var rd2= itemView.findViewById<TextView>(R.id.rd2)
        var rd3= itemView.findViewById<TextView>(R.id.rd3)
        var rd4= itemView.findViewById<TextView>(R.id.rd4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //on below line we are setting data to item of recycler view.
        holder.noteTV.setText(allNotes.get(position).noteTitle)
        holder.qusNum.setText(context!!.resources.getString(R.string.questionno)+(position+1))
        if (allNotes.get(position).SelAnsID=="301"){
            holder.dateTV.setText("Answer not selected!")
            holder.attempt.visibility = View.GONE
            holder.notAttempt.visibility = View.VISIBLE
        }else{
            holder.dateTV.setText(allNotes.get(position).ansTxt)
            holder.attempt.visibility = View.VISIBLE
            holder.notAttempt.visibility = View.GONE
        }

        if(allNotes[position].SelAnsID!="301")
            holder.rdgrp.visibility=View.GONE
        holder.cardBack.setOnClickListener {
            if(holder.rdgrp.visibility==View.VISIBLE)
                holder.rdgrp.visibility=View.GONE
            else
                holder.rdgrp.visibility=View.VISIBLE
            //on below line we are calling a note click interface and we are passing a position to it.
        }
//        holder.closeAll.setOnClickListener {
//            //on below line we are calling a note click interface and we are passing a position to it.
//            /*noteClickInterface.onNoteClick(allNotes.get(position))*/
//            holder.closeAll.visibility = View.GONE
//            holder.viewAll.visibility = View.VISIBLE
//            holder.rdgrp.visibility = View.GONE
//        }
        var ansid: String=""
        var ansText: String=""
        var title: String=allNotes.get(position).noteTitle
        var ansA: String=allNotes.get(position).ansA
        var ansB: String=allNotes.get(position).ansB
        var ansC: String=allNotes.get(position).ansC
        var ansD: String=allNotes.get(position).ansD
        var qnaID: String=allNotes.get(position).QusID
        holder.rdgrp.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: View = holder.rdgrp.findViewById(checkedId)
            val index: Int = holder.rdgrp.indexOfChild(radioButton)
            if (index == 0) {
                ansid = holder.rd1.text.toString()
                ansText = allNotes.get(position).ansA
                val updatedNote = Note(title, ansA, ansB,ansC,ansD,qnaID,ansText,
                    ansid,"1")
                updatedNote.id = allNotes.get(position).id
                viewModal.updateNote(updatedNote)
                holder.dateTV.setText(allNotes.get(position).ansTxt)
                holder.radio1.setBackgroundColor(Color.DKGRAY)

                //indexnumber=index
            } else if (index == 1) {
                ansid = holder.rd2.text.toString()
                ansText = allNotes.get(position).ansB
                val updatedNote = Note(title, ansA, ansB,ansC,ansD,qnaID,ansText,
                    ansid,"1")
                updatedNote.id = allNotes.get(position).id
                viewModal.updateNote(updatedNote)
                holder.dateTV.setText(allNotes.get(position).ansTxt)
                holder.radio2.setBackgroundColor(Color.DKGRAY)
                //indexnumber=index
                //Log.e("sushovon", holder.rd2.text.toString())
            } else if (index == 2) {
                ansid = holder.rd3.text.toString()
                ansText = allNotes.get(position).ansC
                val updatedNote = Note(title, ansA, ansB,ansC,ansD,qnaID,ansText,
                    ansid,"1")
                updatedNote.id = allNotes.get(position).id
                viewModal.updateNote(updatedNote)
                holder.dateTV.setText(allNotes.get(position).ansTxt)
                holder.radio3.setBackgroundColor(Color.DKGRAY)
                //indexnumber=index
            } else if (index == 3) {
                ansid = holder.rd4.text.toString()
                ansText = allNotes.get(position).ansD
                val updatedNote = Note(title, ansA, ansB,ansC,ansD,qnaID,ansText,
                    ansid,"1")
                updatedNote.id = allNotes.get(position).id
                viewModal.updateNote(updatedNote)
                holder.dateTV.setText(allNotes.get(position).ansTxt)
                holder.radio4.setBackgroundColor(Color.DKGRAY)
                //indexnumber=index
            }
        }


        holder.radio1.setText(allNotes.get(position).ansA)



        holder.radio2.setText(allNotes.get(position).ansB)



        holder.radio3.setText(allNotes.get(position).ansC)


        holder.radio4.setText(allNotes.get(position).ansD)

        if(allNotes.get(position).ansTxt == allNotes.get(position).ansA){
            holder.radio1.setBackgroundColor(Color.DKGRAY)
            holder.radio2.setBackgroundColor(Color.TRANSPARENT)
            holder.radio3.setBackgroundColor(Color.TRANSPARENT)
            holder.radio4.setBackgroundColor(Color.TRANSPARENT)
        }else if (allNotes.get(position).ansTxt == allNotes.get(position).ansB){
            holder.radio1.setBackgroundColor(Color.TRANSPARENT)
            holder.radio2.setBackgroundColor(Color.DKGRAY)
            holder.radio3.setBackgroundColor(Color.TRANSPARENT)
            holder.radio4.setBackgroundColor(Color.TRANSPARENT)
        }else if(allNotes.get(position).ansTxt == allNotes.get(position).ansC){
            holder.radio1.setBackgroundColor(Color.TRANSPARENT)
            holder.radio2.setBackgroundColor(Color.TRANSPARENT)
            holder.radio3.setBackgroundColor(Color.DKGRAY)
            holder.radio4.setBackgroundColor(Color.TRANSPARENT)
        }else if(allNotes.get(position).ansTxt == allNotes.get(position).ansD){
            holder.radio1.setBackgroundColor(Color.TRANSPARENT)
            holder.radio2.setBackgroundColor(Color.TRANSPARENT)
            holder.radio3.setBackgroundColor(Color.TRANSPARENT)
            holder.radio4.setBackgroundColor(Color.DKGRAY)
        }



        }

    override fun getItemCount(): Int {
        //on below line we are returning our list size.
        return allNotes.size
    }

    //below method is use to update our list of notes.
    fun updateList(newList: List<Note>) {
        //on below line we are clearing our notes array list/
        allNotes.clear()
        //on below line we are adding a new list to our all notes list.
        allNotes.addAll(newList)
        //on below line we are calling notify data change method to notify our adapter.
        notifyDataSetChanged()
    }

}

interface NoteClickDeleteInterface {
    //creating a method for click action on delete image view.
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    //creating a method for click action on recycler view item for updating it.
    fun onNoteClick(note: Note)
}