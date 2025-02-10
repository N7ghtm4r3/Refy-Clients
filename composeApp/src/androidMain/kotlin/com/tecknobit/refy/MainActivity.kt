package com.tecknobit.refy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextActivityProvider.setCurrentActivity(this)
        FileKit.init(this)
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = MaterialTheme.colorScheme.primary.toArgb(),
                    darkScrim = MaterialTheme.colorScheme.inversePrimary.toArgb()
                )
            )
            App()
        }
    }
}

