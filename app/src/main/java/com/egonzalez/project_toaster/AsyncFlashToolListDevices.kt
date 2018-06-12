package com.egonzalez.project_toaster

import android.os.AsyncTask
import br.org.certi.jocd.board.MbedBoard

class AsyncFlashToolListDevices(val mView: ToasterMVP.View, val mModel: ToasterMVP.Model): AsyncTask<String, String, List<MbedBoard>>() {

    override fun onPreExecute() {
        mView.startProgressBar()
    }
    override fun doInBackground(vararg params: String?): List<MbedBoard> {
        return mModel.getBoards()
    }

    override fun onPostExecute(result: List<MbedBoard>) {
        mView.showBoards(result)
        mView.endProgressBar()
    }
}