package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.databinding.CustomviewQuestionQuizBinding
import com.onlinetalentsearchexam.maharaj.data.models.Question

class QuizQuestionAdapter(val contxt: Context, var listener:QuestionAnswerButtonsListener, var data: List<Question> = listOf()) : RecyclerView.Adapter<QuizQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewQuestionQuizBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(position: Int){
            data[position].apply {
                binding.question.apply {
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
                binding.numbering.text=(position+1).toString()
                binding.ansRecyclerView.apply {
                    layoutManager=LinearLayoutManager(contxt)
                    adapter=QuizAnswerAdapter(contxt,position,data[position],listener)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewQuestionQuizBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
interface QuestionAnswerButtonsListener{
    fun onAnswerClick(ansPos:Int,quesPos:Int,question:Question)

}
