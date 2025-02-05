package com.tecknobit.refy.ui.screens.teams.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.create
import refy.composeapp.generated.resources.teams

class TeamsScreen : RefyScreen<TeamsScreenViewModel>(
    title = Res.string.teams,
    viewModel = TeamsScreenViewModel()
) {

    override fun createAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun createText(): StringResource {
        return Res.string.create
    }

    override fun createIcon(): ImageVector {
        return Icons.Default.GroupAdd
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}