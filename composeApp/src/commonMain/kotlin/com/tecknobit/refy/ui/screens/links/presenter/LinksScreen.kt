@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.links.components.LinkCard
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.screens.RefyScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add
import refy.composeapp.generated.resources.links
import refy.composeapp.generated.resources.no_links_yet
import refy.composeapp.generated.resources.search_by_keywords

class LinksScreen : RefyScreen<LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            viewModel = viewModel,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FiltersBar()
                    ResponsiveContent(
                        onExpandedSizeClass = { LinksGrid() },
                        onMediumSizeClass = { LinksGrid() },
                        onCompactSizeClass = { LinksList() }
                    )
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    override fun RowScope.Filters() {
        IconButton(
            onClick = {
                filtersEnabled.value = !filtersEnabled.value
                if (!filtersEnabled.value) {
                    viewModel.keywords.value = ""
                    viewModel.linksState.refresh()
                }
            }
        ) {
            Icon(
                imageVector = if (filtersEnabled.value)
                    Icons.Default.FilterListOff
                else
                    Icons.Default.FilterList,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FiltersBar() {
        AnimatedVisibility(
            visible = filtersEnabled.value
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = responsiveAssignment(
                    onExpandedSizeClass = { Alignment.CenterHorizontally },
                    onMediumSizeClass = { Alignment.CenterHorizontally },
                    onCompactSizeClass = { Alignment.Start }
                )
            ) {
                EquinoxOutlinedTextField(
                    shape = RoundedCornerShape(
                        size = 12.dp
                    ),
                    value = viewModel.keywords,
                    onValueChange = {
                        viewModel.keywords.value = it
                        viewModel.linksState.refresh()
                    },
                    placeholder = Res.string.search_by_keywords
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun LinksGrid() {
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
                paginationState = viewModel.linksState,
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
                    items = viewModel.linksState.allItems!!,
                    key = { link -> link.id }
                ) { link ->
                    LinkCard(
                        viewModel = viewModel,
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
            modifier = Modifier
                .animateContentSize(),
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
                    viewModel = viewModel,
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
        filtersEnabled = remember { mutableStateOf(false) }
        viewModel.keywords = remember { mutableStateOf("") }
    }

}