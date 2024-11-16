package com.github.lexanovichok.workingshifts.schedule.address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.core.ProvideViewModel
import com.github.lexanovichok.workingshifts.databinding.FragmentAddressInfoBinding
import com.github.lexanovichok.workingshifts.schedule.address.viewModel.AddressInfoViewModel
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import kotlin.random.Random

class AddressInfoFragment : AbstractFragment<FragmentAddressInfoBinding>() {

    private lateinit var viewModel : AddressInfoViewModel
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddressInfoBinding =
        FragmentAddressInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as ProvideViewModel).viewModel(AddressInfoViewModel::class.java)

        viewModel.addressInfoLiveData().observe(viewLifecycleOwner) { address ->
            if (address != null) {
                with(binding) {
                    cityEditText.setText(address.city)
                    streetEditText.setText(address.street)
                    descriptionEditText.setText(address.description)
                }
            } else {
                with (binding) {
                    cityEditText.setText("")
                    streetEditText.setText("")
                    descriptionEditText.setText("")
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val id = viewModel.addressInfoLiveData().value?.id ?: Random(128).toString()
            val city = binding.cityEditText.text.toString()
            val street = binding.streetEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            val address = Address(id = id, city = city, street = street, description = description)
            hideKeyBoard()
            viewModel.updateAddress(address)
        }

        binding.deleteButton.setOnClickListener {
            viewModel.addressInfoLiveData().value?.let {
                hideKeyBoard()
                viewModel.deleteAddress(it.id)
            }
        }
    }
}