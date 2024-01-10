package com.onlinetalentsearchexam

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityLoginBinding
import com.onlinetalentsearchexam.model.Info
import com.onlinetalentsearchexam.model.StudentCode
import com.onlinetalentsearchexam.response.ApiResponse
import com.onlinetalentsearchexam.utils.Utils
import com.onlinetalentsearchexam.viewmodel.LoginViewModel
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.material.button.MaterialButton
import com.avision.commons.OnlineTalentSearchExam
import com.onlinetalentsearchexam.maharaj.InstructionActivity
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import okhttp3.MultipartBody
import okhttp3.RequestBody


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var doubleBounce: Sprite = DoubleBounce()
    lateinit var id: String
    var firstarray : ArrayList<String> =ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_login)
        id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        binding.startExamBtn.setOnClickListener {
            //val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
            //startActivity(intent)
            if(binding.acname.text.toString().length==0){
                Utils.showToast(resources.getString(R.string.blank),this@LoginActivity)
            }
            else if(binding.passsword.text.toString().length==0){
                Utils.showToast(resources.getString(R.string.blank),this@LoginActivity)
            }else{
                login(binding.acname.text.toString(),binding.passsword.text.toString())
            }
        }
    }
    fun login(username: String, password : String){
        binding.spinKit.visibility = View.VISIBLE
        binding.spinKit.setIndeterminateDrawable(doubleBounce)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username",username)
            .addFormDataPart("password",password)
            .addFormDataPart("reg_id",id)
            .build()

        if(OnlineTalentSearchExam.getInstance()!!.isNetworkAvailable()){
            val viewModel: LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
            viewModel.login(requestBody)?.observe(this@LoginActivity,object :
                Observer<ApiResponse?> {
                override fun onChanged(apiResponse: ApiResponse?) {

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
                                this@LoginActivity
                            )

                        } else {

                            if (apiResponse.getPosts().status.equals("200")) {
                                var info    : Info?=apiResponse.getPosts().info

                                if (info != null) {
                                        if(info.childType=="m") {
                                            var studentCode: ArrayList<StudentCode> =
                                                info.studentCode
                                            showDialog(studentCode)
                                        }
                                        else {
                                            saveAndGotoNextPage(info.id.toString())
                                        }
//                                    }
//                                    else{
//                                        Paper.book().write("userid",info.id.toString())
//                                        val intent=Intent(this@LoginActivity,InstructionActivity::class.java)
//                                        startActivity(intent)
//                                        finish()
//                                    }
                                }


                            } else if (!apiResponse.getPosts().status.equals("200")) {
                                Utils.showToast(apiResponse.getPosts().message,this@LoginActivity)
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
            Utils.showToast(resources.getString(R.string.no_internet),this@LoginActivity)
        }
    }
    private fun showDialog(arr: ArrayList<StudentCode>) {
        val dialog = Dialog(this@LoginActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_layout)
        val submitButton = dialog.findViewById(R.id.startExamBtn) as MaterialButton
        val dropdown= dialog.findViewById(R.id.customerTextView1) as AppCompatAutoCompleteTextView
        var code: Int=0

        for(i in 0 until arr.size){
            firstarray.add(arr.get(i).FirstName.toString()+" "+arr.get(i).LastName.toString())
        }

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, firstarray)
        dropdown.setAdapter(adapter)
        dropdown.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, arg1: View?, pos: Int,
                id: Long
            ) {
                code=pos
            }
        })

        submitButton.setOnClickListener {
            dialog.dismiss()
            saveAndGotoNextPage(arr.get(code).StudentId.toString())
        }

        dialog.show()

    }
    private fun saveAndGotoNextPage( id:String){
        Paper.book().write("userid",id)
        Paper.book().write("login",1)
        val intent= Intent(this@LoginActivity,InstructionActivity::class.java)
        startActivity(intent)
        finish()
    }
}