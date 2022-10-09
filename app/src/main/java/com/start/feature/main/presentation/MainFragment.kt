package com.start.feature.main.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.start.R
import com.start.databinding.MainFragmentBinding
import com.start.base.presentation.common.base.BaseMVVMBindFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseMVVMBindFragment<MainViewModel, MainFragmentBinding>(
    MainViewModel::class,
    R.layout.main_fragment
) {

    override val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.vm = viewModel
    }
}