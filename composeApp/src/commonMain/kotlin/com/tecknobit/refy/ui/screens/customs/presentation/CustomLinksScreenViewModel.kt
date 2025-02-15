package com.tecknobit.refy.ui.screens.customs.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import kotlinx.coroutines.launch

class CustomLinksScreenViewModel : BaseLinksScreenViewModel<CustomRefyLink>() {

    override fun loadLinks(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getCustomLinks(
                        page = page,
                        keywords = keywords.value
                    )
                },
                serializer = CustomRefyLink.serializer(),
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

    override fun deleteLink(
        link: CustomRefyLink,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteCustomLink(
                        link = link
                    )
                },
                onSuccess = {
                    onDelete()
                    refresh()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}