package com.tecknobit.refy.ui.screens.teams.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.teams

class TeamsScreen : RefyScreen<TeamsScreenViewModel>(
    title = Res.string.teams,
    viewModel = TeamsScreenViewModel()
) {
    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}