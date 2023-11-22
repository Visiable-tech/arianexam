
package com.visiabletech.avision.maharaj.core.util

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.onlinetalentsearchexam.maharaj.retrofit.State
import com.onlinetalentsearchexam.maharaj.view.CommonDialogs


object Utility {

    fun isFieldsEmpty(array: ArrayList<EditText>): Boolean {
        for(a in array){
            if(a.text!!.trim().isEmpty()){
                return true
            }
        }
        return false
    }
    fun performErrorState(context: AppCompatActivity, state: State.ErrorState, msg: String) {
        Log.i(context.localClassName, state.exception.errorMessage)
        Log.d("TAG","state.error"+state.exception.errorMessage + state.exception.errorCode)
        val dialogs= CommonDialogs()
        dialogs.showDialogWithOneButton(
            context,msg,state.exception.errorMessage,"OK",
            { dialog, _ ->
                dialog.dismiss()
            }
        )
    }
      fun disableFocus(arr:ArrayList<View>){
        for(view in arr){
            view.isFocusable=false
        }
    }
    fun enableFocus(arr:ArrayList<View>){
        for(view in arr){
            view.isFocusableInTouchMode=true
        }
    }

}