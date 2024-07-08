package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.databinding.CustomviewQuestionBinding
import com.onlinetalentsearchexam.maharaj.data.models.QnA
import com.onlinetalentsearchexam.maharaj.data.models.Question

class CorrectAnswerQuestionAdapter(val contxt: Context, var data: List<Question>) : RecyclerView.Adapter<CorrectAnswerQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewQuestionBinding) : RecyclerView.ViewHolder(binding.root){
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
                binding.qNumber.text=(position+1).toString()
                binding.ansRecyclerView.apply {
                    layoutManager=LinearLayoutManager(contxt)
                    adapter=CorrectAnsAnswersAdapter(contxt,data[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewQuestionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.bindItem(position)
    }
    override fun getItemCount(): Int {
        return data.size
    }
}
