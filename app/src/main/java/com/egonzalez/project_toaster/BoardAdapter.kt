package com.egonzalez.project_toaster

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import br.org.certi.jocd.board.MbedBoard

class BoardAdapter(presenter: ToasterMVP.Presenter) : RecyclerView.Adapter<BoardAdapter.ViewHolder>(){
    var mBoards = ArrayList<MbedBoard>()
    val TAG = BoardAdapter::class.toString()

    fun add(mbedBoard: MbedBoard){
        mBoards.add(mbedBoard)
    }
    fun clear(){
        mBoards.clear()
    }

    class ViewHolder(val baord_item: LinearLayout ):RecyclerView.ViewHolder(baord_item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):BoardAdapter.ViewHolder {
        var board_item = LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false) as LinearLayout
        return ViewHolder(board_item)
    }

    override fun onBindViewHolder(holder: BoardAdapter.ViewHolder, position: Int) {
        var textViewBoardName = holder.baord_item.findViewById(R.id.board_name) as TextView
        var textViewBoardUID = holder.baord_item.findViewById(R.id.board_uid) as TextView
        var boardName = mBoards.get(position).name
        var boardUID = mBoards.get(position).uniqueId
        textViewBoardName.setText(mBoards.get(position).name)
        textViewBoardUID.setText(mBoards.get(position).getUniqueId())
        Log.d(TAG, boardName+" "+boardUID)
    }

    override fun getItemCount(): Int {
        return mBoards.count()
    }



}
