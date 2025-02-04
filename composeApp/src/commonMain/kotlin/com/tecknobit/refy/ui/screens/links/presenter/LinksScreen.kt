package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.links

class LinksScreen : RefyScreen<LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}