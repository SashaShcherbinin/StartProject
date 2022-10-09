package com.start.base.presentation.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.start.base.presentation.common.UiHelper
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseMVVMFragment<VM : BaseViewModel>(
    private val viewModelClass: KClass<VM>
) : BaseFragment() {

    abstract val viewModel: VM

    protected val uiHelper: UiHelper by lazy { UiHelper(requireActivity()) }

    protected inline fun <reified T : ViewDataBinding> getBinding() =
        DataBindingUtil.findBinding<T>(requireView())

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.theme.value = requireActivity().theme
        super.onViewCreated(view, savedInstanceState)
        defaultObserve()
    }

    private fun defaultObserve() {
        viewModel.showErrorEvent.observe(this) { viewError ->
            viewError.handleMessage(requireActivity(), messageHandler = {
                uiHelper.showErrorToast(it)
            })
        }
        viewModel.uploadingState.observe(this) {
            uiHelper.showUploading(it)
        }
    }

}

fun <T> Flow<T>.observe(fragment: BaseFragment, function: (T) -> Unit) {
    fragment.lifecycleScope.launchWhenCreated {
        this@observe.collect(function::invoke)
    }
}
