package com.egonzalez.project_toaster

interface IFlashDialog {

    fun startDialog()
    fun setProgress(progress: Int?)
    fun endDialog()
}