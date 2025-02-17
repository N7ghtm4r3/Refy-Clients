package com.tecknobit.refy

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession

/**
 * Method to start the of `Refy` iOs application
 *
 */
fun MainViewController() {
    AmetistaEngine.intake()
    ComposeUIViewController {
        setUpSession {
            localUser.clear()
            navigator.navigate(SPLASHSCREEN)
        }
        App()
    }
}