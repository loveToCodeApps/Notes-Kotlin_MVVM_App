package com.example.notesapp.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.notesapp.database.NotesDao
import kotlinx.coroutines.*

class HomeViewModel(val database: NotesDao) : ViewModel() {

    //job to cancel all coroutines
    private var viewModelJob = Job()

    //remove coroutines before cleanup
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //scope to run coroutines on particular thread
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //get all the latest notes
    var notes = database.getAllNotes()

    //get notes with low priority
    var lowNotes = database.getLowPriorityNotes()

    //get notes with medium priority
    var mediumNotes = database.getMediumPriorityNotes()

    //get notes with high priority
    var highNotes = database.getHighPriorityNotes()


    // delete all notes at once
    fun onClearNotes() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }


}





