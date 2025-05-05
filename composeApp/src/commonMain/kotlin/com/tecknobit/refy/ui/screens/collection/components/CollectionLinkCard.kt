package com.tecknobit.refy.ui.screens.collection.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.refy.ui.components.RemoveItemButton
import com.tecknobit.refy.ui.components.links.LinkCardContainer
import com.tecknobit.refy.ui.screens.collection.presentation.CollectionScreenViewModel
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl

/**
 * Custom card used to display the information of a link shared with the [collection]
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param collection The collection to where the link is shared
 * @param showOwnerData Whether show the data of the owner
 * @param link The link to display
 */
@Wrapper
@Composable
@NonRestartableComposable
fun CollectionLinkCard(
    modifier: Modifier = Modifier,
    viewModel: CollectionScreenViewModel,
    collection: LinksCollection,
    showOwnerData: Boolean,
    link: RefyLinkImpl
) {
    LinkCardContainer(
        modifier = modifier,
        viewModel = viewModel,
        link = link,
        showOwnerData = showOwnerData,
        cancelButton = {
            if (collection.iAmTheOwner()) {
                RemoveItemButton(
                    removeAction = {
                        viewModel.removeLink(
                            link = link
                        )
                    }
                )
            }
        }
    )
}