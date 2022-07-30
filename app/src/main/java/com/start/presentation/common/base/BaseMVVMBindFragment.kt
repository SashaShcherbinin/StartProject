package com.start.presentation.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseMVVMBindFragment<VM : BaseViewModel, B : ViewDataBinding>(
    viewModelClass: KClass<VM>,
    @LayoutRes
    private val layoutResId: Int,
) : BaseMVVMFragment<VM>(viewModelClass) {

    private var _dataBinding: B? = null

    val dataBinding: B
        get() = _dataBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<B>(inflater, layoutResId, container, false)
            .apply { _dataBinding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}