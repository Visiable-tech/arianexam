package com.arianinstitute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.arianinstitute.databinding.ActivityMainBinding
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=getColor(R.color.white)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            if(Paper.book().read("login",0)==1){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)
    }
}