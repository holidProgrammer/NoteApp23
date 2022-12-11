package com.example.notes.damain.usecase

import com.example.notes.damain.repository.NoteRepository
import javax.inject.Inject

class GeAllNotesUseCase @Inject constructor(private val noteRepository: NoteRepository) {

    fun getAllNotes() = noteRepository.getAllNotes()
}