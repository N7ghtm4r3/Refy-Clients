package com.tecknobit.refy.ui.screens.collections.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.components.EmptyCollections
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.collections.components.CollectionCard
import com.tecknobit.refy.ui.screens.collections.presentation.CollectionsScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.create

class CollectionsScreen : ItemsScreen<CollectionsScreenViewModel>(
    title = Res.string.collections,
    viewModel = CollectionsScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun ItemsGrid() {
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
                paginationState = viewModel.collectionsState,
                columns = StaggeredGridCells.Adaptive(
                    minSize = 325.dp
                ),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                firstPageProgressIndicator = { FirstPageProgressIndicator() },
                newPageProgressIndicator = { NewPageProgressIndicator() },
                firstPageEmptyIndicator = {
                    EmptyCollections(
                        size = 400.dp
                    )
                }
            ) {
                items(
                    items = viewModel.collectionsState.allItems!!,
                    key = { collection -> collection.id }
                ) { collection ->
                    CollectionCard(
                        viewModel = viewModel,
                        collection = collection
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ItemsList() {
        PaginatedLazyColumn(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel.collectionsState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { EmptyCollections() }
        ) {
            items(
                items = viewModel.collectionsState.allItems!!,
                key = { collection -> collection.id }
            ) { collection ->
                CollectionCard(
                    viewModel = viewModel,
                    collection = collection
                )
            }
        }
    }

    override fun createAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun createText(): StringResource {
        return Res.string.create
    }

    override fun createIcon(): ImageVector {
        return Icons.Default.CreateNewFolder
    }

}