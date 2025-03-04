package com.github.lexanovichok.workingshift.schedule.address.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.github.lexanovichok.workingshift.core.AbstractFragment
import com.github.lexanovichok.workingshift.core.ProvideViewModel
import com.github.lexanovichok.workingshift.databinding.FragmentAddAddressBinding
import com.github.lexanovichok.workingshift.schedule.address.viewModel.AddAddressesViewModel
import com.github.lexanovichok.workingshift.schedule.userData.Address

class AddAddressesFragment : AbstractFragment<FragmentAddAddressBinding>() {

    private lateinit var viewModel : AddAddressesViewModel
    private var addressPropArray : ArrayList<String> = arrayListOf("", "", "")
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddAddressBinding =
        FragmentAddAddressBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(AddAddressesViewModel::class.java)

        addressPropArray = savedInstanceState?.getStringArrayList(ADDRESS_KEY) ?: arrayListOf("", "", "")
        binding.cityEditText.setText(addressPropArray[0])
        binding.streetEditText.setText(addressPropArray[1])
        binding.descriptionEditText.setText(addressPropArray[2])
        Log.d("BUNDLE", "onViewCreated: $addressPropArray")

        saveTextChanges()


        binding.saveButton.setOnClickListener {
            with(binding) {
                val city = cityEditText.text.toString()
                val street = streetEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (city.trim().isNotBlank()) {
                    viewModel.addAddress(Address(city = city, street = street, description = description))

                    hideKeyBoard()
                    viewModel.comeback()
                } else {
                    Toast.makeText(activity, "Город не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun saveTextChanges() {
        binding.cityEditText.addTextChangedListener {
            with(binding) {
                val city = cityEditText.text.toString()
                addressPropArray[0] = city
            }
        }

        binding.streetEditText.addTextChangedListener {
            with(binding) {
                val street = streetEditText.text.toString()
                addressPropArray[1] = street
            }
        }

        binding.descriptionEditText.addTextChangedListener {
            with(binding) {
                val description = descriptionEditText.text.toString()
                addressPropArray[2] = description
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(ADDRESS_KEY, addressPropArray)
        Log.d("BUNDLE", "onSaveInstanceState: $addressPropArray")
    }

    companion object {
        private const val ADDRESS_KEY = "ADDRESS_KEY"
    }
}