package com.yomo.yakgwa.util.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController


const val RESULT_KEY = "result"

inline fun <T> Fragment.getNavResult(
    @IdRes navId: Int,
    key: String = RESULT_KEY,
    crossinline onChanged: (T?) -> Unit
) {
    val backStackEntry = findNavController().getBackStackEntry(navId)
    val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME && backStackEntry.savedStateHandle.contains(key)) {
            val result = backStackEntry.savedStateHandle.get<T>(key)
            onChanged(result)
            backStackEntry.savedStateHandle.remove<T>(key)
        }
    }
    backStackEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                backStackEntry.lifecycle.removeObserver(observer)
            }
        }
    )
}

fun <T> Fragment.setNavResult(key: String = RESULT_KEY, data: T) {
    findNavController().previousBackStackEntry?.also { stack ->
        stack.savedStateHandle[key] = data
    }
}