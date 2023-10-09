package com.arianinstitute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.arianinstitute.databinding.ActivityOptionBinding

class OptionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_option)
        binding.signin.setOnClickListener {
            val intent= Intent(this@OptionActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        /*binding.signup.setOnClickListener {
            val intent= Intent(this@OptionActivity,RegisterActivity::class.java)
            startActivity(intent)
        }*/
    }
}