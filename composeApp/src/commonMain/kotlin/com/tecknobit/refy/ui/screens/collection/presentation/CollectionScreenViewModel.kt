package com.tecknobit.refy.ui.screens.collection.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.navigator
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.LinksRetriever
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `CollectionScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.collection.presenter.CollectionScreen]
 *
 * @param collectionId The identifier of the collection
 * @param collectionName The name of the collection
 * @param collectionColor The color of the collection
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see ItemScreenViewModel
 * @see LinksRetriever
 * @see CollectionsManager
 *
 */
class CollectionScreenViewModel(
    private val collectionId: String,
    collectionName: String,
    collectionColor: String
) : ItemScreenViewModel<LinksCollection>(
    itemId = collectionId,
    name = collectionName
), LinksRetriever<RefyLinkImpl>, CollectionsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override val requestsScope: CoroutineScope = viewModelScope

    /**
     *`_color` the color of the collection
     */
    private val _color = MutableStateFlow(
        value = collectionColor.toColor()
    )
    val color: StateFlow<Color> = _color

    /**
     * Method to retrieve the information of the item to display
     */
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
                    _itemName.value = _item.value!!.title
                    _color.value = _item.value!!.color.toColor()
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     *`collectionTeams` the state used to handle the pagination of the teams where the collection
     * is shared
     */
    val collectionTeams = PaginationState<Int, Team>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCollectionTeams(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the teams where the collection is shared to append to the [collectionTeams]
     *
     * @param page The page to request
     */
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

    /**
     * Method used to load and retrieve the links to append to the [linksState]
     *
     * @param page The page to request
     */
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

    /**
     * Method to refresh the data after the links have been attached
     */
    override fun refreshAfterLinksAttached() {
        linksState.refresh()
        retrieveItem()
    }

    /**
     * Method to refresh the data after the collection has been shared with teams
     */
    override fun refreshAfterTeamsSharing() {
        collectionTeams.refresh()
        retrieveItem()
    }

    /**
     * Method to remove a link contained by the [_item]
     *
     * @param link The link to remove
     */
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

    /**
     * Method to remove a team from the collection
     *
     * @param team The team to remove
     */
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