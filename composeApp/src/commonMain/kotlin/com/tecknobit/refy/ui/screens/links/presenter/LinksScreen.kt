package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.screens.RefyScreen
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add
import refy.composeapp.generated.resources.links

class LinksScreen : RefyScreen<LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

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