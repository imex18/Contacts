package com.example.contacts.ui.UI.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.R
import com.example.contacts.ui.MainActivity
import com.example.contacts.ui.UI.adapters.ContactsItemAdapter
import com.example.contacts.viewModel.ContactsItemViewModel
import kotlinx.android.synthetic.main.fragment_display_contacts.*


class DisplayContactsFragment : Fragment(R.layout.fragment_display_contacts) {

    private lateinit var viewModel: ContactsItemViewModel
    private lateinit var adapter: ContactsItemAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = (activity as MainActivity).viewModel
        adapter = ContactsItemAdapter(listOf(), viewModel)

        rv_display_contacts.layoutManager = LinearLayoutManager(requireContext())
        rv_display_contacts.adapter = adapter


        viewModel.getContactItem().observe(viewLifecycleOwner, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()

            tv_empty_rv.isVisible = adapter.items.isEmpty()
        })


        fab.setOnClickListener {

            Navigation.findNavController(requireView())
                    .navigate(R.id.action_displayContactsFragment_to_createContactFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.app_bar_edit).isVisible = false
        menu.findItem(R.id.app_bar_delete).isVisible = false

        val search = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView? = search?.actionView as SearchView?
        searchView?.queryHint = "Search Contacts"
        searchView?.isSubmitButtonEnabled = true

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {

                if (query != null) {

                    searchContact(query)
                }
                return true
            }
        })


    }

    private fun searchContact(search: String) {
        val searchQuery = "%$search%"
        viewModel.searchContact(searchQuery).observe(this, { list ->
            list.let {
                adapter.items = it
                adapter.notifyDataSetChanged()

                tv_no_result.isVisible = adapter.items.isEmpty() && !tv_empty_rv.isVisible
            }
        })
    }


}


