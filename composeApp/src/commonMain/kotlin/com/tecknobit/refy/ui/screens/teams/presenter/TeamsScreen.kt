package com.tecknobit.refy.ui.screens.teams.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.create
import refy.composeapp.generated.resources.teams

class TeamsScreen : RefyScreen<TeamsScreenViewModel>(
    title = Res.string.teams,
    viewModel = TeamsScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
    }

    override fun upsertAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun upsertText(): StringResource {
        return Res.string.create
    }

    override fun upsertIcon(): ImageVector {
        return Icons.Default.GroupAdd
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}