package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.R
import com.arianinstitute.databinding.CustomviewAnsBinding
import com.onlinetalentsearchexam.maharaj.data.models.Question

class QuizAnswerAdapter(val context: Context, var quesPos:Int, var data: Question, var listener:QuestionAnswerButtonsListener) : RecyclerView.Adapter<QuizAnswerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewAnsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(position: Int){
            data.ans_arr!![position].apply {
                binding.ans.text= Html.fromHtml(answer)
                binding.number.text=(position+1).toString()
                if(data.selectedAnsId==answer_id) {
                    binding.layoutRoot.setBackgroundColor(context.getColor(R.color.gray))
                    binding.ans.setTextColor(context.getColor(R.color.white))
                }else
                    binding.layoutRoot.setBackgroundColor(context.getColor(com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
            }
            binding.layoutRoot.setOnClickListener{
                listener.onAnswerClick(position, quesPos,data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CustomviewAnsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.bindItem(position)
    }
    override fun getItemCount(): Int {
        return data.ans_arr?.size ?: 0
    }

}
