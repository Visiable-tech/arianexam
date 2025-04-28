package com.onlinetalentsearchexam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arianinstitute.R
import com.arianinstitute.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityCongratsBinding
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

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        finishAffinity()
    }

//    fun closeApp(){
//        this.finish();
//        System.exit(0);
//    }

}