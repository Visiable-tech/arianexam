package com.onlinetalentsearchexam.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arianinstitute.R
import com.arianinstitute.databinding.FragmentHomeBinding
import com.onlinetalentsearchexam.Global
import com.onlinetalentsearchexam.StartTestActivity
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.onlinetalentsearchexam.maharaj.ResultActivity
import com.onlinetalentsearchexam.maharaj.data.models.QuizDetailResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.createPartFromString
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.RequestBody

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var rootView: View? = null
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ExamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {
            val parent = rootView!!.getParent() as ViewGroup
            if (parent != null) parent.removeView(rootView)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val rootView = binding.root
        retainInstance = true

        return rootView
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ExamViewModel::class.java)

        val map: MutableMap<String, RequestBody> = mutableMapOf()
        val studentId = Paper.book().read("userid","")
        map.apply {
            put("student_id", createPartFromString(studentId!!))
        }
        viewModel.apply {
            observe(quizDetailResponse,::onQuizDetailReceive)
            getQuizDetail(map)
        }
    }

    private fun onQuizDetailReceive(state: State<QuizDetailResponse>?) {
        when (state) {
            is State.ErrorState -> {
                Utility.performErrorState(requireActivity(),state, "Failed")
            }
            is State.DataState -> {
                initDataToView(state.data)
            }
            else -> {}
        }
    }
    fun initDataToView(data:QuizDetailResponse){

        if(data.status.equals("200")){
            val quizDetail= data.message!!.get(0)

            Log.d("TAGGG","student_id:"+Paper.book().read("userid",""))
            Log.d("TAGGG","exam taken id "+quizDetail.exam_taken_id)
            Log.d("TAGGG","exam id "+quizDetail.exam_id)

            Global.examid=quizDetail.exam_id.toString()
            Global.time=quizDetail.tot_time.toString().toLong()
            Global.exam_taken_id1=quizDetail.exam_taken_id.toString()
            Global.status=quizDetail.status.toString()
            Global.statusText=quizDetail.status_text.toString()
            initExamBtn()
            val text =
                "<font color=#000000>${resources.getString(R.string.exam)}</font> <font color=#EA6B6A>${quizDetail.exam_name}</font>"
            binding.examname.setText(Html.fromHtml(text))

            val cls =
                "<font color=#000000>${resources.getString(R.string.cls)}</font> <font color=#EA6B6A>${quizDetail.class_or_year}</font>"
            binding.year.setText(Html.fromHtml(cls))

            val subject =
                "<font color=#000000>${resources.getString(R.string.subject)}</font> <font color=#EA6B6A>${quizDetail.subject_name}</font>"
            binding.subject.setText(Html.fromHtml(subject))

            val totqus =
                "<font color=#000000>${resources.getString(R.string.totqus)}</font> <font color=#EA6B6A>${quizDetail.tot_qus}</font>"
            binding.totqus.setText(Html.fromHtml(totqus))

            val duration =
                "<font color=#000000>${resources.getString(R.string.duration)}</font> <font color=#EA6B6A>${quizDetail.tot_time}</font>"
            binding.duration.setText(Html.fromHtml(duration))

            val date =
                "<font color=#000000>${resources.getString(R.string.date)}</font> <font color=#EA6B6A>${quizDetail.exam_date}</font>"
            binding.date.setText(Html.fromHtml(date))

            val marks =
                "<font color=#000000>${resources.getString(R.string.marks)}</font> <font color=#EA6B6A>${quizDetail.max_marks}</font>"
            binding.totmarks.setText(Html.fromHtml(marks))

            val instruction =
                "<font color=#EA6B6A>${quizDetail.instruction}</font>"
            binding.instruction.setText(Html.fromHtml(instruction))
        }else{
            binding.instructionHeading.text="No exam found!"
            binding.startExamBtn.visibility=View.GONE
        }


    }
    fun initExamBtn(){
        binding.startExamBtn.text=Global.statusText
        binding.startExamBtn.setOnClickListener {
            if(Global.status=="0"){
                val intent= Intent(activity, StartTestActivity::class.java)
                startActivity(intent)
                requireActivity().finish(

                )
            }else if(Global.status=="3"){
                startActivity(Intent(requireContext(), ResultActivity::class.java))
            }else{
                Toast.makeText(context,"Not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}