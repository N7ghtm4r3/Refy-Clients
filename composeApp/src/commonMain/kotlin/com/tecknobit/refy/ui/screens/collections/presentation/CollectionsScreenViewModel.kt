package com.tecknobit.refy.ui.screens.collections.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CollectionsScreenViewModel : RefyScreenViewModel(), CollectionsManager {

    override val requestsScope: CoroutineScope = viewModelScope

    val collectionsState = PaginationState<Int, LinksCollection>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCollections(
                page = page
            )
        }
    )

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

    override fun refresh() {
        collectionsState.refresh()
    }

    override fun refreshAfterLinksAttached() {
        refresh()
    }

    override fun refreshAfterTeamsSharing() {
        refresh()
    }

}