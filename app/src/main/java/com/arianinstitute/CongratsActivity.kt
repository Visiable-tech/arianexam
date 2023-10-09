package com.arianinstitute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.Lottie
import com.arianinstitute.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityCongratsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        actionBar?.hide()
        window.statusBarColor=getColor(R.color.white)
        mBinding.lottie1.playAnimation()
        mBinding.lottie2.repeatCount=10;mBinding.lottie1.repeatCount=10
        mBinding.lottie2.playAnimation()
//        Handler().postDelayed(Runnable {
//            closeApp()
//        },5000)

    }

    override fun onBackPressed() {
        finishAffinity()
    }

//    fun closeApp(){
//        this.finish();
//        System.exit(0);
//    }

}