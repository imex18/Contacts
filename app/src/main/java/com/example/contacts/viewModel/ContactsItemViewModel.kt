package com.example.contacts.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.contacts.db.ContactItem
import com.example.contacts.repository.ContactsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ContactsItemViewModel(private  val repository: ContactsRepository): ViewModel() {

var profilePicture = MutableLiveData<Uri?>()
var croppedImagePath:String? = null

    fun addCroppedImagePath (path:String?){

        croppedImagePath = path
    }

    fun addedAvatarPicture (item: Uri) {

        profilePicture.value = item
    }


    fun insert(item: ContactItem) = GlobalScope.launch {

        repository.insert(item)
    }

    fun getContactItem () = repository.getContactItem()

    fun updateItem (item: ContactItem) = GlobalScope.launch {

        repository.updateItem(item)
    }

    fun deleteItem (item: ContactItem) = GlobalScope.launch {

        repository.deleteItem(item)
    }


    fun searchContact(search:String) = repository.searchContact(search)

}