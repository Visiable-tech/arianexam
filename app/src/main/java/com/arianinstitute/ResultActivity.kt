package com.arianinstitute

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arianinstitute.databinding.ActivityResultBinding
import com.arianinstitute.model.viewresult.Message
import com.arianinstitute.response.ViewResultResponse
import com.arianinstitute.utils.Utils
import com.arianinstitute.viewmodel.ViewResultViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.nctapplication.commons.MyApp
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
//import ir.mahozad.android.PieChart
import okhttp3.MultipartBody
import okhttp3.RequestBody

@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    var doubleBounce: Sprite = DoubleBounce()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_result)
        viewresult()
        binding.homeButton.setOnClickListener {
            val intent=Intent(this@ResultActivity,DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        //setUpSelectionPieChart()

    }
    fun viewresult(){
        Paper.book().write("examgiven",true)
        binding.spinKit.visibility = View.VISIBLE
        binding.spinKit.setIndeterminateDrawable(doubleBounce)

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("student_id", Paper.book().read("userid","").toString())
            .addFormDataPart("exam_taken_id", Global.exam_taken_id)
            .build()

        if(MyApp.getInstance()!!.isNetworkAvailable()){
            val viewModel: ViewResultViewModel = ViewModelProvider(this).get(ViewResultViewModel::class.java)
            viewModel.view_result(requestBody)?.observe(this@ResultActivity,object :
                Observer<ViewResultResponse?> {
                override fun onChanged(apiResponse: ViewResultResponse?) {

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
                                this@ResultActivity
                            )

                        } else {

                            if (apiResponse.getPosts().status.equals("200")) {
                                var message : Message?= apiResponse.getPosts().message

                                var percentage:Float = (message?.correct!!.toFloat()/ Global.totalques.toFloat()) * 100.0f
                                var percentage1:Float = (message?.incorrect!!.toFloat() / Global.totalques.toFloat()) * 100.0f


                                if(message?.correct==0){
                                    /*binding.pieChart.slices = listOf(
                                        PieChart.Slice(0.99f, resources.getColor(R.color.wrongred)),
                                    )*/
                                    val dataArray = ArrayList<PieEntry>()
                                    val colorSet = ArrayList<Int>()
                                    dataArray.add(PieEntry(100f))
                                    val dataSet = PieDataSet(dataArray, "")
                                    dataSet.valueTextSize=20f
                                    dataSet.valueTextColor=Color.WHITE
                                    colorSet.add(resources.getColor(R.color.wrongred))  //red
                                    dataSet.setColors(colorSet)
                                    val data = PieData(dataSet)
                                    binding.pieChart.description.text = "Analysis Report"
                                    binding.pieChart.description.textSize = 20f
                                    binding.pieChart.data = data
                                    binding.pieChart.centerTextRadiusPercent = 0f
                                    binding.pieChart.isDrawHoleEnabled = true
                                    binding.pieChart.legend.isEnabled = false
                                    binding.pieChart.description.isEnabled = true
                                    binding.pieChart.invalidate();
                                }else{
                                    /*binding.pieChart.slices = listOf(
                                        PieChart.Slice(percentage/100, resources.getColor(R.color.green)),
                                        PieChart.Slice(percentage1/100, resources.getColor(R.color.wrongred)),
                                    )*/
                                    pie1(percentage,percentage1)
                                }


                                binding.correctans.text=resources.getString(R.string.correct)+" "+message?.correct
                                binding.wrongtans.text=resources.getString(R.string.wrong)+" "+message?.incorrect

                            } else if (!apiResponse.getPosts().status.equals("200")) {
                                Utils.showToast("error",this@ResultActivity)
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
            Utils.showToast(resources.getString(R.string.no_internet),this@ResultActivity)
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent=Intent(this@ResultActivity,DashboardActivity::class.java)
            startActivity(intent)
            finish()
            false
        } else super.onKeyDown(keyCode, event)
    }
    fun pie1(percentage: Float, percentage1: Float){
        val dataArray = ArrayList<PieEntry>()
        val colorSet = ArrayList<Int>()
        dataArray.add(PieEntry(percentage))
        dataArray.add(PieEntry(percentage1))
        val dataSet = PieDataSet(dataArray, "")
        dataSet.valueTextSize=20f
        dataSet.valueTextColor=Color.WHITE
        colorSet.add(resources.getColor(R.color.green))
        colorSet.add(resources.getColor(R.color.wrongred))
        dataSet.setColors(colorSet)
        val data = PieData(dataSet)
        binding.pieChart.description.text = "Analysis Report"
        binding.pieChart.description.textSize = 20f
        binding.pieChart.data = data
        binding.pieChart.centerTextRadiusPercent = 0f
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.description.isEnabled = true
        binding.pieChart.invalidate()
    }

}