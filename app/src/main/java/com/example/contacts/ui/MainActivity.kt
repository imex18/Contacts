package com.example.contacts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.contacts.R
import com.example.contacts.viewModel.ContactsItemViewModel
import com.example.contacts.viewModel.ViewModelProviderFactory
import com.example.contacts.db.ContactsDataBase
import com.example.contacts.repository.ContactsRepository

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ContactsItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contactsRepository = ContactsRepository( ContactsDataBase (this) as ContactsDataBase)
        val viewModelProviderFactory = ViewModelProviderFactory(contactsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ContactsItemViewModel::class.java)


        setupActionBarWithNavController(findNavController(R.id.fragment))

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp():Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    }
