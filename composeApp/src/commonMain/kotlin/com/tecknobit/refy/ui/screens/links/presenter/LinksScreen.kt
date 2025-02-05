@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.links.components.LinkCard
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.screens.RefyScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add
import refy.composeapp.generated.resources.links
import refy.composeapp.generated.resources.no_links_yet

class LinksScreen : RefyScreen<LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            viewModel = viewModel,
            content = { LinksSection() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        ResponsiveContent(
            onExpandedSizeClass = { LinksGrid() },
            onMediumSizeClass = { LinksGrid() },
            onCompactSizeClass = { LinksList() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinksGrid() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaginatedLazyVerticalGrid(
                modifier = Modifier
                    .widthIn(
                        max = MAX_CONTAINER_WIDTH
                    ),
                paginationState = viewModel.linksState,
                columns = GridCells.Adaptive(
                    minSize = 325.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                firstPageProgressIndicator = { FirstPageProgressIndicator() },
                newPageProgressIndicator = { NewPageProgressIndicator() },
                firstPageEmptyIndicator = { EmptyLinks() }
            ) {
                items(
                    items = viewModel.linksState.allItems!!,
                    key = { link -> link.id }
                ) { link ->
                    LinkCard(
                        link = link
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun LinksList() {
        PaginatedLazyColumn(
            paginationState = viewModel.linksState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { EmptyLinks() }
        ) {
            items(
                items = viewModel.linksState.allItems!!,
                key = { link -> link.id }
            ) { link ->
                LinkCard(
                    link = link
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun EmptyLinks() {
        EmptyListUI(
            icon = Icons.Default.LinkOff,
            subText = Res.string.no_links_yet
        )
    }

    override fun createAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun createText(): StringResource {
        return Res.string.add
    }

    override fun createIcon(): ImageVector {
        return Icons.Default.AddLink
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}