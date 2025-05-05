@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.refy.ui.screens.collections.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.refy.UPSERT_COLLECTION_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.EmptyCollections
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.collection.helpers.restoreDefaultApplicationThemeStatusBar
import com.tecknobit.refy.ui.screens.collections.components.CollectionCard
import com.tecknobit.refy.ui.screens.collections.presentation.CollectionsScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.create

/**
 * The [CollectionsScreen] class is useful to display the list of the collections available for the
 * [com.tecknobit.refy.localUser]
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemsScreen
 */
class CollectionsScreen : ItemsScreen<CollectionsScreenViewModel>(
    title = Res.string.collections,
    viewModel = CollectionsScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        restoreDefaultApplicationThemeStatusBar()
        super.ArrangeScreenContent()
    }

    /**
     * Custom component used to display the items list as grid
     */
    @Composable
    @NonRestartableComposable
    override fun Items() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaginatedLazyVerticalStaggeredGrid(
                modifier = Modifier
                    .responsiveMaxWidth()
                    .animateContentSize(),
                paginationState = viewModel.collectionsState,
                columns = StaggeredGridCells.Adaptive(
                    minSize = 325.dp
                ),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
    }

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate(UPSERT_COLLECTION_SCREEN)
    }

    /**
     * The representative text of the upsert action
     */
    override fun upsertText(): StringResource {
        return Res.string.create
    }

    /**
     * The representative icon of the upsert action
     */
    override fun upsertIcon(): ImageVector {
        return Icons.Default.CreateNewFolder
    }

}