package com.egonzalez.project_toaster

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat

class FileAdapter(presenter: ToasterMVP.Presenter) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {
    var TAG:String = FileAdapter::class.toString()
    var  mFiles:ArrayList<File> = ArrayList<File>()
    var mPresenter = presenter
    fun add(file: File){
        mFiles.add(file)
    }

    fun clear(){
        mFiles.clear()
    }


    class ViewHolder(val hex_item: LinearLayout) : RecyclerView.ViewHolder(hex_item)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileAdapter.ViewHolder {
        Log.d(TAG, "onCreateViewHolder");
        var hex_item = LayoutInflater.from(parent.context).inflate(R.layout.hex_item, parent, false) as LinearLayout

        return ViewHolder(hex_item)
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    override fun onBindViewHolder(holder: FileAdapter.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder");

        holder.hex_item.setOnClickListener(View.OnClickListener {
            /**
             * Is not elegant and brokes MVPm should research how to connect recyclerview to mainactivity following mvp pattern
             */
            var file_path = mFiles.get(position).canonicalPath
            mPresenter.onListItemClicked(file_path)
        })
        var textViewHexName = holder.hex_item.findViewById(R.id.hex_name) as TextView
        var textViewHexPath = holder.hex_item.findViewById(R.id.hex_path) as TextView
        var textViewHexDate = holder.hex_item.findViewById(R.id.hex_date) as TextView

        var file = mFiles[position]
        textViewHexName.text = file.name
        textViewHexPath.text = file.canonicalPath
        textViewHexDate.text = getDateTime(file.lastModified())
    }


    private fun getDateTime(l: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(l)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }


}