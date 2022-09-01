package dev.anshshukla.splitty.utils

import android.text.TextUtils

enum class FormValidationErrorCode {
    NO_ERROR,
    BLANK,
    EMAIL_MALFORMED
}

object FormUtils {
    fun validateEmail(email: String): FormValidationErrorCode {
        if(TextUtils.isEmpty(email)) return FormValidationErrorCode.BLANK
        if(!email.matches(android.util.Patterns.EMAIL_ADDRESS.toRegex()))
            return FormValidationErrorCode.EMAIL_MALFORMED
        return FormValidationErrorCode.NO_ERROR
    }

    fun validatePassword(password: String): FormValidationErrorCode {
        if(TextUtils.isEmpty(password)) return FormValidationErrorCode.BLANK
        return FormValidationErrorCode.NO_ERROR
    }
}