package com.example.notes.damain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.damain.data.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 3, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}