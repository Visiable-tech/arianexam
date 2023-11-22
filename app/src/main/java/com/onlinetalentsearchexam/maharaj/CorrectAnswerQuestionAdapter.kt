package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.databinding.CustomviewQuestionBinding
import com.onlinetalentsearchexam.maharaj.data.models.QnA

class CorrectAnswerQuestionAdapter(val contxt: Context, var data: List<QnA>) : RecyclerView.Adapter<CorrectAnswerQuestionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewQuestionBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(position: Int){
            data[position].apply {
                binding.question.text=question
                binding.qNumber.text=(position+1).toString()
                binding.ansRecyclerView.apply {
                    layoutManager=LinearLayoutManager(contxt)
                    adapter=AnswersAdapter(contxt,data[position])
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
