package com.tecknobit.refy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.annotation.ContentView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider
import io.github.vinceglb.filekit.core.FileKit

/**
 * The [MainActivity] is used as entry point of Refy's application for Android
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 *
 */
class MainActivity : ComponentActivity() {

    companion object {

        /**
         * `appUpdateManager` the manager to check if there is an update available
         */
        lateinit var appUpdateManager: AppUpdateManager

        /**
         * `launcher` the result registered for [appUpdateManager] and the action to execute if fails
         */
        lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    }

    init {
        launcher = registerForActivityResult(StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK)
                startSession()
        }
    }

    /**
     * {@inheritDoc}
     *
     * If your ComponentActivity is annotated with [ContentView], this will call [setContentView]
     * for you.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        ContextActivityProvider.setCurrentActivity(this)
        FileKit.init(this)
        AmetistaEngine.intake()
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = MaterialTheme.colorScheme.primary.toArgb(),
                    darkScrim = MaterialTheme.colorScheme.inversePrimary.toArgb()
                )
            )
            setUpSession {
                localUser.clear()
                navigator.navigate(SPLASHSCREEN)
            }
            App()
        }
    }

}

