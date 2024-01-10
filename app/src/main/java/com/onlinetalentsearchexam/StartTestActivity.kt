package com.onlinetalentsearchexam

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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
import com.onlinetalentsearchexam.adapter.QuestionAnswerAdapter
import com.onlinetalentsearchexam.adapter.PageIndicatorListener
import com.onlinetalentsearchexam.commons.Constants
import com.onlinetalentsearchexam.database.AnswerDAO
import com.onlinetalentsearchexam.model.DetailsItem
import com.onlinetalentsearchexam.model.QusdataItem
import com.onlinetalentsearchexam.model.SendDataModel
import com.onlinetalentsearchexam.model.start_test.AnsArr
import com.onlinetalentsearchexam.response.SaveQusResponse
import com.onlinetalentsearchexam.response.StartTestResponse
import com.onlinetalentsearchexam.utils.SnapHelperOneByOne
import com.onlinetalentsearchexam.utils.Utils
import com.onlinetalentsearchexam.viewmodel.SavequsViewModel
import com.onlinetalentsearchexam.viewmodel.StartTestViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.gson.Gson
import com.avision.commons.ApiInterface
import com.avision.commons.OnlineTalentSearchExam
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class StartTestActivity : AppCompatActivity(),QuestionAnswerAdapter.customButtonListener ,QuestionAnswerAdapter.customButtonListener1,
    QuestionAnswerAdapter.customButtonListener2, PageIndicatorListener,NoteClickInterface, NoteClickDeleteInterface{
    private lateinit var binding: ActivityStartTestBinding
    var doubleBounce: Sprite = DoubleBounce()
    lateinit var questionAnswerAdapter : QuestionAnswerAdapter
    private var questionAnswerLayoutManager: LinearLayoutManager? = null
    lateinit var pageIndicatorAdapter: PageIndicatorAdapter
    private var pageIndicatorLayoutManager: LinearLayoutManager? = null
    var finalSubmissionProgressBar:ProgressBar?=null
    var questionlist: ArrayList<HashMap<String, String>>? = ArrayList()
    var answerlist: ArrayList<HashMap<String, String>>? = ArrayList()
    lateinit var ansdao: AnswerDAO
    lateinit var _notesDao: NotesDao
    var isreload: Boolean=false
    //lateinit var toolbar: Toolbar
    lateinit var timer: TextView
    var newTimer: Long = 0
    lateinit var finish: Button
    lateinit var viewModal: NoteViewModal
    private var
            allNotes = ArrayList<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_start_test)
        window.statusBarColor=getColor(R.color.themecolor)
        ansdao= OnlineTalentSearchExam.getDB().getanswerdetails()!!
        _notesDao= NoteDatabase.getDatabase(this).getNotesDao()
        timer=binding.toolbar.timer
        finish=binding.toolbar.finish
        finish.setOnClickListener {
            finalCheckDialog()
        }


        viewModal = ViewModelProvider(
            this@StartTestActivity,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        viewModal.clearData()
        questionAnswerAdapter= QuestionAnswerAdapter(this@StartTestActivity,questionlist,answerlist,viewModal,allNotes)
        questionAnswerLayoutManager = LinearLayoutManager(
            this@StartTestActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
        linearSnapHelper.attachToRecyclerView(binding.recyclerview)
        questionAnswerAdapter.setCustomButtonListner(this@StartTestActivity)
        questionAnswerAdapter.setCustomButtonListner1(this@StartTestActivity)
        questionAnswerAdapter.setCustomButtonListner2(this@StartTestActivity)
        binding.recyclerview.layoutManager = questionAnswerLayoutManager
        binding.recyclerview.adapter=questionAnswerAdapter
        binding.recyclerview.isLayoutFrozen=true

        pageIndicatorAdapter= PageIndicatorAdapter(this,this)
        pageIndicatorLayoutManager= LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.pageIndicatorRecyclerView.apply {
            adapter=pageIndicatorAdapter
            layoutManager=pageIndicatorLayoutManager
        }

        countDown()
        //getdb()
        startexam()
    }

    fun getdb(){
        /*lifecycleScope.launch {
            var ans=Answer(0,"16308","4600","18394",0)
            ansdao.insert(ans)
        }*/
    }
    fun startexam(){
        binding.spinKit.visibility = View.VISIBLE
        binding.spinKit.setIndeterminateDrawable(doubleBounce)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("student_id", Paper.book().read("userid","").toString())
            .addFormDataPart("exam_id", Global.examid)
            .build()
        Log.d("TAGGG","student_id: "+Paper.book().read("userid","").toString() + " exam_id:"+Global.examid)
        if(OnlineTalentSearchExam.getInstance()!!.isNetworkAvailable()){
            val viewModel: StartTestViewModel = ViewModelProvider(this).get(StartTestViewModel::class.java)
            viewModel.start_test(requestBody)?.observe(this@StartTestActivity,object :
                Observer<StartTestResponse?> {
                override fun onChanged(apiResponse: StartTestResponse?) {

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
                                this@StartTestActivity
                            )

                        } else {

                            if (apiResponse.getPosts().status.equals("200")) {
                                var message : ArrayList<com.onlinetalentsearchexam.model.start_test.Message> = apiResponse.getPosts().message

                                for (i in 0 until message.size){

                                    var resultp = HashMap<String, String>()
                                    var answertp = HashMap<String, String>()
                                    resultp.put("question",message.get(i).question.toString())
                                    resultp.put("question_id",message.get(i).questionId.toString())
                                    resultp.put("exam_taken_id",message.get(i).examTakenId.toString())
                                    Global.exam_taken_id1=message.get(i).examTakenId.toString()
                                    questionlist?.add(resultp)

                                    var ansArr       : ArrayList<AnsArr> =message.get(i).ansArr
                                    if(ansArr.size>0){

                                        for(i in 0 until ansArr.size){
                                            if(i==0){
                                                answertp.put("answer0",ansArr.get(i).answer.toString())
                                                answertp.put("answer_id0",ansArr.get(i).answerId.toString())
                                            }
                                            else if(i==1){
                                                answertp.put("answer1",ansArr.get(i).answer.toString())
                                                answertp.put("answer_id1",ansArr.get(i).answerId.toString())
                                            }
                                            else if(i==2){
                                                answertp.put("answer2",ansArr.get(i).answer.toString())
                                                answertp.put("answer_id2",ansArr.get(i).answerId.toString())
                                            }
                                            else if(i==3){
                                                answertp.put("answer3",ansArr.get(i).answer.toString())
                                                answertp.put("answer_id3",ansArr.get(i).answerId.toString())
                                            }
                                        }
                                        answerlist?.add(answertp)
                                    }

                                    val addAllNote = Note(
                                        resultp["question"].toString(),
                                        Html.fromHtml(answertp["answer0"]).toString(),
                                        Html.fromHtml(answertp["answer1"]).toString(),
                                        Html.fromHtml(answertp["answer2"]).toString(),
                                        Html.fromHtml(answertp["answer3"]).toString(),
                                        resultp["question_id"].toString(),"Not selected", "0", "1")
                                    allNotes.add(addAllNote)
                                    viewModal.addNote(addAllNote)

                                }

                                viewModal.allNotes.observe(this@StartTestActivity, Observer { list ->
                                    list?.let {
                                        //on below line we are updating our list.
                                        questionAnswerAdapter.updateList(it)
                                        pageIndicatorAdapter.updateData(it)
                                    }
                                })
                               // adapter.notifyDataSetChanged()
                                Global.totalques= answerlist?.size ?:0
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
            Utils.showToast(resources.getString(R.string.no_internet),this@StartTestActivity)
        }
    }

    override fun onButtonClickListner(
        pos: Int,
        examid: String,
        questionid: String,
        answerid: String
    ) {
        nextButtonListener()

    }

    private fun finalCheckDialog() {
        val dialog = Dialog(this@StartTestActivity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        dialog.setContentView(R.layout.summary_dialog)
        val submitFinal:AppCompatButton
        lateinit var timerX: TextView
        lateinit var notesRV: RecyclerView
        submitFinal=dialog.findViewById(R.id.submitFinal)
        dialog.findViewById<ImageView>(R.id.backBtn).setOnClickListener { dialog.dismiss() }
        finalSubmissionProgressBar=dialog.findViewById(R.id.loadingProgress)
        timerX=dialog.findViewById(R.id.timerX)
        notesRV = dialog.findViewById(R.id.notesRV)
        val countDownTimer = object : CountDownTimer((60*Global.time*1000), 1000) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = newTimer / 1000

                timerX.setText(
                    "00" + ":" + String.format(
                        "%02d",
                        seconds / 60
                    ) + ":" + String.format("%02d", seconds % 60)
                )
            }

            override fun onFinish() {
                lifecycleScope.launch {
                    finishExam()
                }
            }
        }
        countDownTimer.start()
        notesRV.layoutManager = LinearLayoutManager(this@StartTestActivity)
        //on below line we are initializing our adapter class.
        val noteRVAdapter = NoteRVAdapter(this@StartTestActivity, viewModal, this@StartTestActivity)
        //on below line we are setting adapter to our recycler view.
        notesRV.adapter = noteRVAdapter
        //on below line we are initializing our view modal.

        //on below line we are calling all notes methof from our view modal class to observer the changes on list.
        viewModal.allNotes.observe(this@StartTestActivity, Observer { list ->
            list?.let {
                //on below line we are updating our list.
                noteRVAdapter.updateList(it)
            }
        })

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
            for (li in list){
                qusID=li.QusID
                selectedAnsId=li.SelAnsID
                qusdata.add(QusdataItem(qusID,selectedAnsId))
            }

            val dataToSend = SendDataModel(detail,qusdata)
            val gs = Gson()
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

    private fun countDown() {
        val countDownTimer = object : CountDownTimer((60*Global.time*1000), 1000) {
            override fun onTick(leftTimeInMilliseconds: Long) {
                val seconds: Long = leftTimeInMilliseconds / 1000
                timer.setText(
                    "00" + ":" + String.format(
                        "%02d",
                        seconds / 60
                    ) + ":" + String.format("%02d", seconds % 60)
                )
                newTimer=leftTimeInMilliseconds
            }

            override fun onFinish() {
                lifecycleScope.launch {
                    finishExam()
                }
            }
        }
        countDownTimer.start()
    }
    override fun onNoteClick(note: Note) {
        //opening a new intent and passing a data to it.
        val intent = Intent(this@StartTestActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("SelAnsID", note.SelAnsID)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
        this.finish()
    }
    override fun nextButtonListener() {
        val oldPosition = questionAnswerLayoutManager!!.findLastCompletelyVisibleItemPosition()
        val newPosition = oldPosition + 1
        if (oldPosition < questionAnswerAdapter.itemCount - 1){
            questionAnswerLayoutManager!!.scrollToPosition(newPosition)
            pageIndicatorAdapter.updatePosition(newPosition)
        } else
            finalCheckDialog()

    }

    override fun prevButtonListener() {
        val oldPosition=questionAnswerLayoutManager!!.findLastCompletelyVisibleItemPosition()
        val newPosition=oldPosition - 1
        if (oldPosition> 0) {
            questionAnswerLayoutManager!!.scrollToPosition(newPosition)
            pageIndicatorAdapter.updatePosition(newPosition)
        }
    }
    override fun onPressIndicator(pos: Int) {
        questionAnswerLayoutManager!!.scrollToPosition(pos)
        pageIndicatorAdapter.updatePosition(pos)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code here
            false
        } else super.onKeyDown(keyCode, event)
    }


    override fun onDeleteIconClick(note: Note) {
        TODO("Not yet implemented")
    }


}