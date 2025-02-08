@file:OptIn(ExperimentalMultiplatform::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.collection.presenter

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.materialkolor.rememberDynamicColorScheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Dark
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Light
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.refy.localUser
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachCollection
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.DeleteCollection
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.components.links.LinksList
import com.tecknobit.refy.ui.icons.FolderManaged
import com.tecknobit.refy.ui.screens.collection.components.CollectionLinkCard
import com.tecknobit.refy.ui.screens.collection.components.CollectionTeamCard
import com.tecknobit.refy.ui.screens.collection.helpers.adaptStatusBarToCollectionTheme
import com.tecknobit.refy.ui.screens.collection.presentation.CollectionScreenViewModel
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.shared.presenters.ItemScreen
import com.tecknobit.refy.ui.theme.AppTypography
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.delete
import refy.composeapp.generated.resources.edit
import refy.composeapp.generated.resources.links

class CollectionScreen(
    collectionId: String,
    collectionName: String,
    private val collectionColor: String
) : ItemScreen<LinksCollection, CollectionScreenViewModel>(
    viewModel = CollectionScreenViewModel(
        collectionId = collectionId
    ),
    itemName = collectionName,
    snackbarHostStateBottomPadding = 0.dp,
    contentBottomPadding = 0.dp
) {

    @Composable
    override fun ArrangeScreenContent() {
        val colorScheme = rememberDynamicColorScheme(
            primary = collectionColor.toColor(),
            isDark = when (localUser.theme) {
                Dark -> true
                Light -> false
                Auto -> isSystemInDarkTheme()
            },
            isAmoled = true
        )
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            adaptStatusBarToCollectionTheme()
            super.ArrangeScreenContent()
        }
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ExtendedFAB() {
        awaitNullItemLoaded(
            itemToWait = item.value,
            extras = { collection -> collection.iAmTheOwner() }
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                val state = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                val scope = rememberCoroutineScope()
                SmallFloatingActionButton(
                    onClick = {
                        scope.launch {
                            state.show()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Attachment,
                        contentDescription = null
                    )
                }
                AttachContent(
                    state = state,
                    scope = scope
                )
                val delete = remember { mutableStateOf(false) }
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        ),
                    containerColor = MaterialTheme.colorScheme.error,
                    onClick = { delete.value = !delete.value }
                ) {
                    Text(
                        text = stringResource(Res.string.delete)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
                DeleteItemContent(
                    delete = delete
                )
                super.ExtendedFAB()
            }
        }
    }

    @Composable
    override fun ColumnScope.ItemDetails() {
        TeamsSection()
        LinksSection()
    }

    @Composable
    @NonRestartableComposable
    private fun TeamsSection() {
        PaginatedLazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            paginationState = this@CollectionScreen.viewModel.collectionTeams,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            this.items(
                items = this@CollectionScreen.viewModel.collectionTeams.allItems!!,
                key = { team -> team.id }
            ) { team ->
                CollectionTeamCard(
                    viewModel = viewModel,
                    team = team
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        LinksHeader()
        ResponsiveContent(
            onExpandedSizeClass = {
                LinksGrid(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        CollectionLinkCard(
                            viewModel = viewModel,
                            collection = item.value!!,
                            link = link
                        )
                    }
                )
            },
            onMediumSizeClass = {
                LinksGrid(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        CollectionLinkCard(
                            viewModel = viewModel,
                            collection = item.value!!,
                            link = link
                        )
                    }
                )
            },
            onCompactSizeClass = {
                LinksList(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        CollectionLinkCard(
                            viewModel = viewModel,
                            collection = item.value!!,
                            link = link
                        )
                    }
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LinksHeader() {
        Column(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 10.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.links),
                    style = AppTypography.headlineMedium
                )
                FilterButton()
            }
            FiltersInputField()
        }
    }

    override fun upsertIcon(): ImageVector {
        return FolderManaged
    }

    override fun upsertText(): StringResource {
        return Res.string.edit
    }

    override fun upsertAction() {
        // TODO: NAV TO EDIT
    }

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ColumnScope.TrailingContent() {
        awaitNullItemLoaded(
            itemToWait = item.value,
            extras = { collection -> collection.iAmTheOwner() }
        ) { collection ->
            Column {
                AttachItemButton(
                    attachItemContent = { state, scope ->
                        AttachContent(
                            state = state,
                            scope = scope
                        )
                    }
                )
                DeleteItemButton(
                    item = collection,
                    deleteContent = { delete ->
                        DeleteItemContent(
                            delete = delete
                        )
                    }
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun AttachContent(
        state: SheetState,
        scope: CoroutineScope
    ) {
        AttachCollection(
            state = state,
            scope = scope,
            collectionsManager = viewModel,
            collection = item.value!!
        )
    }

    @Composable
    @NonRestartableComposable
    override fun DeleteItemContent(
        delete: MutableState<Boolean>
    ) {
        DeleteCollection(
            show = delete,
            collectionsManager = viewModel,
            collection = item.value!!,
            onDelete = {
                delete.value = false
                navigator.goBack()
            }
        )
    }

}