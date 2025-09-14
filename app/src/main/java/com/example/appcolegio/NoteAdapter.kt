package com.example.appcolegio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notesList: List<Note>,
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStudent: TextView = itemView.findViewById(R.id.tvStudent)
        val tvDetails: TextView = itemView.findViewById(R.id.tvDetails)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.tvStudent.text = note.student
        holder.tvDetails.text = "${note.grade} - ${note.subject}"
        holder.tvNote.text = "Nota: ${note.finalNote}"
        holder.itemView.setOnClickListener { onClick(note) }
    }

    override fun getItemCount(): Int = notesList.size
}