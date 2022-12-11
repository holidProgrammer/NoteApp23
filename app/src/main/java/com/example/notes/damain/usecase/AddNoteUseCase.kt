package com.example.notes.damain.usecase

import com.example.notes.damain.model.Note
import com.example.notes.damain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun addNote(note: Note) = noteRepository.addNote(note)
}