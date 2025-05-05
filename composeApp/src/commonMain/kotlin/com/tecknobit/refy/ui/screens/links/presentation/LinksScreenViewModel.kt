package com.tecknobit.refy.ui.screens.links.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.LinksRetriever
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import kotlinx.coroutines.launch

/**
 * The `LinksScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.links.presenter.LinksScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see LinksRetriever
 * @see BaseLinksScreenViewModel
 */
class LinksScreenViewModel : BaseLinksScreenViewModel<RefyLinkImpl>() {

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

    /**
     * Method to share a link with collections
     *
     * @param link The link to share
     * @param collections The collections where share the link
     * @param afterShared The action to execute after the link has been shared
     */
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

    /**
     * Method to share a link with teams
     *
     * @param link The link to share
     * @param teams The teams where share the link
     * @param afterShared The action to execute after the link has been shared
     */
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

    /**
     * Method to request to delete a link
     *
     * @param link The link to delete
     * @param onDelete The action to execute when the links has been deleted
     */
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