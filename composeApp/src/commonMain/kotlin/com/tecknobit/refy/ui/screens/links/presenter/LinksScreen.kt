package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.refy.ui.screens.links.components.LinkCard
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.BaseLinksScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.links

class LinksScreen : BaseLinksScreen<RefyLinkImpl, LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun RelatedLinkCard(
        link: RefyLinkImpl
    ) {
        LinkCard(
            viewModel = viewModel,
            link = link
        )
    }

    override fun upsertAction() {
        // TODO: TO NAV TO CREATE
    }

}