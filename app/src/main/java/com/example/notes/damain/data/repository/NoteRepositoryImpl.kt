package com.example.notes.damain.data.repository

import com.example.notes.core.BaseRepository
import com.example.notes.core.Resource
import com.example.notes.damain.model.Note
import com.example.notes.damain.data.local.NoteDao
import com.example.notes.damain.data.mapper.toNote
import com.example.notes.damain.data.mapper.toNoteEntity
import com.example.notes.damain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository, BaseRepository() {

    override fun addNote(note: Note): Flow<Resource<Unit>> = doRequest {
        noteDao.addNote(note.toNoteEntity())
    }

    override fun deleteNote(note: Note) = doRequest {
        noteDao.deleteNote(note.toNoteEntity())
    }

    override fun editNote(note: Note): Flow<Resource<Unit>> = doRequest {
        noteDao.editNote(note.toNoteEntity())
    }

    override fun getAllNotes(): Flow<Resource<List<Note>>> = flow {
        emit(Resource.Loading(data = null))
        try {
            val data = noteDao.gAllNotes().map { it.toNote() }
            emit(Resource.Success(data))
        } catch (ioException: IOException) {
            emit(Resource.Error(ioException.localizedMessage ?: "Unknown error"))
        }
    }
}