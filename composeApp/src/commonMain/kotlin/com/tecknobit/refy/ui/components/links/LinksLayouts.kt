package com.tecknobit.refy.ui.components.links

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen.Companion.MAX_CONTAINER_WIDTH
import com.tecknobit.refy.ui.components.EmptyLinks
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import io.github.ahmad_hamwi.compose.pagination.PaginationState

@Composable
@NonRestartableComposable
fun LinksGrid(
    linksState: PaginationState<Int, RefyLinkImpl>,
    linkCard: @Composable() (RefyLinkImpl) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyVerticalStaggeredGrid(
            modifier = Modifier
                .widthIn(
                    max = MAX_CONTAINER_WIDTH
                )
                .animateContentSize(),
            paginationState = linksState,
            columns = StaggeredGridCells.Adaptive(
                minSize = 325.dp
            ),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { EmptyLinks() }
        ) {
            items(
                items = linksState.allItems!!,
                key = { link -> link.id }
            ) { link ->
                linkCard(link)
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun LinksList(
    linksState: PaginationState<Int, RefyLinkImpl>,
    linkCard: @Composable() (RefyLinkImpl) -> Unit
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .animateContentSize(),
        paginationState = linksState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() },
        firstPageEmptyIndicator = { EmptyLinks() }
    ) {
        items(
            items = linksState.allItems!!,
            key = { link -> link.id }
        ) { link ->
            linkCard(link)
        }
    }
}