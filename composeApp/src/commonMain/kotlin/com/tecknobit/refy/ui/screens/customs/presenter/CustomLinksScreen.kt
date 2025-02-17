package com.tecknobit.refy.ui.screens.customs.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.refy.UPSERT_CUSTOM_LINK_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.screens.customs.components.CustomLinkCard
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.customs.presentation.CustomLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.BaseLinksScreen
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.custom

/**
 * The [BaseLinksScreen] class is the base screen used to display the custom links of the [com.tecknobit.refy.localUser]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemsScreen
 * @see BaseLinksScreen
 */
class CustomLinksScreen : BaseLinksScreen<CustomRefyLink, CustomLinksScreenViewModel>(
    title = Res.string.custom,
    viewModel = CustomLinksScreenViewModel()
) {

    /**
     * The custom card used to display the information of the [link]
     *
     * @param link The link to display
     */
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

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate(UPSERT_CUSTOM_LINK_SCREEN)
    }

}