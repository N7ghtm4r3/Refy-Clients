package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.refy.ui.screens.links.data.RefyLink
import io.github.ahmad_hamwi.compose.pagination.PaginationState

interface LinksRetriever<T : RefyLink> {

    val linksState: PaginationState<Int, T>

    fun loadLinks(
        page: Int
    )

}