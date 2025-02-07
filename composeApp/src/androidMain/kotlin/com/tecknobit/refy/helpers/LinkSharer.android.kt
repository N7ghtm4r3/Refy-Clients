package com.tecknobit.refy.helpers

import android.content.Intent
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider
import com.tecknobit.refy.ui.screens.links.data.RefyLink

/**
 * `INTENT_TYPE` the type of the intent to apply to correctly share the link
 */
private const val INTENT_TYPE = "text/plain"

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
    val intent = Intent()
    intent.type = INTENT_TYPE
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, "${link.title}\n${link.reference}")
    ContextActivityProvider.getCurrentActivity()?.startActivity(Intent.createChooser(intent, null))
}