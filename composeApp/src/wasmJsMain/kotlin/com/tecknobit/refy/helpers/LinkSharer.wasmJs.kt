package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.utilities.copyOnClipboard
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.link_copied

/**
 * Method to share the link
 *
 * @param viewModel The support viewmodel of the screen
 * @param link The link to share
 */
actual fun shareLink(
    viewModel: LinksScreenViewModel,
    link: RefyLink
) {
    copyOnClipboard(
        content = link.reference,
        onCopy = {
            viewModel.showSnackbarMessage(
                message = Res.string.link_copied
            )
        }
    )
}