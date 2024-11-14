package com.github.lexanovichok.workingshifts.schedule.address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.databinding.FragmentAddressInfoBinding

class AddressInfoFragment : AbstractFragment<FragmentAddressInfoBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddressInfoBinding =
        FragmentAddressInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}