package com.start.presentation.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.start.presentation.common.UiHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
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
        viewModel.showErrorEvent.consumeAsFlow().connectContentState(this) { viewError ->
            viewError.handleMessage(requireActivity(), messageHandler = {
                uiHelper.showErrorToast(it)
            })
        }
        viewModel.uploadingState.connectContentState(this) {
            uiHelper.showUploading(it)
        }
        viewModel.messageEvent.consumeAsFlow().connectContentState(this) {
            uiHelper.showMessage(it)
        }
    }

}

fun <T> Flow<T>.connectContentState(fragment: BaseFragment, function: (T) -> Unit) {
    fragment.lifecycleScope.launchWhenCreated {
        this@connectContentState.collect(function::invoke)
    }
}
