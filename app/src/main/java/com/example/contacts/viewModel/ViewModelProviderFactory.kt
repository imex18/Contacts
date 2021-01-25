package com.example.contacts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contacts.repository.ContactsRepository

class ViewModelProviderFactory( private val repository: ContactsRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactsItemViewModel(repository) as T
    }
}