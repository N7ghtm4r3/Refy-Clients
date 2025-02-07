package com.tecknobit.refy.ui.screens.collection.helpers

import androidx.compose.runtime.Composable

/**
 * Method used to adapt the status bar of the application according to the collection theme set
 */
@Composable
expect fun adaptStatusBarToCollectionTheme()

/**
 * Method used to adapt the status bar of the application to the default theme of the application.
 *
 * It will just be invoked inside a composable inside the [com.tecknobit.refy.ui.theme.RefyTheme] method
 * with the [androidx.compose.material3.MaterialTheme] instance restored to the default application
 * theme value
 */
@Composable
fun restoreDefaultApplicationThemeStatusBar() {
    adaptStatusBarToCollectionTheme()
}