package com.tecknobit.refy.ui.screens.collection.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.navigator
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.LinksRetriever
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class CollectionScreenViewModel(
    private val collectionId: String
) : ItemScreenViewModel<LinksCollection>(
    itemId = collectionId
), LinksRetriever<RefyLinkImpl>, CollectionsManager {

    override val requestsScope: CoroutineScope = viewModelScope

    override fun retrieveItem() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getCollection(
                        collectionId = collectionId
                    )
                },
                onSuccess = {
                    setServerOfflineValue(false)
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    val collectionTeams = PaginationState<Int, Team>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCollectionTeams(
                page = page
            )
        }
    )

    private fun loadCollectionTeams(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getCollectionTeams(
                        collectionId = collectionId,
                        page = page,
                    )
                },
                serializer = Team.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    collectionTeams.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { navigator.goBack() },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    override fun loadLinks(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getCollectionLinks(
                        collectionId = collectionId,
                        page = page,
                        keywords = keywords.value
                    )
                },
                serializer = RefyLinkImpl.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    linksState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { navigator.goBack() },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    override fun refreshAfterLinksAttached() {
        linksState.refresh()
    }

    override fun refreshAfterTeamsAttached() {
        collectionTeams.refresh()
    }

    override fun removeLink(
        link: RefyLinkImpl
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    removeLinkFromCollection(
                        collectionId = collectionId,
                        link = link
                    )
                },
                onSuccess = { linksState.refresh() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun removeTeam(
        team: Team
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    removeTeamFromCollection(
                        collectionId = collectionId,
                        team = team
                    )
                },
                onSuccess = { collectionTeams.refresh() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}