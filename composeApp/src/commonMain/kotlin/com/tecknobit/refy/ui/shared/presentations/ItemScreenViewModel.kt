package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyItem
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class ItemScreenViewModel<I : RefyItem>(
    private val itemId: String
) : RefyScreenViewModel(), LinksRetriever<RefyLinkImpl> {

    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    abstract fun retrieveItem()

    override val linksState: PaginationState<Int, RefyLinkImpl> = PaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadLinks(
                page = page
            )
        }
    )

    override fun refresh() {
        linksState.refresh()
    }

    abstract fun removeLink(
        link: RefyLinkImpl
    )

}