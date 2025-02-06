package com.tecknobit.refy.helpers

import android.content.Intent
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel

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
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.reference}")
    ContextActivityProvider.getCurrentActivity()?.startActivity(Intent.createChooser(intent, null))
}