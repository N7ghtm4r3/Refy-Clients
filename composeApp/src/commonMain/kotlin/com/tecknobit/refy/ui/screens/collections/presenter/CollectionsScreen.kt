package com.tecknobit.refy.ui.screens.collections.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.refy.ui.screens.collections.presentation.CollectionsScreenViewModel
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections

class CollectionsScreen : RefyScreen<CollectionsScreenViewModel>(
    title = Res.string.collections,
    viewModel = CollectionsScreenViewModel()
) {
    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}