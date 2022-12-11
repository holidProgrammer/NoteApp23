package com.example.notes.presentation.fragment.note

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.notes.R
import com.example.notes.core.BaseFragment
import com.example.notes.damain.model.Note
import com.example.notes.databinding.FragmentNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : BaseFragment(R.layout.fragment_note) {

    private val noteAdapter by lazy { NoteAdapter(this::onItemLongClick, this::onItemClick) }
    private val notesAdapter by lazy {
        NoteRecyclerAdapter(
            this::onItemLongClick,
            this::onItemClick
        )
    }
    private val viewModel by viewModels<NoteListViewModel>()
    private val binding by viewBinding(FragmentNoteBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupRequest()
        setupObservers()
        setupClickListeners()
    }

    private fun initialize() = with(binding) {
        RecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        RecyclerView.adapter = noteAdapter
    }

    private fun setupRequest() {
        viewModel.getAllNotes()
    }

    private fun setupObservers() {

        viewModel.getAllNotesState.collectState(
            onLoading = { },
            onError = { },
            onSuccess = { data ->
                noteAdapter.setData(data)
                Toast.makeText(requireContext(), "Notes updated successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        viewModel.deleteNoteState.collectState(
            onLoading = { },
            onError = { },
            onSuccess = {
                Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupClickListeners() {
        binding.btnFab.setOnClickListener {
            findNavController().navigate(R.id.addNoteFragment)
        }
    }

    private fun onItemLongClick(note: Note) {
        viewModel.deleteNote(note)
        noteAdapter.delete(note)
    }

    private fun onItemClick(note: Note) {
        val bundle = Bundle()
        bundle.putSerializable(ARG_EDIT_NOTE, note)
        findNavController().navigate(R.id.addNoteFragment, bundle)
    }

    companion object {
        const val ARG_EDIT_NOTE = "edi_note"
    }
}