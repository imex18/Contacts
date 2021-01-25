package com.example.contacts.repository

import com.example.contacts.db.ContactItem
import com.example.contacts.db.ContactsDataBase

class ContactsRepository(private val dataBase: ContactsDataBase) {

    suspend fun insert(item: ContactItem) = dataBase.getDao().insert(item)

    fun getContactItem() = dataBase.getDao().getContact()

    suspend fun updateItem(item: ContactItem) = dataBase.getDao().updateItem(item)

    suspend fun deleteItem(item: ContactItem) = dataBase.getDao().deleteItem(item)

    fun searchContact(search :String) = dataBase.getDao().searchContact(search)

}