package com.tecknobit.refy

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        setUpSession {
            localUser.clear()
            navigator.navigate(SPLASHSCREEN)
        }
        App()
    }
}