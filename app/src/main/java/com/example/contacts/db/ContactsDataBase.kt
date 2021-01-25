package com.example.contacts.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [ContactItem::class],
        version = 1)
abstract class ContactsDataBase : RoomDatabase() {

    abstract fun getDao() :ContactDAO

    companion object {

        @Volatile
        private var instance: ContactsDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
                ?: synchronized(LOCK) {
                    instance
                            ?: createDatabase(
                                    context
                            ).also { instance = it }
                }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ContactsDataBase::class.java, "ContactsDB.db").build()
    }
}