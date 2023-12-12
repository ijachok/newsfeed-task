package tk.svsq.newsfeed.presentation.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

fun Fragment.proceedIfResumed(action: () -> Unit) {
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        action.invoke()
    }
}

fun Fragment.proceedIfStarted(action: () -> Unit) {
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        action.invoke()
    }
}