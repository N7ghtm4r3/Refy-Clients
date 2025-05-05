@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.links.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.AttachLink
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.DeleteLink
import com.tecknobit.refy.ui.components.links.LinkCardContainer
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl

/**
 * Custom card used to display the information of a [RefyLinkImpl]
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param link The link to display
 */
@Wrapper
@Composable
@NonRestartableComposable
fun LinkCard(
    modifier: Modifier = Modifier,
    viewModel: LinksScreenViewModel,
    link: RefyLinkImpl
) {
    LinkCardContainer(
        modifier = modifier,
        viewModel = viewModel,
        link = link,
        extraButton = if (link.iAmTheOwner()) {
            {
                AttachItemButton(
                    attachItemContent = { state, scope ->
                        AttachLink(
                            state = state,
                            scope = scope,
                            viewModel = viewModel,
                            link = link
                        )
                    }
                )
            }
        } else
            null,
        cancelButton = {
            DeleteItemButton(
                modifier = Modifier
                    .weight(1f),
                item = link,
                deleteContent = { delete ->
                    DeleteLink(
                        show = delete,
                        viewModel = viewModel,
                        link = link
                    )
                }
            )
        }
    )
}