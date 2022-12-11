package com.example.notes.damain.data.mapper

import com.example.notes.damain.data.model.NoteEntity
import com.example.notes.damain.model.Note

fun Note.toNoteEntity() = NoteEntity(
    id, title, description, createdAt
)

fun NoteEntity.toNote() = Note(
    id, title, description, createdAt
)