package com.onlinetalentsearchexam

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityStartTestBinding
import com.onlinetalentsearchexam.adapter.PageIndicatorAdapter
import com.onlinetalentsearchexam.adapter.PageIndicatorListener
import com.onlinetalentsearchexam.commons.Constants
import com.onlinetalentsearchexam.maharaj.data.models.DetailsItem
import com.onlinetalentsearchexam.maharaj.data.models.QusdataItem
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitRequest
import com.onlinetalentsearchexam.utils.SnapHelperOneByOne
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.avision.commons.ApiInterface
import com.google.gson.Gson
import com.onlinetalentsearchexam.maharaj.FinalDialogQuestionAdapter
import com.onlinetalentsearchexam.maharaj.QuizQuestionAdapter
import com.onlinetalentsearchexam.maharaj.QuestionAnswerButtonsListener
import com.onlinetalentsearchexam.maharaj.data.models.Question
import com.onlinetalentsearchexam.maharaj.data.models.QuizAnsSubmitResponse
import com.onlinetalentsearchexam.maharaj.data.models.QuizQuestionResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.viewmodels.ExamViewModel
import com.visiabletech.avision.maharaj.core.extension.createPartFromString
import com.visiabletech.avision.maharaj.core.extension.observe
import com.visiabletech.avision.maharaj.core.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


@AndroidEntryPoint
class StartTestActivity : AppCompatActivity(), QuestionAnswerButtonsListener, PageIndicatorListener {
    private lateinit var binding: ActivityStartTestBinding
    private val viewModel: ExamViewModel by viewModels()
    lateinit var mainData:List<Question>
    var doubleBounce: Sprite = DoubleBounce()
    lateinit var quizQuestionAdapter : QuizQuestionAdapter
    private var quizLayoutManger: LinearLayoutManager? = null
    lateinit var pageIndicatorAdapter: PageIndicatorAdapter
    private var pageIndicatorLayoutManager: LinearLayoutManager? = null
    lateinit var finalDialogQuestionAdapter:FinalDialogQuestionAdapter
    var finalSubmissionProgressBar:ProgressBar?=null
     var finalDialogTimer: TextView?=null

    lateinit var finish: Button
    lateinit var viewModal: NoteViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_start_test)
        window.statusBarColor=getColor(R.color.themecolor)

        val studentId = Paper.book().read("userid","")
        val map: MutableMap<String, RequestBody> = mutableMapOf()
        map.apply {
            put("exam_id", createPartFromString(Global.examid))
            put("student_id", createPartFromString(studentId!!))
        }
        Log.d("TAG ","exam_id:"+Global.examid+"  student_id:"+studentId)
        viewModel.apply {
            observe(questionAnswerResponse,::onQuestionAnswerReceive)
            observe(quizAnsSubmitResponse,::onQuizAnsSubmitResponseReceive)

            getQuestionAnswer(map)
        }
        binding.apply {
            toolbar.finish.setOnClickListener {
                finalCheckDialog()
            }
            arrowLeft.setOnClickListener { onPrev() }
            rightArrow.setOnClickListener { onNext() }
            submitans.setOnClickListener { onNext() }
        }
        val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper.attachToRecyclerView(binding.recyclerview)
        quizLayoutManger= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        pageIndicatorLayoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        quizQuestionAdapter=QuizQuestionAdapter(this,this)
        binding.recyclerview.apply {
            layoutManager = quizLayoutManger
            adapter=quizQuestionAdapter
            isLayoutFrozen=true
        }

        pageIndicatorAdapter= PageIndicatorAdapter(this,this)
        binding.pageIndicatorRecyclerView.apply {
            adapter=pageIndicatorAdapter
            layoutManager=pageIndicatorLayoutManager
        }
        countDown()
        viewModal = ViewModelProvider(
            this@StartTestActivity,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        viewModal.clearData()
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Not Allowed!",Toast.LENGTH_SHORT).show()
    }

    private fun onQuestionAnswerReceive(state: State<QuizQuestionResponse>?) {
        when (state) {
            is State.ErrorState -> {
                Utility.performErrorState(this,state, "Failed")
            }
            is State.DataState -> {
                initDataToView(state.data)
            }
            else -> {}
        }
    }
    private fun onQuizAnsSubmitResponseReceive(state: State<QuizAnsSubmitResponse>?){
        when (state) {
            is State.ErrorState -> {
                Utility.performErrorState(this,state, "Failed")
                finalSubmissionProgressBar?.visibility=View.INVISIBLE
            }
            is State.DataState -> {
                val intent=Intent(this@StartTestActivity, ExamFinishedActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            else -> {}
        }
    }
    fun initDataToView(data: QuizQuestionResponse){
        mainData=data.message!!
        pageIndicatorAdapter.updateData(mainData)
        quizQuestionAdapter.updateData(mainData)
    }

    private fun finalCheckDialog() {
        val dialog = Dialog(this@StartTestActivity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        dialog.setContentView(R.layout.final_dialog)
        val submitFinal:AppCompatButton
        lateinit var questionRecyclerview: RecyclerView
        submitFinal=dialog.findViewById(R.id.submitFinal)
        dialog.findViewById<ImageView>(R.id.backBtn).setOnClickListener { dialog.dismiss() }
        finalSubmissionProgressBar=dialog.findViewById(R.id.loadingProgress)
        finalDialogTimer=dialog.findViewById(R.id.timerX)
        questionRecyclerview = dialog.findViewById(R.id.questionRecyclerview)

        questionRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StartTestActivity)
            finalDialogQuestionAdapter = FinalDialogQuestionAdapter(this@StartTestActivity,mainData,this@StartTestActivity)
            adapter = finalDialogQuestionAdapter
        }
        submitFinal.setOnClickListener {
            finalCheckWarning()
        }
        dialog.show()
    }

    private fun finalCheckWarning() {
        val builder1 = AlertDialog.Builder(this@StartTestActivity)
        builder1.setMessage("Do you want to submit exam?")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface, id: Int ->
            dialog.cancel()
            finishExam()
        }
        builder1.setNegativeButton(
            "No"
        ) { dialog: DialogInterface, id: Int -> dialog.cancel() }
        val alert = builder1.create()
        alert.show()
    }

    private fun finishExam() {
        finalSubmissionProgressBar?.visibility=View.VISIBLE
        Paper.book().write("examgiven",true)
        viewModal.allNotes.observe(this@StartTestActivity, Observer { list ->
            val retro = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.createWithScheduler(
                        Schedulers.io())) //added this line by sushovon
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retroInstance= retro.create(ApiInterface::class.java)
            val studentId = Paper.book().read("userid","")

            val detail= listOf(DetailsItem(studentId!!.toInt(),Global.exam_taken_id1))
            var qusID="";
            var selectedAnsId="";
            val qusdata= arrayListOf<QusdataItem>()
            for (li in mainData){
                qusID=li.question_id!!
                selectedAnsId=li.selectedAnsId
                qusdata.add(QusdataItem(qusID,selectedAnsId))
            }

            val dataToSend = QuizAnsSubmitRequest(detail,qusdata)
            val call = retroInstance.submitTest(dataToSend)
            call.enqueue(object : Callback<Void>{
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful){
                        val intent=Intent(this@StartTestActivity, ExamFinishedActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {
                    finalSubmissionProgressBar?.visibility=View.GONE
                    Log.d(localClassName,t.localizedMessage)
                    Toast.makeText(this@StartTestActivity,t.localizedMessage,Toast.LENGTH_LONG).show()
                }
            })

        })
    }
//    private fun finishExam() {
//        finalSubmissionProgressBar?.visibility=View.VISIBLE
//
//            val studentId = Paper.book().read("userid","")
//            val detail= listOf(DetailsItem(studentId!!.toInt(),Global.exam_taken_id1))
//            val qusdata= arrayListOf<QusdataItem>()
//            for (question in mainData){
//                qusdata.add(QusdataItem(question.question_id!!,question.selectedAnsId))
//            }
//
//            val quizAnsSubmitRequest = QuizAnsSubmitRequest(detail,qusdata)
//
//    viewModel.quizAnsSubmit(quizAnsSubmitRequest)
//    }

    private fun countDown() {
        val countDownTimer = object : CountDownTimer((60*Global.time*1000), 1000) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                val time= "00" + ":" + String.format(
                    "%02d",
                    seconds / 60
                ) + ":" + String.format("%02d", seconds % 60)
                binding.toolbar.timer.text=time
                finalDialogTimer?.text = time
            }

            override fun onFinish() {
                lifecycleScope.launch {
                    finishExam()
                }
            }
        }
        countDownTimer.start()
    }

    override fun onPressIndicator(pos: Int) {
        quizLayoutManger!!.scrollToPosition(pos)
        pageIndicatorAdapter.updatePosition(pos)
    }


     fun onPrev() {
        val oldPosition=quizLayoutManger!!.findLastCompletelyVisibleItemPosition()
        val newPosition=oldPosition - 1
        if (oldPosition> 0) {
            quizLayoutManger!!.scrollToPosition(newPosition)
            pageIndicatorAdapter.updatePosition(newPosition)
        }    }

     fun onNext() {
        val oldPosition = quizLayoutManger!!.findLastCompletelyVisibleItemPosition()
        val newPosition = oldPosition + 1
        if (oldPosition < quizQuestionAdapter.itemCount - 1){
            quizLayoutManger!!.scrollToPosition(newPosition)
            pageIndicatorAdapter.updatePosition(newPosition)
        } else
            finalCheckDialog()
    }

     override fun onAnswerClick(ansPos: Int, quesPos: Int, question:Question ) {
        mainData[quesPos].selectedAnsId=question.ans_arr!![ansPos].answer_id!!
         mainData[quesPos].selectedAns=question.ans_arr!![ansPos].answer!!
         quizQuestionAdapter.notifyItemChanged(quesPos)
        pageIndicatorAdapter.notifyItemChanged(quesPos)
         if(::finalDialogQuestionAdapter.isInitialized)
             finalDialogQuestionAdapter.notifyItemChanged(quesPos)


     }


}