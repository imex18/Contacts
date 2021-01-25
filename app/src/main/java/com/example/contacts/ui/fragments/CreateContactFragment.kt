package com.example.contacts.ui.UI.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.ui.MainActivity
//import com.example.contacts.UI.fragments.CreateContactFragmentArgs
//import com.example.contacts.UI.fragments.CreateContactFragmentDirections
import com.example.contacts.viewModel.ContactsItemViewModel
import com.example.contacts.db.ContactItem
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_create_contact.*


class CreateContactFragment : Fragment(R.layout.fragment_create_contact) {


    private var cropActivityContract = object : ActivityResultContract<Any?, Uri>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {

            return CropImage.getActivityResult(intent)?.uri
        }
    }


    private lateinit var cropActivityForResultLauncher: ActivityResultLauncher<Any?>

    private lateinit var viewModel: ContactsItemViewModel

    private val args by navArgs<CreateContactFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)


        viewModel = (activity as MainActivity).viewModel


        cropActivityForResultLauncher = registerForActivityResult(cropActivityContract) {
            it?.let { it ->

                val croppedImagePath = it.path
                viewModel.addCroppedImagePath(croppedImagePath)
                viewModel.addedAvatarPicture(it)
            }
        }

        val isEditMode = args.editContactArguments != null

        if (isEditMode) {

            create_contact_button.text = "Update"
            populateFields()
        }


        iv_avatar.setOnClickListener {
            cropActivityForResultLauncher.launch(null)
        }


        create_contact_button.setOnClickListener {
            if (isEditMode) {
                  updateContact()

            }else{

                saveContact()
            }
        }

        viewModel.profilePicture.observe(viewLifecycleOwner, Observer { uri ->
            if (uri != null) {
                setImage(uri)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun saveContact() {

        val firstName = first_name.text?.toString()
        val lastName = last_name.text?.toString()
        val phoneNumber = phone_number.text?.toString()
        val email = email.text?.toString()
        val company = company.text?.toString()
        val avatar = viewModel.croppedImagePath

        val contactItem = ContactItem(0, avatar, firstName, lastName, phoneNumber, email, company)

        if (firstName.isNullOrEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Please enter a First Name",
                    Toast.LENGTH_LONG
            ).show()

        } else {

            viewModel.insert(contactItem)

            Snackbar.make(
                    requireView(),
                    "$firstName has been successfully saved",
                    Snackbar.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_createContactFragment_to_displayContactsFragment)

            viewModel.profilePicture.value = null
            viewModel.croppedImagePath = null
        }
    }


    private fun populateFields() {
        val currentItem = args.editContactArguments

        if (!currentItem?.profileAvatarPath.isNullOrEmpty()) {

            Glide.with(requireActivity()).load(currentItem?.profileAvatarPath)
                    .circleCrop().into(
                            iv_avatar
                    )
        }

        first_name.setText(currentItem?.firstName)
        last_name.setText(currentItem?.lastName)
        phone_number.setText(currentItem?.phone)
        email.setText(currentItem?.email)
        company.setText(currentItem?.company)
    }


    private fun updateContact() {

        val firstName = first_name.text?.toString()
        val lastName = last_name.text?.toString()
        val phoneNumber = phone_number.text?.toString()
        val email = email.text?.toString()
        val company = company.text?.toString()

        val avatar = if (viewModel.croppedImagePath != null){

            viewModel.croppedImagePath
        }else {

            args.editContactArguments?.profileAvatarPath

        }

        val contactItem = ContactItem(
                args.editContactArguments!!.id,
                avatar,
                firstName,
                lastName,
                phoneNumber,
                email,
                company
        )

        if (firstName.isNullOrEmpty()) {

            Toast.makeText(
                    requireContext(),
                    "Please enter a First Name",
                    Toast.LENGTH_LONG
            ).show()

        } else {

            viewModel.updateItem(contactItem)

            Snackbar.make(
                    requireView(),
                    "$firstName has been successfully updated",
                    Snackbar.LENGTH_SHORT
            ).show()

            val action = CreateContactFragmentDirections.actionCreateContactFragmentToContactDetailsFragment(contactItem)

            findNavController().navigate(action)

            viewModel.profilePicture.value = null
            viewModel.croppedImagePath = null
        }
    }


    private fun setImage(uri: Uri) {
        Glide.with(requireActivity())
                .load(uri)
                .circleCrop()
                .into(iv_avatar)
    }

}



