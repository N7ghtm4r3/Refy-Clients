package com.tecknobit.refy

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.ametistaengine.AmetistaEngine
import kotlinx.browser.document

/**
 * Method to start the of `Refy` webapp
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        App()
    }
}