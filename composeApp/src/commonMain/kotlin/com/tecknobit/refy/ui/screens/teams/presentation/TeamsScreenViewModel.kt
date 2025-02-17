package com.tecknobit.refy.ui.screens.teams.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The `TeamsScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.teams.presenter.TeamsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see TeamsManager
 */
class TeamsScreenViewModel : RefyScreenViewModel(), TeamsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    override val requestsScope: CoroutineScope = viewModelScope

    /**
     *`teamsState` the state used to handle the pagination of the teams list
     */
    val teamsState = PaginationState<Int, Team>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadTeams(
                page = page
            )
        }
    )

    /**
     * Method used to load and retrieve the teams to append to the [teamsState]
     *
     * @param page The page to request
     */
    private fun loadTeams(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getTeams(
                        page = page,
                        keywords = keywords.value
                    )
                },
                serializer = Team.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    teamsState.appendPage(
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
        teamsState.refresh()
    }

    /**
     * Method to refresh the data after the links have been attached
     */
    override fun refreshAfterLinksAttached() {
        refresh()
    }

    /**
     * Method to refresh the data after the collections have been attached
     */
    override fun refreshAfterCollectionsAttached() {
        refresh()
    }

}