package com.tecknobit.refy.ui.screens.links.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import kotlinx.coroutines.launch

class LinksScreenViewModel : BaseLinksScreenViewModel<RefyLinkImpl>() {

    override fun loadLinks(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                serializer = RefyLinkImpl.serializer(),
                request = {
                    getLinks(
                        page = page,
                        keywords = keywords.value
                    )
                },
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    linksState.appendPage(
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

    fun shareLinkWithCollections(
        link: RefyLinkImpl,
        collections: List<LinksCollection>,
        afterShared: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    shareLinkWithCollections(
                        link = link,
                        collections = collections.map { collection -> collection.id }
                    )
                },
                onSuccess = { afterShared() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun shareLinkWithTeams(
        link: RefyLinkImpl,
        teams: List<Team>,
        afterShared: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    shareLinkWithTeams(
                        link = link,
                        teams = teams.map { team -> team.id }
                    )
                },
                onSuccess = { afterShared() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun deleteLink(
        link: RefyLinkImpl,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteLink(
                        link = link
                    )
                },
                onSuccess = {
                    refresh()
                    onDelete()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}