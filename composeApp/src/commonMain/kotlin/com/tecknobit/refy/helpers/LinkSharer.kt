package com.tecknobit.refy.helpers

import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel

/**
 * Method to share the link
 *
 * @param viewModel The support viewmodel of the screen
 * @param link The link to share
 */
expect fun shareLink(
    viewModel: LinksScreenViewModel,
    link: RefyLink
)