package com.tecknobit.refy.ui.screens.team.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.refy.ui.components.RemoveItemButton
import com.tecknobit.refy.ui.components.links.LinkCardContainer
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel

@Wrapper
@Composable
@NonRestartableComposable
fun TeamLinkCard(
    modifier: Modifier = Modifier,
    viewModel: TeamScreenViewModel,
    link: RefyLinkImpl
) {
    LinkCardContainer(
        modifier = modifier,
        viewModel = viewModel,
        link = link,
        showOwnerData = true,
        cancelButton = {
            if (link.iAmTheOwner()) {
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