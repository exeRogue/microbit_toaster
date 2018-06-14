package com.egonzalez.project_toaster

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView

class FlashDialog(val mContext : Context) : IFlashDialog{

    lateinit var progressFlashBar:ProgressBar
    lateinit var textViewProgress:TextView
    var dialog = Dialog(mContext)

    override fun startDialog() {

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.flash_progress)

            progressFlashBar = dialog.findViewById(R.id.progress_flash_bar)
            textViewProgress = dialog.findViewById(R.id.progres_text_view)
            dialog.show()
            progressFlashBar.visibility= View.VISIBLE
    }

    override fun setProgress(progress: Int?) {
        var auxProgress =  progress?:0
        progressFlashBar.progress = auxProgress
        textViewProgress.text = "$auxProgress%"
    }

    override fun endDialog() {
        progressFlashBar.visibility= View.GONE
        dialog.dismiss()
    }
}