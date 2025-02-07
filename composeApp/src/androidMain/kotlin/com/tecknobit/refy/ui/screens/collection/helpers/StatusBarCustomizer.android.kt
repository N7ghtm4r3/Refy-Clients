package com.tecknobit.refy.ui.screens.collection.helpers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Method used to adapt the status bar of the application according to the collection theme set
 */
@Composable
actual fun adaptStatusBarToCollectionTheme() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.primary
    )
}