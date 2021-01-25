package com.example.contacts.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ContactItem)

    @Query("SELECT * FROM contact_item ORDER BY firstName")
    fun getContact():LiveData<List<ContactItem>>

    @Update
    suspend fun updateItem(item: ContactItem)

    @Delete
    suspend fun deleteItem(item: ContactItem)


    @Query("SELECT * FROM contact_item WHERE firstName LIKE :search OR lastName LIKE :search ORDER BY firstName")
    fun searchContact(search: String): LiveData<List<ContactItem>>
}