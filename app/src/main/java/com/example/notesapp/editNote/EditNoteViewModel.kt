package com.example.notesapp.editNote

import androidx.lifecycle.ViewModel
import com.example.notesapp.database.Notes
import com.example.notesapp.database.NotesDao
import kotlinx.coroutines.*

class EditNoteViewModel(val database: NotesDao) : ViewModel() {

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

    //<--------------------------------------------------------------->

//    private val _onCreatePressed = MutableLiveData<Boolean>()
//    val onCreatePressed:LiveData<Boolean>
//            get() = _onCreatePressed
//
//    fun onDoneCreatePressed()
//    {
//        _onCreatePressed.value=false
//    }


    //<--------------------------------------------------------------->

    // update existing note
    fun onUpdate(notez: Notes) {
        uiScope.launch {
            update(notez)
//            _onCreatePressed.value=true
        }
    }

    private suspend fun update(note: Notes) {
        withContext(Dispatchers.IO)
        {
            database.update(note)
        }
    }

    //<--------------------------------------------------------------->

    // delete a note
    fun onDeleteNotes(itemKey: Long) {
        uiScope.launch {
            deleteNote(itemKey)
        }
    }

    private suspend fun deleteNote(key: Long) {
        withContext(Dispatchers.IO)
        {
            database.deleteNotes(key)
        }
    }
}

