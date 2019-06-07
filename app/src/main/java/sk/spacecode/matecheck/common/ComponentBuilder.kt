package sk.spacecode.matecheck.common

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import sk.spacecode.matecheck.R

class ComponentBuilder {

    fun createSnackBar(rootElement: View, message: String, context: Activity?) {
        val snackbar = Snackbar.make(rootElement, message, Snackbar.LENGTH_SHORT)

        context?.let {
            snackbar.view.setBackgroundColor(ContextCompat.getColor(it.applicationContext, R.color.black))
            snackbar.setTextColor(context.resources.getColor(android.R.color.white))
        }
        snackbar.show()
    }
}