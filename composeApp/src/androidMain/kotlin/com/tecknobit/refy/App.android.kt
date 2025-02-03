package com.tecknobit.refy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    // TODO: MAKE THE REAL NAVIGATION
    startSession()
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    // TODO: TO SET 
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
    // TODO: TO IMPLEMENT 
}