@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.refy.ui.components.links

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.refy.ui.components.EmptyLinks
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.shared.data.RefyLink
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import io.github.ahmad_hamwi.compose.pagination.PaginationState

/**
 * Custom layout used to display a list of links as grid
 *
 * @param linksState The state used to load the links
 * @param linkCard The card used to display the link details
 */
@Composable
fun <T : RefyLink> LinksGrid(
    linksState: PaginationState<Int, T>,
    linkCard: @Composable (T) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaginatedLazyVerticalStaggeredGrid(
            modifier = Modifier
                .responsiveMaxWidth()
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

