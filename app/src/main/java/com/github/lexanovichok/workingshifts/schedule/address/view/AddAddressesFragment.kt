package com.github.lexanovichok.workingshifts.schedule.address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentAddAddressBinding
import com.github.lexanovichok.workingshifts.schedule.address.viewModel.AddAddressesViewModel
import com.github.lexanovichok.workingshifts.schedule.userData.Address

class AddAddressesFragment : AbstractFragment<FragmentAddAddressBinding>() {

    private lateinit var viewModel : AddAddressesViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddAddressBinding =
        FragmentAddAddressBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(AddAddressesViewModel::class.java)

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
                    Toast.makeText(activity, "City can't be empty", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}