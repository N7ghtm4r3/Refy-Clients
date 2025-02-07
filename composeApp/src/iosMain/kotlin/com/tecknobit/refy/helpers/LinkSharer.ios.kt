package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

/**
 * Method to share the link
 *
 * @param viewModel The support viewmodel of the screen
 * @param link The link to share
 */
actual fun shareLink(
    viewModel: EquinoxViewModel,
    link: RefyLink
) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val activityViewController = UIActivityViewController(link.reference, null)
    viewController?.presentViewController(activityViewController, true, null)
}