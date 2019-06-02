package sk.spacecode.matecheck.common

import android.text.TextUtils
import android.widget.EditText

object Validators {

    fun validateForm(vararg fields: EditText): Boolean {
        var valid = true

        for (field in fields) {
            if (TextUtils.isEmpty(field.text.toString())) {
                field.error = "Required."
                valid = false
            } else {
                field.error = null
            }
        }
        return valid
    }
}