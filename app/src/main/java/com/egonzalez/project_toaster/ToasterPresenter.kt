package com.egonzalez.project_toaster

import android.util.Log
import br.org.certi.jocd.board.MbedBoard
import certi.org.br.jocdandroidtestapp.AsyncFlashToolFlashBoard

class ToasterPresenter : ToasterMVP.Presenter {


    private val TAG: String = ToasterPresenter::class.toString()
    lateinit var mModel: ToasterMVP.Model
    lateinit var mView: ToasterMVP.View

//    override fun onListItemClicked(position:Int) {
//        /**
//         * Here should go the logic for toasting the microbit
//         */
//
//        var bDeviceConnected:Boolean = if(MbedBoard.getAllConnectedBoards() != null)  true else false
//        var files = mModel.getHexFiles()
//        var fileToToast = files.get(position)
//        Log.d(TAG, fileToToast.name)
//        if (bDeviceConnected)
//        {
//           return
//
//        }else{
//            mView.showMessage(R.string.no_device_connected)
//        }
//    }

//    override fun onListItemClicked(hexFilePosition: Int, currentBoard: Int) {
//
//    }

    override fun onListItemClicked(hexFilePath: String) {
        var currentBoardUID = mView.getCurrentBoardUId()
        Log.d(TAG, "uid is $currentBoardUID")
        if (!currentBoardUID.equals("")){

            var asyncFlashToolFlashBoard = AsyncFlashToolFlashBoard(mView, hexFilePath, currentBoardUID)
            asyncFlashToolFlashBoard.execute()
        }else{
            mView.showMessage(R.string.no_board_connected)
        }
    }

    override fun onFABClicked() {
        intLoadHexsFiles()
    }

    override fun loadHexsFiles() {
        intLoadHexsFiles()
    }

    override fun loadBoards() {
        var asyncFlashToolListDevices = AsyncFlashToolListDevices(mView, mModel)
        asyncFlashToolListDevices.execute()
    }


    private fun intLoadHexsFiles(){
        mView.startProgressBar()
        var files = mModel.getHexFiles()
        mView.showHexFiles(files)
        mView.endProgressBar()
    }


    override fun setView(view: ToasterMVP.View) {
        mView = view
        mModel =  ToasterModel()
        mModel.setRepository(ToasterRepo())
    }

}