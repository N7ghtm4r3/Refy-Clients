package com.tecknobit.refy

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.ametistaengine.AmetistaEngine

/**
 * Method to start the of `Refy` iOs application
 *
 */
fun MainViewController() {
    AmetistaEngine.intake()
    ComposeUIViewController {
        App()
    }
}