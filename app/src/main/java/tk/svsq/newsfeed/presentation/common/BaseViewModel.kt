package tk.svsq.newsfeed.presentation.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val errorStateFlow = MutableSharedFlow<String>()

    fun handleException(it: Throwable, actions: (() -> Unit)? = null) {
        viewModelScope.launch {
            errorStateFlow.emit(it.message.orEmpty())
        }
        actions?.invoke()
        Log.e("BaseViewModel", "Exception found: ${it.message}")
    }
}