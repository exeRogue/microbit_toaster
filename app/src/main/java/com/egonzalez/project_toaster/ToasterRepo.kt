package com.egonzalez.project_toaster

import android.os.Environment
import br.org.certi.jocd.Jocd
import br.org.certi.jocd.board.MbedBoard
import java.io.File
import java.util.HashMap

class ToasterRepo:ToasterMVP.Repository {

    override fun getBoards():  List<MbedBoard>{
        return MbedBoard.getAllConnectedBoards()
    }

    override fun loadHexFilesFromDisc(): ArrayList<File> {
        var file = Environment.getExternalStorageDirectory()
        return getListFiles(file)
    }

    private fun getListFiles(file: File): ArrayList<File> {
        var inFiles: ArrayList<File> = ArrayList<File>()
        file.walkBottomUp().forEach {
            if (it.name.endsWith(".hex")){
                inFiles.add(it)
            }
        }

        return inFiles
    }
}