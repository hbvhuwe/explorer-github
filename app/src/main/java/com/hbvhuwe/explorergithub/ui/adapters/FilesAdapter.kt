package com.hbvhuwe.explorergithub.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hbvhuwe.explorergithub.R
import com.hbvhuwe.explorergithub.model.File

class FilesAdapter(dataset: List<File>)
    : BaseAdapter<File, FilesAdapter.ViewHolder>(dataset, R.layout.file_layout) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
            ViewHolder(LayoutInflater.from(p0.context).inflate(layout, p0, false))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var file: File
        var fileName: TextView = itemView.findViewById(R.id.file_name) as TextView
        var fileIcon: ImageView = itemView.findViewById(R.id.file_icon) as ImageView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.file = dataset[position]
        holder.fileName.text = dataset[position].name
        if (holder.file.isDirectory()) {
            holder.fileIcon.setImageResource(R.drawable.ic_folder)
        } else {
            holder.fileIcon.setImageResource(R.drawable.ic_file)
        }
    }

}
