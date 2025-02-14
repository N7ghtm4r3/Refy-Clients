package com.tecknobit.refy.ui.screens.teams.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TeamsScreenViewModel : RefyScreenViewModel(), TeamsManager {

    override val requestsScope: CoroutineScope = viewModelScope

    val teamsState = PaginationState<Int, Team>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadTeams(
                page = page
            )
        }
    )

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

    override fun refresh() {
        teamsState.refresh()
    }

    override fun refreshAfterLinksAttached() {
        refresh()
    }

    override fun refreshAfterCollectionsAttached() {
        refresh()
    }

}