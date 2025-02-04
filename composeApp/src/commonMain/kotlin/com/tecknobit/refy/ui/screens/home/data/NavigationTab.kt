package com.tecknobit.refy.ui.screens.home.data

import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import org.jetbrains.compose.resources.StringResource

data class NavigationTab(
    val title: StringResource,
    val icon: ImageVector,
    val screen: RefyScreen<*>
)
