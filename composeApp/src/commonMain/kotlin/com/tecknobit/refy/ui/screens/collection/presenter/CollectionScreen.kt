@file:OptIn(ExperimentalMultiplatform::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.collection.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.edit

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
    override fun ColumnScope.ItemDetails() {
        TeamsSection()
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
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
    override fun ColumnScope.TrailingContent() {
        var show by remember { mutableStateOf(false) }
        LaunchedEffect(item.value) {
            show = item.value != null
        }
        AnimatedVisibility(
            visible = show
        ) {
            Column {
                AttachItemButton(
                    attachItemContent = { state, scope ->
                        AttachCollection(
                            state = state,
                            scope = scope,
                            collectionsManager = viewModel,
                            collection = item.value!!
                        )
                    }
                )
                Row {

                    DeleteItemButton(
                        item = item.value!!,
                        deleteContent = { delete ->
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
                    )
                }
            }
        }
    }

}