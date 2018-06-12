package com.egonzalez.project_toaster

import br.org.certi.jocd.board.MbedBoard
import java.io.File
import java.util.HashMap

class ToasterModel : ToasterMVP.Model {
    lateinit var mRepository:ToasterMVP.Repository;

    override fun getBoards():List<MbedBoard> {
        return mRepository.getBoards();
    }


    override fun setRepository(repo: ToasterMVP.Repository) {
        mRepository = repo
    }

    constructor()

    override fun getHexFiles(): ArrayList<File> {
       return mRepository.loadHexFilesFromDisc()
    }




}