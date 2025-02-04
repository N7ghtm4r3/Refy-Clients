package com.tecknobit.refy.ui.screens.customs.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.refy.ui.screens.customs.presentation.CustomLinksScreenViewModel
import com.tecknobit.refy.ui.screens.shared.screens.RefyScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.custom

class CustomLinksScreen : RefyScreen<CustomLinksScreenViewModel>(
    title = Res.string.custom,
    viewModel = CustomLinksScreenViewModel()
) {
    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}