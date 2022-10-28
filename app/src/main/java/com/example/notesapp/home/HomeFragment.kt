package com.example.notesapp.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesApapter
import com.example.notesapp.database.Notes
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    //binding variable
    lateinit var binding: FragmentHomeBinding

    //viewmodel
    lateinit var homeViewModel: HomeViewModel

    //adapter for notes recyclerview
    var adapter = NotesApapter()

    // list for searched item
    var filteredNotes = arrayListOf<Notes>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        //application context
        val application = requireNotNull(this.activity).application

        //database instance
        val dataSource = NotesDatabase.getInstance(application).notesDao

        //factory to instantiate viewmodel
        val viewModelFactory = HomeViewModelFactory(dataSource)

        //viewmodel
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        //bind adapter to recyclerview
        binding.rcvNotes.adapter = adapter

        //<--------------------------------------------------------------->

        // observe changes in all-notes list
        homeViewModel.notes.observe(viewLifecycleOwner, Observer {

            // list need to search item from all notes
            filteredNotes = it as ArrayList<Notes>

            //<--------------------------------------------------------------->

            //important to observe data when app freshly starts(to observe already stored data)
            adapter.data = it

            //text and image to tell user that notes are empty or not
            if (it.size > 0) {
                binding.emptyText.visibility = View.GONE
                binding.emptyLogo.visibility = View.GONE

            } else if (it.size == 0) {
                binding.emptyText.visibility = View.VISIBLE
                binding.emptyLogo.visibility = View.VISIBLE
                binding.allPriority.visibility = View.GONE
                binding.lowPriority.visibility = View.GONE
                binding.mediumPriority.visibility = View.GONE
                binding.highPriority.visibility = View.GONE
            }
        })

        //<--------------------------------------------------------------->

//        create new note button
        binding.newNoteButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }

        //<--------------------------------------------------------------->

        // filter notes based on priority
        //All priority filter
        binding.allPriority.setOnClickListener {
            homeViewModel.notes.observe(viewLifecycleOwner, Observer {

                // list need to search item from all notes
                filteredNotes = it as ArrayList<Notes>

                // to bind correct data
                it?.let {
                    adapter.data = it
                }
            })
        }

        //<--------------------------------------------------------------->

        //Low priority filter
        binding.lowPriority.setOnClickListener {
            homeViewModel.lowNotes.observe(viewLifecycleOwner, Observer {

                // list need to search item from low priority notes
                filteredNotes = it as ArrayList<Notes>

                // to bind correct data
                it?.let {
                    adapter.data = it
                }
            })
        }

        //<--------------------------------------------------------------->

        //Medium priority filter
        binding.mediumPriority.setOnClickListener {
            homeViewModel.mediumNotes.observe(viewLifecycleOwner, Observer {


                // list need to search item from medium priority notes
                filteredNotes = it as ArrayList<Notes>

                // to bind correct data
                it?.let {
                    adapter.data = it
                }
            })
        }

        //<--------------------------------------------------------------->

        //High priority filter
        binding.highPriority.setOnClickListener {
            homeViewModel.highNotes.observe(viewLifecycleOwner, Observer {

                // list need to search item from high priority notes
                filteredNotes = it as ArrayList<Notes>

                // to bind correct data
                it?.let {
                    adapter.data = it
                }
            })
        }

        //<--------------------------------------------------------------->

        setHasOptionsMenu(true)
        return binding.root
    }

    //<--------------------------------------------------------------->

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.overflow_menu, menu)

        // search view to search items
        val item = menu.findItem(R.id.app_bar_search)
        val search = item.actionView as SearchView
        search.queryHint = "search notes here"

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            // checks item as we keep entering text
            override fun onQueryTextChange(newText: String?): Boolean {
                NotesFiltering(newText)
                return true
            }
        })
    }

    //<--------------------------------------------------------------->

    //function to search item based on text we entered in searchview
    private fun NotesFiltering(newText: String?) {
        var newFilteredList = arrayListOf<Notes>()
        for (i in filteredNotes) {
            // NOTE:- i changed below newtext parameters to toString , then it started to collect data by matching
            if (i.title!!.contains(newText!!.toString()) || i.subTitle!!.contains(newText.toString())) {
                newFilteredList.add(i)
            }
        }
        adapter.filtering(newFilteredList)
        Log.i("****************", "$newText + ${adapter.data}")
    }

    //<--------------------------------------------------------------->

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_data) {
            homeViewModel.onClearNotes()
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.cleared_all_data),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return NavigationUI.onNavDestinationSelected(
            item!!,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}

//<--------------------------------------------------------------->

