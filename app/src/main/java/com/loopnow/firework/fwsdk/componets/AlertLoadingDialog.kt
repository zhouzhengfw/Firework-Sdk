package com.loopnow.firework.fwsdk.componets

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import com.loopnow.firework.R


class AlertLoadingDialog(val context: Context) : INetWorkLoading {


    fun getInstance(): AlertLoadingDialog = this

    private val dialog: AlertDialog by lazy {
        val dialog = AlertDialog.Builder(context, R.style.FwProgressDialog)
            .setView(R.layout.fw_loading_dialog_fragment).create()
        dialog.setOnShowListener {
            val lp = dialog.window?.attributes
            lp?.dimAmount = 0f
            dialog.window?.attributes = lp
        }

        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog
    }

    override fun startLoading() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun endLoading() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

}