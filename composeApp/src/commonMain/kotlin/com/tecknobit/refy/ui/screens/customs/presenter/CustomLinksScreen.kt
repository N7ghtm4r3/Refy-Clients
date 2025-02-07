package com.tecknobit.refy.ui.screens.customs.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.customs.presentation.CustomLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add
import refy.composeapp.generated.resources.custom

class CustomLinksScreen : RefyScreen<CustomLinksScreenViewModel>(
    title = Res.string.custom,
    viewModel = CustomLinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
    }

    override fun createAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun createText(): StringResource {
        return Res.string.add
    }

    override fun createIcon(): ImageVector {
        return Icons.Default.AddLink
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}