package com.example.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface NotesDao {

    //insert a new note
    @Insert
    fun insert(notes: Notes)

    //update an existing note
    @Update
    fun update(notes: Notes)

    //get all the notes in the table
    @Query("SELECT * from my_notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    //delete any particular note
    @Query("DELETE from my_notes_table WHERE id=:key")
    fun deleteNotes(key: Long)

    //delete all notes at once
    @Query("DELETE from my_notes_table")
    fun clear()

    // queries for filter / search -----------------------------
    @Query("SELECT * from my_notes_table WHERE notess_priority='high' ORDER BY id DESC ")
    fun getHighPriorityNotes(): LiveData<List<Notes>>

    @Query("SELECT * from my_notes_table WHERE notess_priority='medium' ORDER BY id DESC")
    fun getMediumPriorityNotes(): LiveData<List<Notes>>

    @Query("SELECT * from my_notes_table WHERE notess_priority='low' ORDER BY id DESC")
    fun getLowPriorityNotes(): LiveData<List<Notes>>

//----------------------------------------------------------
}

