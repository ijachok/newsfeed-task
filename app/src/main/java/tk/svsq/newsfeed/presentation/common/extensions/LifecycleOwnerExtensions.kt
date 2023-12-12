package tk.svsq.newsfeed.presentation.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

fun LifecycleOwner.observe(state: Lifecycle.State = Lifecycle.State.STARTED, observe: suspend () -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            observe.invoke()
        }
    }
}