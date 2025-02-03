package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel

class LinksScreen : EquinoxScreen<LinksScreenViewModel>(
    viewModel = LinksScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}