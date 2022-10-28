package com.example.notesapp.newNote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.database.Notes
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.databinding.FragmentNewNoteBinding
import com.google.android.material.snackbar.Snackbar

    class NewNoteFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            // Inflate the layout for this fragment
            val binding: FragmentNewNoteBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_new_note, container, false
            )

            //application context
            val application = requireNotNull(this.activity).application

            //database instance
            val dataSource = NotesDatabase.getInstance(application).notesDao

            //factory to instantiate viewmodel
            val viewModelFactory = NewNoteViewModelFactory(dataSource)

            //viewmodel
            val newTaskViewmodel =
                ViewModelProviders.of(this, viewModelFactory).get(NewNoteViewModel::class.java)

            binding.lifecycleOwner = this

            //priority value
            var priority: String = "low"

            //<--------------------------------------------------------------->

            //set priority value on toggle button click
            binding.myToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.high -> priority = "high"
                        R.id.medium -> priority = "medium"
                        else -> priority = "low"
                    }
                }
            }

            //<--------------------------------------------------------------->

            //create new note
            binding.saveNotes.setOnClickListener {
                if (binding.title.text!!.trim().length != 0 && binding.subTitle.text!!.trim().length != 0) {
                    val title = binding.title.text.toString()
                    val subTitle = binding.subTitle.text.toString()
                    val note = binding.notes.text.toString()
                    newTaskViewmodel.onCreateNotes(Notes(null, title, subTitle, note, priority))
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.note_added_success),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    this.findNavController().navigate(R.id.action_newNoteFragment_to_homeFragment)
                } else {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.cant_be_empty),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            //<--------------------------------------------------------------->

            return binding.root
        }
    }

