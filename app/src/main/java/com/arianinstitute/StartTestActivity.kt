package com.arianinstitute

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
import android.widget.TextView
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
import com.arianinstitute.adapter.PageIndicatorAdapter
import com.arianinstitute.adapter.QuestionAnswerAdapter
import com.arianinstitute.adapter.PageIndicatorListener
import com.arianinstitute.commons.Constants
import com.arianinstitute.database.AnswerDAO
import com.arianinstitute.databinding.ActivityStartTestBinding
import com.arianinstitute.model.DetailsItem
import com.arianinstitute.model.QusdataItem
import com.arianinstitute.model.SendDataModel
import com.arianinstitute.model.start_test.AnsArr
import com.arianinstitute.response.SaveQusResponse
import com.arianinstitute.response.StartTestResponse
import com.arianinstitute.response.SubmittestResponse
import com.arianinstitute.utils.SnapHelperOneByOne
import com.arianinstitute.utils.Utils
import com.arianinstitute.viewmodel.SavequsViewModel
import com.arianinstitute.viewmodel.StartTestViewModel
import com.arianinstitute.viewmodel.SubmittestViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.gson.Gson
import com.nctapplication.commons.ApiInterface
import com.nctapplication.commons.MyApp
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
        ansdao= MyApp.getDB().getanswerdetails()!!
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

        if(MyApp.getInstance()!!.isNetworkAvailable()){
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
                                var message : ArrayList<com.arianinstitute.model.start_test.Message> = apiResponse.getPosts().message

                                for (i in 0 until message.size){

                                    var resultp = HashMap<String, String>()
                                    var answertp = HashMap<String, String>()
                                    resultp.put("question",message.get(i).question.toString())
                                    resultp.put("question_id",message.get(i).questionId.toString())
                                    resultp.put("exam_taken_id",message.get(i).examTakenId.toString())
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
                                        Html.fromHtml(resultp["question"]).toString(),
                                        Html.fromHtml(answertp["answer0"]).toString(),
                                        Html.fromHtml(answertp["answer1"]).toString(),
                                        Html.fromHtml(answertp["answer2"]).toString(),
                                        Html.fromHtml(answertp["answer3"]).toString(),
                                        Html.fromHtml(resultp["question_id"]).toString(),"Not selected", "301", "1")
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
        //Log.e("sushovon", examid+'\n'+questionid+'\n'+answerid)
        lifecycleScope.launch {
            isreload=true
            submitanswer(pos,questionid,answerid,examid)
        }
        nextButtonListener()

    }
    fun submitanswer(pos:Int,question_id: String, answer_id :String, exam_taken_id: String){
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("question_id", question_id)
            .addFormDataPart("answer_id", answer_id)
            .addFormDataPart("exam_taken_id", exam_taken_id)
            .build()

        if(MyApp.getInstance()!!.isNetworkAvailable()){
            val viewModel: SavequsViewModel = ViewModelProvider(this).get(SavequsViewModel::class.java)
            viewModel.save_question(requestBody)?.observe(this@StartTestActivity,object :
                Observer<SaveQusResponse?> {
                @SuppressLint("SuspiciousIndentation")
                override fun onChanged(apiResponse: SaveQusResponse?) {
                    if(isreload){
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
                                    isreload=false
                                   // Utils.showToast("Answer submitted",this@StartTestActivity)
                                    if(pos== answerlist!!.size){
                                        finalCheckDialog()
                                    }
                                    //Log.e("sushovon", apiResponse.getPosts().message.toString())
                                } else if (!apiResponse.getPosts().status.equals("200")) {
                                    isreload=false
                                    //Utils.showToast(apiResponse.getPosts().message,this@InstructionActivity)
                                }
                            }
                        } else {
                            // call failed.
                            binding.spinKit.visibility = View.GONE
                            val e = apiResponse.error
                        }
                    }

                }

            })
        }else{
            Utils.showToast(resources.getString(R.string.no_internet),this@StartTestActivity)
        }
    }

    private fun finalCheckDialog() {
        val dialog = Dialog(this@StartTestActivity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        dialog.setContentView(R.layout.summary_dialog)
        val submitFinal:AppCompatButton
        lateinit var timerX: TextView
        lateinit var notesRV: RecyclerView
        submitFinal=dialog.findViewById(R.id.submitFinal)
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
        builder1.setMessage("Do you want to quit exam?")
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
        Paper.book().write("examgiven",true)
        viewModal.allNotes.observe(this@StartTestActivity, Observer { list ->

            val gson = Gson()
            val jsonArrayString = gson.toJson(list)
            val secondPart = jsonArrayString
            val firstPart="""[{
                                                    "student_id" : ${Paper.book().read("userid","").toString()},
                                                    "test_taken_id" : ${Global.examid},
                                                    }]""".trimMargin()
            Log.d("TAG", "onChanged: jsonVal"+secondPart)
            // submit_test_final(firstPart,secondPart)

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
            val detail = listOf(studentId?.let { it1 ->
                DetailsItem(
                    it1.toInt(),Global.examid)
            })
            // val qusdata=ArrayList<Note>
            var qusID="";
            var selectedAnsId="";
            Log.d("TAG", "onChanged:lengtOfarr "+list.size)
            for (i in 1 until list.size){
                qusID=list.get(i).QusID
                selectedAnsId=list.get(i).SelAnsID
            }
            val qusdata = listOf(
                QusdataItem(qusID,selectedAnsId,"","","",
                    "","",0,"","")
            )
            val dataToSend = SendDataModel(detail,qusdata)
            val call = retroInstance.submitTest(dataToSend)

            call.enqueue(object : Callback<Void>{
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful){
                        val intent=Intent(this@StartTestActivity, CongratsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    t: Throwable
                ) {
                    TODO("Not yet implemented")
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
        }
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