package com.example.contacts.ui.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.ui.MainActivity
import com.example.contacts.viewModel.ContactsItemViewModel
import kotlinx.android.synthetic.main.fragment_contact_details.*

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val args by navArgs<ContactDetailsFragmentArgs>()
    private lateinit var viewModel: ContactsItemViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = (activity as MainActivity).viewModel

        populateFields()


        cd_email.setOnClickListener {
            sendEmail(args.detailsCurrentItem?.email)
        }


        cd_message.setOnClickListener {
            sendMessage(args.detailsCurrentItem?.phone)
        }

        cd_call.setOnClickListener {
            call(args.detailsCurrentItem?.phone)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.app_bar_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {

            R.id.app_bar_delete -> {

                AlertDialog.Builder(requireContext())
                        .setTitle("Delete Contact")
                        .setMessage("${args.detailsCurrentItem?.firstName} will be permanently removed.")
                        .setPositiveButton("Delete") { _, _ ->
                            args.detailsCurrentItem.let {
                                if (it != null) {
                                    viewModel.deleteItem(it)
                                }
                            }
                            findNavController().navigate(R.id.action_contactDetailsFragment_to_displayContactsFragment)
                            Toast.makeText(
                                    requireContext(),
                                    "${args.detailsCurrentItem?.firstName} has been deleted",
                                    Toast.LENGTH_SHORT
                            ).show()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()

            }

            R.id.app_bar_edit -> {

                val currentItem = args.detailsCurrentItem

                val action = ContactDetailsFragmentDirections.actionContactDetailsFragmentToCreateContactFragment(currentItem)


                findNavController().navigate(action)


            }

        }
        return false
    }

    private fun populateFields(){

        val fullName = getString(
                R.string.fullName,
                args.detailsCurrentItem?.firstName,
                args.detailsCurrentItem?.lastName
        )

        if (!args.detailsCurrentItem?.profileAvatarPath.isNullOrEmpty()) {

            Glide.with(requireActivity()).load(args.detailsCurrentItem?.profileAvatarPath)
                    .circleCrop().into(
                            cd_iv_avatar
                    )

        } else {

            cd_iv_avatar.setImageResource(R.drawable.default_avatar)
        }

        cd_tv_name_under_avatar.text = fullName
        cd_tv_fullName.text = fullName
        cd_tv_phoneNumber.text = args.detailsCurrentItem?.phone
        cd_tv_email.text = args.detailsCurrentItem?.email
        cd_tv_company.text = args.detailsCurrentItem?.company
    }



    private fun sendEmail(email: String?) {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            type = "text/plain"
        }

        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }

    private fun call(phoneNumber: String?) {

        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }

        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }

    private fun sendMessage(phoneNumber: String?) {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("sms:$phoneNumber")
        }

        if (activity?.packageManager?.let { intent.resolveActivity(it) } != null) {
            startActivity(intent)
        }
    }


}