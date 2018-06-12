package com.egonzalez.project_toaster

import br.org.certi.jocd.board.MbedBoard
import java.io.File
import kotlin.collections.HashMap

interface ToasterMVP {
    interface  View
    {
        fun startProgressBar()
        fun endProgressBar()
        fun startProgressBarToast()
        fun setProgressBarToastProgress(progress:Int?)
        fun endProgressBarToast()
        fun showHexFiles(files: ArrayList<File>)
        fun showMessage(messageId:Int)
        fun showMessage(message:String)
        fun showBoards(mBoards: List<MbedBoard>)
        fun getCurrentBoardUId():String
    }

    interface Presenter
    {
        fun setView(view :View)
        fun loadHexsFiles()
        fun loadBoards()
        fun onFABClicked()
        fun onListItemClicked(hexFilePath:String)
    }

    interface Model
    {
        fun setRepository(repo: Repository)
        fun getHexFiles():ArrayList<File>
        fun getBoards():  List<MbedBoard>
    }

    interface Repository
    {
        fun loadHexFilesFromDisc():ArrayList<File>
        fun getBoards(): List<MbedBoard>
    }
}