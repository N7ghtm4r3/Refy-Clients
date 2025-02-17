package com.tecknobit.refy

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.app_name
import refy.composeapp.generated.resources.logo

/**
 * Method to start the of `Refy` desktop app
 *
 */
fun main() {
    AmetistaEngine.intake()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.logo),
            state = rememberWindowState(
                placement = WindowPlacement.Maximized
            )
        ) {
            setUpSession {
                localUser.clear()
                navigator.navigate(SPLASHSCREEN)
            }
            App()
        }
    }
}