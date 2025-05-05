package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.refy.ui.shared.data.RefyLink

/**
 * Method to share the link
 *
 * @param viewModel The support viewmodel of the screen
 * @param link The link to share
 */
expect fun shareLink(
    viewModel: EquinoxViewModel,
    link: RefyLink
)