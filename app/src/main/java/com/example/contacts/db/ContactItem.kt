package com.example.contacts.db

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "contact_item")
class ContactItem (
        @PrimaryKey (autoGenerate = true)
        val id : Int,
        val profileAvatarPath: String?,
        val firstName:String?,
        val lastName:String?,
        val phone:String?,
        val email:String?,
        val company:String?,
    ) :Parcelable{


}