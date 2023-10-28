package com.onlinetalentsearchexam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityRegisterBinding

import com.google.android.material.datepicker.MaterialDatePicker


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_register)
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        val materialDatePicker = materialDateBuilder.build()
        binding.dob.setOnClickListener {
            materialDatePicker.show(supportFragmentManager, "DATE_PICKER")
           /* MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText("Select date of birth")
                .build()
                .show(supportFragmentManager, "DATE_PICKER")*/
        }
        materialDatePicker.addOnPositiveButtonClickListener {
            //mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.headerText)
            binding.dob.setText(materialDatePicker.headerText).toString()
            Log.e("arian","Selected Date is : " + materialDatePicker.headerText)

        }

        binding.startExamBtn.setOnClickListener {
            val intent= Intent(this@RegisterActivity,DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}