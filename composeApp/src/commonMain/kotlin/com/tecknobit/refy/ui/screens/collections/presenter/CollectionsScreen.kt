package com.tecknobit.refy.ui.screens.collections.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.collections.presentation.CollectionsScreenViewModel
import com.tecknobit.refy.ui.shared.screens.RefyScreen
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.create

class CollectionsScreen : RefyScreen<CollectionsScreenViewModel>(
    title = Res.string.collections,
    viewModel = CollectionsScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
    }

    override fun createAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun createText(): StringResource {
        return Res.string.create
    }

    override fun createIcon(): ImageVector {
        return Icons.Default.CreateNewFolder
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}