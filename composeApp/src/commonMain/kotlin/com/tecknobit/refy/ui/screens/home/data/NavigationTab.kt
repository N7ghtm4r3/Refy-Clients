package com.tecknobit.refy.ui.screens.home.data

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

/**
 * The `NavigationTab` data class contains the information related to a tab used for the navigation
 *
 * @property title The title of the tab
 * @property icon The representative icon of the tab
 *
 * @author N7ghtm4r3 - Tecknobit
 */
data class NavigationTab(
    val title: StringResource,
    val icon: ImageVector
)
