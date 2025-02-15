package com.tecknobit.refy.ui.screens.team.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import com.tecknobit.refycore.enums.TeamRole
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class TeamScreenViewModel(
    private val teamId: String
) : ItemScreenViewModel<Team>(
    itemId = teamId
), TeamsManager {

    override val requestsScope: CoroutineScope = viewModelScope

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
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    val teamCollections = PaginationState<Int, LinksCollection>(
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

    override fun refreshAfterLinksAttached() {
        linksState.refresh()
        retrieveItem()
    }

    override fun refreshAfterCollectionsAttached() {
        teamCollections.refresh()
        retrieveItem()
    }

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