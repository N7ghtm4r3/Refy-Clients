package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import io.github.ahmad_hamwi.compose.pagination.PaginationState

@Structure
abstract class BaseLinksScreenViewModel<L : RefyLink> : RefyScreenViewModel(), LinksRetriever<L> {

    override val linksState: PaginationState<Int, L> = PaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadLinks(
                page = page
            )
        }
    )

    abstract fun deleteLink(
        link: L,
        onDelete: () -> Unit
    )

    override fun refresh() {
        linksState.refresh()
    }

}