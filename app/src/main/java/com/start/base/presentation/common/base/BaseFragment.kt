package com.start.base.presentation.common.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    val fragment: BaseFragment get() = this

    open fun onCatchBackPressed(): Boolean = false

    fun backPress() = requireActivity().onBackPressed()

    open fun onBaseActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

}