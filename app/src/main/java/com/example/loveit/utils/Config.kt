package com.example.loveit.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.loveit.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {
    var dialog: AlertDialog?=null

    fun showDialog(context: Context)
    {
        dialog=MaterialAlertDialogBuilder(context)
            .setView(R.layout.loading_layout)
            .setCancelable(false)
            .create()

        dialog!!.show()

    }

    fun hideDialog()
    {
        dialog!!.dismiss()
    }
}