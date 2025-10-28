@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.components.links

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.onClick
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton.Companion.Secondary
import androidx.compose.ui.platform.UriHandler
import com.tecknobit.refy.ui.shared.data.RefyLink

/**
 * Method use to handle the `Close Application on Link Open` feature
 *
 * @param uriHandler The handler used to open the links
 * @param link The link to open
 *
 * @return the modifier to apply to the component as [Modifier]
 *
 * @since 1.1.0
 */
internal actual fun Modifier.handleCloseOnLinkOpen(
    uriHandler: UriHandler,
    link: RefyLink,
): Modifier = this.onClick(
    matcher = PointerMatcher.mouse(Secondary),
    onClick = {
        println("hi")
        uriHandler.openUri(
            uri = link.reference
        )
    }
)