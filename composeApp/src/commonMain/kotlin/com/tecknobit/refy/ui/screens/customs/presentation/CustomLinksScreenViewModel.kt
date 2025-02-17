package com.tecknobit.refy.ui.screens.customs.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.LinksRetriever
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import kotlinx.coroutines.launch

/**
 * The `CustomLinksScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.customs.presenter.CustomLinksScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see BaseLinksScreenViewModel
 * @see LinksRetriever
 *
 */
class CustomLinksScreenViewModel : BaseLinksScreenViewModel<CustomRefyLink>() {

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

    /**
     * Method to request to delete a link
     *
     * @param link The link to delete
     * @param onDelete The action to execute when the links has been deleted
     */
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