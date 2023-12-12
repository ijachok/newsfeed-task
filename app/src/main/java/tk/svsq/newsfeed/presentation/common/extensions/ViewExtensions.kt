package tk.svsq.newsfeed.presentation.common.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.snackbarWithUndoAction(message: String, actionText: String, onUndoAction: () -> Unit) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).apply {
        addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
            }
        })
        setAction(actionText) {
            onUndoAction.invoke()
        }
        show()
    }
}