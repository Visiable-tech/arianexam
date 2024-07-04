package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.databinding.CustomviewQuestionFinaldialogBinding
import com.onlinetalentsearchexam.maharaj.data.models.Question

class FinalDialogQuestionAdapter(val contxt: Context, var data: List<Question> = listOf() ,var listener:QuestionAnswerButtonsListener) : RecyclerView.Adapter<FinalDialogQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewQuestionFinaldialogBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(position: Int){
            data[position].apply {
                binding.qusNum.text = "Q. "+(position+1)
                binding.questionWebview.apply {
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
                    loadData(question!!,"text/html","utf-8")
                }
                binding.ansRecyclerView.apply {
                    layoutManager=LinearLayoutManager(contxt)
                    adapter=FinalDialogAnswerAdapter(contxt,position,data[position],listener)
                }
                if(this.selectedAnsId!="0") {
                    binding.givenAnswer.text = selectedAns
                    binding.ansRecyclerView.visibility = View.GONE
                    binding.attempt.visibility=View.VISIBLE
                    binding.notAttempt.visibility=View.INVISIBLE
                }else{
                    binding.notAttempt.visibility=View.VISIBLE
                    binding.attempt.visibility=View.INVISIBLE
                }
                binding.cardRoot.setOnClickListener{
                    if(binding.ansRecyclerView.visibility==View.GONE)
                        binding.ansRecyclerView.visibility=View.VISIBLE
                    else
                        binding.ansRecyclerView.visibility=View.GONE
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewQuestionFinaldialogBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.bindItem(position)
    }
    override fun getItemCount(): Int {
        return data.size
    }
    fun updateData(data:List<Question>){
        this.data=data
        notifyDataSetChanged()
    }
}
