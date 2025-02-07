package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import io.github.ahmad_hamwi.compose.pagination.PaginationState

interface LinksRetriever {

    val linksState: PaginationState<Int, RefyLinkImpl>

    fun loadLinks(
        page: Int
    )

}