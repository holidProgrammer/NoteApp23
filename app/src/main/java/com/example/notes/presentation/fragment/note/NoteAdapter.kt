package com.example.notes.presentation.fragment.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.damain.model.Note
import com.example.notes.databinding.ItemNoteBinding

class NoteAdapter(
    private val onItemLongClick: (Note) -> Unit,
    private val onItemClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var mlist = mutableListOf<Note>()

    fun setData(list: List<Note>) {
        mlist.clear()
        mlist.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(note: Note) = with(binding) {
            txtTitle.text = note.title
            txtDescription.text = note.description
            itemView.setOnLongClickListener {
                onItemLongClick.invoke(note)
                return@setOnLongClickListener true
            }
            itemView.setOnClickListener {
                onItemClick.invoke(note)
                return@setOnClickListener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(mlist[position])
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    fun delete(note: Note) {
        val position = mlist.indexOf(note)
        mlist.removeAt(position)
        notifyItemRemoved(position)
    }

    fun edit(note: Note) {
        val position = mlist.indexOfFirst { it.createdAt == note.createdAt }
        mlist[position] = note
        notifyItemChanged(position)
    }
}