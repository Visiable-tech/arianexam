package com.onlinetalentsearchexam.maharaj.view

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.arianinstitute.R
import javax.inject.Inject

class CommonDialogs @Inject constructor(){

     var dialog: AlertDialog? = null

    fun showDialogWithTwoButtons(
        context: Context?,
        titleId: Int,
        messageId: Int,
        positiveButtonText: Int,
        negativeButtonText: Int,
        positiveButtonClickListener: DialogInterface.OnClickListener,
        negativeButtonClickListener: DialogInterface.OnClickListener
    ) {

        if (context == null)
            return

        val alertErrorBuilder = AlertDialog.Builder(context)
        alertErrorBuilder.setMessage(messageId)
            .setCancelable(false)
            .setPositiveButton(positiveButtonText, positiveButtonClickListener)
            .setNegativeButton(negativeButtonText, negativeButtonClickListener)

        if (titleId != 0) {
            alertErrorBuilder.setTitle(titleId)
        }

        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        dialog = alertErrorBuilder.create()
        dialog?.show()
        setDialogButtonColor(context)
    }

        fun removeDialog() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

     fun setDialogButtonColor(context: Context) {
        dialog?.let {
            if (it.getButton(DialogInterface.BUTTON_POSITIVE) != null) {
                it.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.themecolor))
            }
            if (it.getButton(DialogInterface.BUTTON_NEGATIVE) != null) {
                it.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.themecolor))
            }

            if (it.getButton(DialogInterface.BUTTON_NEUTRAL) != null) {
                it.getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(context, R.color.themecolor))
            }
        }
    }
    fun showDialogWithOneButton(
        context: Context?,
        title: String,
        message: String,
        positiveButtonText: String,
        positiveButtonClickListener: DialogInterface.OnClickListener,
    ) {

        if (context == null)
            return

        val alertErrorBuilder = AlertDialog.Builder(context)
        alertErrorBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveButtonText, positiveButtonClickListener)
        if (!title.isNullOrEmpty()) {
            alertErrorBuilder.setTitle(title)
        }

        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        dialog = alertErrorBuilder.create()
        dialog?.show()
        setDialogButtonColor(context)
    }
    fun dialogBuild(context: Context,viewBinding: ViewBinding): AlertDialog {
        var dialogBuilder= AlertDialog.Builder(context)
        dialogBuilder.setView(viewBinding.root)
        dialogBuilder.setCancelable(false)
        val dialog=dialogBuilder.create()
        dialog.show()
        return dialog
    }
    fun dialogBuildFullScreen(context: Context,viewBinding: ViewBinding): AlertDialog {
        var dialogBuilder= AlertDialog.Builder(context,android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        dialogBuilder.setView(viewBinding.root)
        dialogBuilder.setCancelable(false)
        val dialog=dialogBuilder.create()
        dialog.show()
        return dialog
    }
}