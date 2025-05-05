package com.tecknobit.refy.ui.screens.team.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.LinksRetriever
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import com.tecknobit.refycore.enums.TeamRole
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `TeamScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.team.presenter.TeamScreen]
 *
 * @param teamId The identifier of the team
 * @param teamName The name of the team
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see LinksRetriever
 * @see TeamsManager
 *
 */
class TeamScreenViewModel(
    private val teamId: String,
    teamName: String,
) : ItemScreenViewModel<Team>(
    itemId = teamId,
    name = teamName
), TeamsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override val requestsScope: CoroutineScope = viewModelScope

    /**
     * Method to retrieve the information of the item to display
     */
    override fun retrieveItem() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getTeam(
                        teamId = teamId
                    )
                },
                onSuccess = {
                    setServerOfflineValue(false)
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                    _itemName.value = _item.value!!.title
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    /**
     *`teamCollections` the state used to handle the pagination of the collections shared with the team list
     */
    val teamCollections = PaginationState<Int, LinksCollection>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCollections(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the collections shared with the team to append to the [teamCollections]
     *
     * @param page The page to request
     */
    private fun loadCollections(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getTeamCollections(
                        teamId = teamId,
                        page = page
                    )
                },
                serializer = LinksCollection.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    teamCollections.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it) },
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
                    getTeamLinks(
                        teamId = teamId,
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
                onFailure = { showSnackbarMessage(it) },
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
     * Method to refresh the data after the collections have been attached
     */
    override fun refreshAfterCollectionsAttached() {
        teamCollections.refresh()
        retrieveItem()
    }

    /**
     * Method to remove a collection shared with the team
     *
     * @param collection The collection to remove
     */
    fun removeCollection(
        collection: LinksCollection
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    removeCollectionFromTeam(
                        teamId = teamId,
                        collection = collection
                    )
                },
                onSuccess = { teamCollections.refresh() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
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
                    removeLinkFromTeam(
                        teamId = teamId,
                        link = link
                    )
                },
                onSuccess = { linksState.refresh() },
                onFailure = { showSnackbarMessage(it) }
            )
        }

    }

    /**
     * Method to change the role of the member
     *
     * @param member The member to change his/her role
     * @param role The new role to set
     * @param onChange The action to execute when the role changed
     */
    fun changeMemberRole(
        member: TeamMember,
        role: TeamRole,
        onChange: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    changeMemberRole(
                        teamId = teamId,
                        member = member,
                        role = role
                    )
                },
                onSuccess = {
                    onChange()
                    retrieveItem()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to remove a member
     *
     * @param member The member to change his/her role
     * @param onRemove The action to execute when the user has been removed
     */
    fun removeMember(
        member: TeamMember,
        onRemove: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    removeMember(
                        teamId = teamId,
                        member = member,
                    )
                },
                onSuccess = {
                    onRemove()
                    retrieveItem()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to leave from the team
     *
     * @param onLeave The action to execute when the member leaves
     */
    fun leaveTeam(
        onLeave: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    leave(
                        teamId = teamId
                    )
                },
                onSuccess = { onLeave() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}