package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.refy.UPSERT_LINK_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.screens.links.components.LinkCard
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.BaseLinksScreen
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.links

/**
 * The [LinksScreen] class is the screen where are displayed all the links the [com.tecknobit.refy.localUser]
 * has available
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemsScreen
 * @see BaseLinksScreen
 */
class LinksScreen : BaseLinksScreen<RefyLinkImpl, LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    /**
     * The custom card used to display the information of the [link]
     *
     * @param link The link to display
     */
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

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate(UPSERT_LINK_SCREEN)
    }

}