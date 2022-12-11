package com.example.notes.presentation.fragment.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.notes.R
import com.example.notes.core.BaseFragment
import com.example.notes.core.UIState
import com.example.notes.damain.model.Note
import com.example.notes.databinding.FragmentAddNoteBinding
import com.example.notes.presentation.fragment.note.NoteFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteFragment : BaseFragment(R.layout.fragment_add_note) {

    private val binding by viewBinding(FragmentAddNoteBinding::bind)
    private val viewModel by viewModels<AddNoteViewModel>()

    private var editableNote: Note? = null

    private var screen_mode: String = MODE_UNKNOWN
    private var note_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT) {
            throw RuntimeException("Screen mode is unknown")
        }
        screen_mode = mode
        if (screen_mode == MODE_EDIT) {
            if (!args.containsKey(NOTE_ID)) {
                throw RuntimeException("Param note id is absent")
            }
            note_id = args.getInt(NOTE_ID, Note.DEFAULT_ID)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupRequest()
        setupObservers()
        setupClickListeners()
    }

    private fun initialize() {
        editableNote = arguments?.getSerializable(NoteFragment.ARG_EDIT_NOTE) as Note?
        if (editableNote != null) {
            binding.editTitle.setText(editableNote!!.title)
            binding.editDescription.setText(editableNote!!.description)
        }
    }

    private fun setupRequest() {
    }

    private fun setupObservers() {
        viewModel.editNoteState.collectState(
            onLoading = {
                Toast.makeText(requireContext(), "Item touched", Toast.LENGTH_SHORT).show()
            },
            onError = { },
            onSuccess = {
                findNavController().navigateUp()
            }
        )
//        viewModel.addNoteState.collectState(
//            onLoading = {
//
//            },
//            onError = {
//
//            },
//            onSuccess = {
//                findNavController().navigateUp()
//            }
//        )
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addNoteState.collect { state ->
                    when (state) {
                        is UIState.Loading -> {

                        }
                        is UIState.Success -> {
                            findNavController().navigateUp()
                        }
                        is UIState.Error -> {

                        }
                        is UIState.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun setupClickListeners() = with(binding) {
        btnSave.setOnClickListener {
            if (editableNote != null) {
                val note = Note(
                    id = editableNote!!.id,
                    title = editTitle.text.toString(),
                    description = editDescription.text.toString(),
                    createdAt = System.currentTimeMillis()
                )
                viewModel.editNote(note)
            } else {
                viewModel.addNote(
                    Note(
                        title = editTitle.text.toString(),
                        description = editDescription.text.toString(),
                        createdAt = System.currentTimeMillis()
                    )
                )
            }

        }
    }

    companion object {

        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val NOTE_ID = "note_id"
        private const val MODE_UNKNOWN = ""

        fun newInstanceEditMode(noteId: Int): AddNoteFragment {
            return AddNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(NOTE_ID, noteId)
                }
            }
        }
    }
}