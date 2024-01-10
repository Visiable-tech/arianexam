package com.onlinetalentsearchexam.maharaj

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.R
import com.arianinstitute.databinding.CustomviewAnsBinding
import com.arianinstitute.databinding.QuestionListitemBinding
import com.onlinetalentsearchexam.maharaj.data.models.Ans
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.data.models.QnA

class AnswersAdapter(val context: Context, var data: QnA) : RecyclerView.Adapter<AnswersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CustomviewAnsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItem(position: Int){
            data.ans_arr!![position].apply {
                binding.ans.text= Html.fromHtml(answer)
                binding.number.text=(position+1).toString()
                if(ans_status==1)
                    binding.layoutRoot.setBackgroundColor(context.getColor(R.color.green))
                else
                    binding.layoutRoot.setBackgroundColor(context.getColor(R.color.white))
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
        return data.ans_arr!!.size
    }
}
