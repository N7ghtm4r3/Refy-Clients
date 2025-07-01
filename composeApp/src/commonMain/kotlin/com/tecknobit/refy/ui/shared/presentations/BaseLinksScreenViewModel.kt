@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.runtime.ExperimentalComposeApi
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.ui.shared.data.RefyLink
import io.github.ahmad_hamwi.compose.pagination.PaginationState

/**
 * The `BaseLinksScreenViewModel` class is the support class used to handle the links list displayed
 * by a screen which have to display that links list
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see LinksRetriever
 *
 * @param L The type of the link to retrieve
 */
@Structure
abstract class BaseLinksScreenViewModel<L : RefyLink> : RefyScreenViewModel(), LinksRetriever<L> {

    /**
     *`linksState` the state used to handle the pagination of the links list
     */
    override val linksState: PaginationState<Int, L> = PaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadLinks(
                page = page
            )
        }
    )

    /**
     * Method to request to delete a link
     *
     * @param link The link to delete
     * @param onDelete The action to execute when the links has been deleted
     */
    abstract fun deleteLink(
        link: L,
        onDelete: () -> Unit,
    )

    /**
     * Method used to refresh the data displayed by the screen
     */
    override fun refresh() {
        linksState.refresh()
    }

    /**
     * Method used to reload the content related to data to retrieve that gone on error during the
     * retrieving
     */
    override fun reload() {
        linksState.retryLastFailedRequest()
    }

    /**
     * Method used to notify about a server offline status
     */
    override fun notifyServerOffline() {
        linksState.setError(Exception())
        sessionFlowState.notifyServerOffline()
    }

}