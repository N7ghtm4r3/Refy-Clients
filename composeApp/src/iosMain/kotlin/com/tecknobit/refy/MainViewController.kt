package com.tecknobit.refy

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.equinoxcompose.session.setUpSession

fun MainViewController() = ComposeUIViewController {
    setUpSession {
        localUser.clear()
        navigator.navigate(SPLASHSCREEN)
    }
    App()
}