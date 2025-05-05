package com.tecknobit.refy.ui.screens.collections.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The `CollectionsScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.collections.presenter.CollectionsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see CollectionsManager
 */
class CollectionsScreenViewModel : RefyScreenViewModel(), CollectionsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override val requestsScope: CoroutineScope = viewModelScope

    /**
     *`collectionsState` the state used to handle the pagination of the collections list
     */
    val collectionsState = PaginationState<Int, LinksCollection>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCollections(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the collections to append to the [collectionsState]
     *
     * @param page The page to request
     */
    private fun loadCollections(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getCollections(
                        page = page,
                        keywords = keywords.value
                    )
                },
                serializer = LinksCollection.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    collectionsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     * Method used to refresh the data displayed by the screen
     */
    override fun refresh() {
        collectionsState.refresh()
    }

    /**
     * Method to refresh the data after the links have been attached
     */
    override fun refreshAfterLinksAttached() {
        refresh()
    }

    /**
     * Method to refresh the data after the collection has been shared with teams
     */
    override fun refreshAfterTeamsSharing() {
        refresh()
    }

}