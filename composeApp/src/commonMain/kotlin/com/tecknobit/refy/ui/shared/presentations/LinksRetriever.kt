package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.refy.ui.screens.links.data.RefyLink
import io.github.ahmad_hamwi.compose.pagination.PaginationState

/**
 * The `LinksRetriever` interface is used to retrieve the links from the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @param L The type of the link to retrieve
 */
interface LinksRetriever<L : RefyLink> {

    /**
     *`linksState` the state used to handle the pagination of the links list
     */
    val linksState: PaginationState<Int, L>

    /**
     * Method used to load and retrieve the links to add in the [linksState]
     *
     * @param page The page to request
     */
    fun loadLinks(
        page: Int
    )

}