package com.example.notesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.database.Notes
import com.example.notesapp.home.HomeFragmentDirections
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesApapter : RecyclerView.Adapter<NotesApapter.viewHolder>() {

    //data to be display by Recyclerview
    var data = listOf<Notes>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    fun filtering(newFilteredLis: ArrayList<Notes>) {
        data = newFilteredLis
        notifyDataSetChanged()
    }



    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val sub: TextView = itemView.findViewById(R.id.subTitle)
        val note: TextView = itemView.findViewById(R.id.note)
        val date: TextView = itemView.findViewById(R.id.time)
        val rating: ImageView = itemView.findViewById(R.id.priority)
        val edit: ImageView = itemView.findViewById(R.id.edit_button)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.item_notes, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = data[position]
        //date formatter
        var simple = SimpleDateFormat("dd MMM yyyy  HH:mm:ss")
        var result = Date(item.currentTime)
        holder.title.text = item.title
        holder.sub.text = item.subTitle
        holder.note.text = item.note
        holder.date.text =simple.format(result)
        holder.rating.setImageResource(
            when (item.priority) {
                "low" -> R.drawable.one_star
                "medium" -> R.drawable.two_star
                else -> R.drawable.three_star
            }
        )

        //edit notes button
        holder.edit.setOnClickListener {
            it.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(item))
        }
    }

    //total number of items/notess
    override fun getItemCount() = data.size
}


