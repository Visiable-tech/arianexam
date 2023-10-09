package com.arianinstitute.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arianinstitute.Global
import com.arianinstitute.NoteViewModal
import com.arianinstitute.R
import com.arianinstitute.StartTestActivity
import com.arianinstitute.databinding.FragmentHomeBinding
import com.arianinstitute.model.examtest.Message
import com.arianinstitute.response.ExamResponse
import com.arianinstitute.utils.Utils
import com.arianinstitute.viewmodel.ExamViewModel
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.nctapplication.commons.MyApp
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MultipartBody
import okhttp3.RequestBody

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var rootView: View? = null
    private lateinit var binding: FragmentHomeBinding
    var doubleBounce: Sprite = DoubleBounce()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (rootView != null) {
            val parent = rootView!!.getParent() as ViewGroup
            if (parent != null) parent.removeView(rootView)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val rootView = binding.root
        retainInstance = true

        return rootView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        examtest()

        if(Paper.book().read("examgiven",false)==true){
            binding.submitButton.visibility=View.GONE
            binding.submitButton.setOnClickListener {

                val intent= Intent(activity, StartTestActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }else{
            binding.submitButton.setOnClickListener {
                val intent= Intent(activity, StartTestActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }


    }
    fun examtest(){
        binding.spinKit.visibility = View.VISIBLE
        binding.spinKit.setIndeterminateDrawable(doubleBounce)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("student_id", Paper.book().read("userid","").toString())
            .build()

        if(MyApp.getInstance()!!.isNetworkAvailable()){
            val viewModel: ExamViewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
            viewModel.examtest(requestBody)?.observe(requireActivity(),object :
                Observer<ExamResponse?> {
                override fun onChanged(apiResponse: ExamResponse?) {

                    if (apiResponse == null) {
                        // handle error here
                        binding.spinKit.visibility = View.GONE
                        return
                    }
                    if (apiResponse.error == null) {
                        // call is successful
                        binding.spinKit.setVisibility(View.GONE);
                        if (apiResponse.posts == null) {
                            binding.spinKit.visibility = View.GONE
                            Utils.showToast(
                                resources.getString(R.string.data_not_found),
                                activity
                            )

                        } else {

                            if (apiResponse.getPosts().status.equals("200")) {
                                var message : ArrayList<Message> = apiResponse.getPosts().message
                                for(i in 0 until message.size){

                                    if (MyApp.isValidContextForGlide(activity)) {
                                        // Load image via Glide lib using context
                                        Glide.with(requireActivity())
                                            .load(message.get(i).logo)
                                            //.placeholder(R.drawable.placeholderprofile)
                                            //.error(Glide.with(context!!).load(R.drawable.placeholderprofile))
                                            .into(binding.image)
                                    } else {
                                        Glide.with(requireActivity()).load(R.drawable.logo).into(binding.image)
                                    }
                                    Global.examid=message.get(i).examId.toString()
                                    Global.time=message.get(i).totTime.toString().toLong()
                                    Global.exam_taken_id=message.get(i).examTakenId.toString()

                                    val text =
                                        "<font color=#000000>${resources.getString(R.string.exam)}</font> <font color=#EA6B6A>${message.get(i).examName}</font>"
                                    binding.examname.setText(Html.fromHtml(text))

                                    val cls =
                                        "<font color=#000000>${resources.getString(R.string.cls)}</font> <font color=#EA6B6A>${message.get(i).classOrYear}</font>"
                                    binding.year.setText(Html.fromHtml(cls))

                                    val subject =
                                        "<font color=#000000>${resources.getString(R.string.subject)}</font> <font color=#EA6B6A>${message.get(i).subjectName}</font>"
                                    binding.subject.setText(Html.fromHtml(subject))

                                    val totqus =
                                        "<font color=#000000>${resources.getString(R.string.totqus)}</font> <font color=#EA6B6A>${message.get(i).totQus}</font>"
                                    binding.totqus.setText(Html.fromHtml(totqus))

                                    val duration =
                                        "<font color=#000000>${resources.getString(R.string.duration)}</font> <font color=#EA6B6A>${message.get(i).totTime}</font>"
                                    binding.duration.setText(Html.fromHtml(duration))

                                    val date =
                                        "<font color=#000000>${resources.getString(R.string.date)}</font> <font color=#EA6B6A>${message.get(i).examDate}</font>"
                                    binding.date.setText(Html.fromHtml(date))

                                    val marks =
                                        "<font color=#000000>${resources.getString(R.string.marks)}</font> <font color=#EA6B6A>${message.get(i).maxMarks}</font>"
                                    binding.totmarks.setText(Html.fromHtml(marks))

                                    val instruction =
                                        "<font color=#000000>${resources.getString(R.string.instruction)}</font> <font color=#EA6B6A>${message.get(i).instruction}</font>"
                                    binding.instruction.setText(Html.fromHtml(instruction))

                                }

                            } else if (!apiResponse.getPosts().status.equals("200")) {
                                //Utils.showToast(apiResponse.getPosts().message,this@InstructionActivity)
                            }


                        }
                    } else {
                        // call failed.
                        binding.spinKit.visibility = View.GONE
                        val e = apiResponse.error
                    }
                }

            })
        }else{
            Utils.showToast(resources.getString(R.string.no_internet),activity)
        }
    }
}