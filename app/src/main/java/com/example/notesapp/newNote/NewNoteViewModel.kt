package com.example.notesapp.newNote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.database.Notes
import com.example.notesapp.database.NotesDao
import kotlinx.coroutines.*



class NewNoteViewModel(val database: NotesDao) : ViewModel() {

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

    private val _onCreatePressed = MutableLiveData<Boolean>()
    val onCreatePressed: LiveData<Boolean>
        get() = _onCreatePressed
    //
    fun onDoneCreatePressed()
    {
        _onCreatePressed.value=false
    }
//

    // insert function
    fun onCreateNotes(notez:Notes) {
        uiScope.launch {
            _onCreatePressed.value=true
            addNotes(notez)

        }
    }

    private suspend fun addNotes(note: Notes) {
        withContext(Dispatchers.IO)
        {
            database.insert(note)
        }
    }
}


