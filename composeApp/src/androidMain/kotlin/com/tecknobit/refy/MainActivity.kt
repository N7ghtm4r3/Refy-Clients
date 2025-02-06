package com.tecknobit.refy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextActivityProvider.setCurrentActivity(this)
        setContent {
            App()
        }
    }
}

