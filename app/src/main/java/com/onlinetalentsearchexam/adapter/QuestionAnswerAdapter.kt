package com.onlinetalentsearchexam.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.R
import com.onlinetalentsearchexam.Note
import com.onlinetalentsearchexam.NoteViewModal
import com.onlinetalentsearchexam.utils.Utils
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class QuestionAnswerAdapter :
    RecyclerView.Adapter<QuestionAnswerAdapter.MyViewHolder> {

    var data: ArrayList<HashMap<String, String>>? = null
    var answerdata: ArrayList<HashMap<String, String>>? = null
    var resultp = HashMap<String, String>()
    var answertp = HashMap<String, String>()
    var context: Context? = null
    var totaldata: Int=0
    var customListner: customButtonListener? = null
    var customListner1: customButtonListener1? = null
    var customListner2: customButtonListener2? = null
    lateinit var viewModal: NoteViewModal
    var noteID = -1;
     var
            allNotesx = ArrayList<Note>()




    interface customButtonListener {
        fun onButtonClickListner(pos: Int,examid: String, questionid: String, answerid: String)
    }
    fun setCustomButtonListner(listener: customButtonListener?) {
        customListner = listener
    }

    interface customButtonListener1 {
        fun nextButtonListener()
    }
    fun setCustomButtonListner1(listener: customButtonListener1?) {
        customListner1 = listener
    }

    interface customButtonListener2 {
        fun prevButtonListener()
    }
    fun setCustomButtonListner2(listener: customButtonListener2?) {
        customListner2 = listener
    }
    class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var question: WebView
        var numbering : TextView
        var radio1: RadioButton
        var radio2: RadioButton
        var radio3: RadioButton
        var radio4: RadioButton
        //var next: TextView
        var rdgrp: RadioGroup
        var rd1: TextView
        var rd2: TextView
        var rd3: TextView
        var rd4: TextView
        var qusid: TextView
        var examid: TextView
        var submitans: AppCompatButton
        var right_arrow: ImageView
        var arrow_left: ImageView

        init {
            numbering=itemView.findViewById(R.id.numbering)
            question = itemView.findViewById(R.id.question)
            radio1 = itemView.findViewById(R.id.radio1)
            radio2 = itemView.findViewById(R.id.radio2)
            radio3 = itemView.findViewById(R.id.radio3)
            radio4 = itemView.findViewById(R.id.radio4)
            //next = itemView.findViewById(R.id.next)
            rdgrp = itemView.findViewById(R.id.rdgrp)
            rd1 = itemView.findViewById(R.id.rd1)
            rd2 = itemView.findViewById(R.id.rd2)
            rd3 = itemView.findViewById(R.id.rd3)
            rd4 = itemView.findViewById(R.id.rd4)
            qusid = itemView.findViewById(R.id.qusid)
            examid=itemView.findViewById(R.id.examid)
            submitans= itemView.findViewById(R.id.submitans)
            right_arrow= itemView.findViewById(R.id.right_arrow)
            arrow_left=itemView.findViewById(R.id.arrow_left)


        }
    }

    constructor(
        context: Context?,
        arraylist: ArrayList<HashMap<String, String>>?,
        answerlist: ArrayList<HashMap<String, String>>?,
        noteViewModal: NoteViewModal,
        allNotes: ArrayList<Note>,
    )  {
        data = arraylist
        answerdata = answerlist
        this.context = context
        viewModal = noteViewModal
        allNotesx = allNotes
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.customview_question_quiz, parent, false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        totaldata=allNotesx.size
        return allNotesx.size
    }
    fun updateList(newList: List<Note>) {
        //on below line we are clearing our notes array list/
        allNotesx.clear()
        //on below line we are adding a new list to our all notes list.
        allNotesx.addAll(newList)
        //on below line we are calling notify data change method to notify our adapter.
        notifyDataSetChanged()
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        resultp = data!![holder.absoluteAdapterPosition]
        answertp=answerdata!![holder.absoluteAdapterPosition]

       /* viewModal = ViewModelProvider(context).get(NoteViewModal::class.java)
        viewModal = ViewModelProvider(
            this.context,
            ViewModelProvider.AndroidViewModelFactory.getInstance()
        ).get(NoteViewModal::class.java)*/

        /*if(totaldata==(holder.absoluteAdapterPosition+1)){
            holder.submit.visibility=View.VISIBLE
            holder.next.visibility=View.GONE
        }*/


        holder.qusid.text=resultp["question_id"]
        holder.examid.text=resultp["exam_taken_id"]
        //var indexnumber: Int
        var ansid: String=""
        var ansText: String=""
        var qusID: String=resultp["question_id"].toString()
        holder.rdgrp.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                val radioButton: View = holder.rdgrp.findViewById(checkedId)
                val index: Int = holder.rdgrp.indexOfChild(radioButton)

                if(index==0) {
                    ansid = holder.rd1.text.toString()
                    ansText = Html.fromHtml(answertp["answer0"]).toString()
                    //indexnumber=index
                }
                else if(index==1) {
                    ansid = holder.rd2.text.toString()
                    ansText = Html.fromHtml(answertp["answer1"]).toString()
                    //indexnumber=index
                    //Log.e("sushovon", holder.rd2.text.toString())
                }
                else if(index==2) {
                    ansid = holder.rd3.text.toString()
                    ansText = Html.fromHtml(answertp["answer2"]).toString()
                    //indexnumber=index
                }
                else if(index==3) {
                    ansid = holder.rd4.text.toString()
                    ansText = Html.fromHtml(answertp["answer3"]).toString()
                    //indexnumber=index
                }


            }
        })

            holder.submitans.setOnClickListener {
                if(!ansid.isNullOrEmpty()){
                    val updatedNote = Note(resultp["question"].toString(),Html.fromHtml(answertp["answer0"]).toString(),
                        Html.fromHtml(answertp["answer1"]).toString(),Html.fromHtml(answertp["answer2"]).toString(),
                        Html.fromHtml(answertp["answer3"]).toString(),qusID,Html.fromHtml(ansText).toString(), ansid, "1")

                    updatedNote.id = allNotesx.get(position).id
                    viewModal.updateNote(updatedNote)
                    /*Utils.showToast(
                        "Saved"+(allNotesx.get(position).id),
                        context
                    )*/

                    customListner?.onButtonClickListner(holder.absoluteAdapterPosition+1,holder.examid.text.toString(),holder.qusid.text.toString(),ansid)
                }else{
                    Utils.showToast("Select any one option to submit asnwer",context)
                }
            }



        holder.right_arrow.setOnClickListener {
            customListner1?.nextButtonListener()
        }

        holder.arrow_left.setOnClickListener {
            //updatedNote.id = holder.absoluteAdapterPosition+1
            customListner2?.prevButtonListener()
        }

        holder.numbering.setText(context!!.resources.getString(R.string.questionno)+(holder.absoluteAdapterPosition+1))
        Log.d("GGGInsideAdaptr","Question: "+allNotesx.get(position).noteTitle)
        holder.question.apply {
            settings.javaScriptEnabled=true
            webViewClient= object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    view?.loadUrl("javascript:(function() {" +
                            "var imgs = document.getElementsByTagName('img');" +
                            "for (var i = 0; i < imgs.length; i++) {" +
                            "  imgs[i].style.maxWidth = '100%';" +
                            "  imgs[i].style.height = 'auto';" +
                            "}" +
                            "})()")
                }
            }
            loadData(allNotesx.get(position).noteTitle,"text/html","utf-8")
        }

        if(answerdata!!.size>0){
            for(i in 0 until answerdata!!.size){
                if(i==0){
                    holder.radio1.setText(Html.fromHtml(answertp["answer0"]))
                    holder.rd1.text=answertp["answer_id0"]
                }
                else if(i==1){
                    holder.radio2.setText(Html.fromHtml(answertp["answer1"]))
                    holder.rd2.text=answertp["answer_id1"]
                }
                else if(i==2){
                    holder.radio3.setText(Html.fromHtml(answertp["answer2"]))
                    holder.rd3.text=answertp["answer_id2"]
                }
                else if(i==3){
                    holder.radio4.setText(Html.fromHtml(answertp["answer3"]))
                    holder.rd4.text=answertp["answer_id3"]
                }
            }
        }

    }


}