package com.github.lexanovichok.workingshifts.schedule.address.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.databinding.FragmentAddAddressBinding

class AddAddressesFragment : AbstractFragment<FragmentAddAddressBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentAddAddressBinding =
        FragmentAddAddressBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}