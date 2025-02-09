package com.tecknobit.refy.ui.screens.customs.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.refy.ui.screens.customs.components.CustomLinkCard
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.customs.presentation.CustomLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.BaseLinksScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.custom

class CustomLinksScreen : BaseLinksScreen<CustomRefyLink, CustomLinksScreenViewModel>(
    title = Res.string.custom,
    viewModel = CustomLinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun RelatedLinkCard(
        link: CustomRefyLink
    ) {
        CustomLinkCard(
            viewModel = viewModel,
            link = link
        )
    }

    override fun upsertAction() {
        // TODO: TO NAV TO CREATE
    }

}