package com.github.lexanovichok.workingshifts.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.lexanovichok.workingshifts.core.AbstractFragment
import com.github.lexanovichok.workingshifts.databinding.FragmentScheduleBinding

class ScheduleFragment : AbstractFragment<FragmentScheduleBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentScheduleBinding =
        FragmentScheduleBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}