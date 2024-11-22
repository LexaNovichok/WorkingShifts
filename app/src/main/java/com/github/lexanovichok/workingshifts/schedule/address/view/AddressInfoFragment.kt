package com.github.lexanovichok.workingshifts.schedule.address.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            Log.d("LiveData", "AddressInfoFragment addressInfoLiveData updated with: $address")
        }

        binding.saveButton.setOnClickListener {
            val id = viewModel.addressInfoLiveData().value?.id ?: Random(128).toString()
            val city = binding.cityEditText.text.toString()
            val street = binding.streetEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if (city.trim().isNotBlank()) {
                val address =
                    Address(id = id, city = city, street = street, description = description)
                hideKeyBoard()
                viewModel.updateAddress(address)
                Log.d("LiveData", "AddressInfoFragment saved to addressInfoLiveData: $address")
            } else {
                Toast.makeText(activity, "Город не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Вы уверены, что хотите удалить этот адресс?")
            .setPositiveButton("Удалить") { dialog, id ->
                // Получаем информацию о работнике и выполняем удаление
                viewModel.addressInfoLiveData().value?.let {
                    hideKeyBoard()
                    viewModel.deleteAddress(it.id)
                    //Toast.makeText(requireContext(), "Работник удалён", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена") { dialog, id ->
                dialog.dismiss() // Закрытие диалога без действия
            }

        val dialog = builder.create()
        dialog.show()
    }
}