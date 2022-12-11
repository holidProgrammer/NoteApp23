package com.example.notes.damain.repository

import com.example.notes.core.Resource
import com.example.notes.damain.model.Note
import kotlinx.coroutines.flow.Flow


interface NoteRepository {

    fun addNote(note: Note): Flow<Resource<Unit>>

    fun deleteNote(note: Note): Flow<Resource<Unit>>

    fun editNote(note: Note): Flow<Resource<Unit>>

    fun getAllNotes(): Flow<Resource<List<Note>>>
}