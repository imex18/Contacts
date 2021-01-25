package com.example.contacts.ui.UI.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.R
//import com.example.contacts.UI.fragments.DisplayContactsFragmentDirections
import com.example.contacts.viewModel.ContactsItemViewModel
import com.example.contacts.db.ContactItem
import com.example.contacts.ui.UI.fragments.DisplayContactsFragmentDirections
import kotlinx.android.synthetic.main.contact_item_layout.view.*

class ContactsItemAdapter(
        var items: List<ContactItem>,
        private val viewModel: ContactsItemViewModel
) : RecyclerView.Adapter<ContactsItemAdapter.ContactsViewHolder>() {


    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {

        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout, parent, false)
        return ContactsViewHolder(view)


    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val currentItem = items[position]


        holder.itemView.apply {


            if (!currentItem.profileAvatarPath.isNullOrEmpty()) {

                val uri = Uri.parse(currentItem.profileAvatarPath)
                contact_item_thumbnail.setImageURI(uri)

            } else {

                contact_item_thumbnail.setImageResource(R.drawable.default_avatar)
            }


            val fullName =
                    resources.getString(R.string.fullName, currentItem.firstName, currentItem.lastName)

            contact_item_full_name.text = fullName

            row_layout.setOnClickListener {


                val action = DisplayContactsFragmentDirections.actionDisplayContactsFragmentToContactDetailsFragment(
                        currentItem
                )

                findNavController().navigate(action)
            }

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}


