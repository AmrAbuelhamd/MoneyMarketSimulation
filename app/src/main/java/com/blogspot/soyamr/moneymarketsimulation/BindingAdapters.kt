package com.blogspot.soyamr.moneymarketsimulation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:error")
fun setErrorText(view: TextInputLayout, errorMessage: String) {
    if (errorMessage.isEmpty()) {
        view.error = null
        view.isErrorEnabled = false
    }
    else {
        view.error = errorMessage
        view.isErrorEnabled = true
    }
}
