package com.example.notesapp.editNote

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.database.Notes
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.databinding.FragmentEditNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

    class EditNoteFragment : Fragment() {

        //viewmodel
        lateinit var editNoteViewModel: EditNoteViewModel

        //safe args
        lateinit var args: EditNoteFragmentArgs

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            // Inflate the layout for this fragment
            val binding: FragmentEditNoteBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_edit_note, container, false)

            //application context
            val application = requireNotNull(this.activity).application

            //database instance
            val dataSource = NotesDatabase.getInstance(application).notesDao

            //factory to instantiate viewmodel
            val viewModelFactory = EditNoteViewModelFactory(dataSource)

            //viewmodel
            editNoteViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(EditNoteViewModel::class.java)

            //safe args
            args = EditNoteFragmentArgs.fromBundle(requireArguments())

            //fetch data of an existing note
            binding.title.setText(args.oldData.title)
            binding.subTitle.setText(args.oldData.subTitle)
            binding.notes.setText(args.oldData.note)
            binding.myToggle.check(
                when (args.oldData.priority) {
                    "low" -> R.id.low
                    "medium" -> R.id.medium
                    else -> R.id.high
                }
            )

            //priority value
            var priority: String = "low"

            binding.myToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.low -> priority = "low"
                        R.id.medium -> priority = "medium"
                        R.id.high -> priority = "high"
                    }
                }
            }

            //<--------------------------------------------------------------->

            // edit button
            binding.edit.setOnClickListener {

                if (binding.title.length() != 0 && binding.subTitle.length() != 0) {
                    val title = binding.title.text.toString()
                    val subTitle = binding.subTitle.text.toString()
                    val note = binding.notes.text.toString()

                    editNoteViewModel.onUpdate(Notes(args.oldData.id, title, subTitle, note, priority))
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "updated succesfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    this.findNavController().navigate(R.id.action_editNoteFragment_to_homeFragment)
                } else {
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        "it can't be empty",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            //<--------------------------------------------------------------->

            setHasOptionsMenu(true)
            return binding.root

        }

        //<--------------------------------------------------------------->

        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater?.inflate(R.menu.delete_item, menu)
        }

        //<--------------------------------------------------------------->

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.delete_item) {
                val deleteDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
                deleteDialog.setContentView(R.layout.delete_dialog)
                deleteDialog.show()

                val yesTextView = deleteDialog.findViewById<TextView>(R.id.yes)
                val noTextView = deleteDialog.findViewById<TextView>(R.id.no)

                yesTextView?.setOnClickListener {
                    editNoteViewModel.onDeleteNotes(args.oldData.id!!)
                    deleteDialog.dismiss()
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.deleted_successfully),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_editNoteFragment_to_homeFragment)
                }

                noTextView?.setOnClickListener {
                    deleteDialog.dismiss()
                }
            }

            //<--------------------------------------------------------------->

            return super.onOptionsItemSelected(item)
        }
    }

