package com.example.notes.damain.data.di

import android.content.Context
import androidx.room.Room
import com.example.notes.damain.data.local.NoteDao
import com.example.notes.damain.data.local.NoteDataBase
import com.example.notes.damain.data.repository.NoteRepositoryImpl
import com.example.notes.damain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Singleton
    @Provides
    fun provideNoteDataBase(
        @ApplicationContext context: Context
    ): NoteDataBase = Room.databaseBuilder(
        context,
        NoteDataBase::class.java,
        "note_db"
    ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideNoteDao(noteDataBase: NoteDataBase) = noteDataBase.noteDao()

    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }
}