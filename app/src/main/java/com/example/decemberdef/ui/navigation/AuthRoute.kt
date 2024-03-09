package com.example.decemberdef.ui.navigation

import androidx.annotation.StringRes
import com.example.decemberdef.R


enum class AuthRoute(@StringRes val title: Int) {
    SignIn(title = R.string.get_started),
    SignUp(title = R.string.sign_up)
}