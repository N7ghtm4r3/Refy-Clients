@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.collection.presenter

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.materialkolor.rememberDynamicColorScheme
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Dark
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Light
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.UPSERT_COLLECTION_SCREEN
import com.tecknobit.refy.localUser
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachCollection
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.DeleteCollection
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.icons.FolderManaged
import com.tecknobit.refy.ui.screens.collection.components.CollectionLinkCard
import com.tecknobit.refy.ui.screens.collection.components.CollectionTeamCard
import com.tecknobit.refy.ui.screens.collection.helpers.adaptStatusBarToCollectionTheme
import com.tecknobit.refy.ui.screens.collection.presentation.CollectionScreenViewModel
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presenters.ItemScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.delete
import refy.composeapp.generated.resources.edit

/**
 * The [CollectionScreen] class is useful to display the information of a [LinksCollection] and manage
 * which links are attached and where the collection is shared
 *
 * @param collectionId The identifier of the collection
 * @param collectionName The name of the collection
 * @param collectionColor The color of the collection
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemScreen
 */
class CollectionScreen(
    collectionId: String,
    collectionName: String,
    private val collectionColor: String
) : ItemScreen<LinksCollection, CollectionScreenViewModel>(
    viewModel = CollectionScreenViewModel(
        collectionId = collectionId,
        collectionName = collectionName,
        collectionColor = collectionColor
    ),
    name = collectionName
) {

    /**
     *`isCollectionShared` whether the collection is shared with teams
     */
    private var isCollectionShared: Boolean = false

    /**
     *`color` the color of the collection
     */
    private lateinit var color: State<Color>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        val colorScheme = rememberDynamicColorScheme(
            primary = color.value,
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

    /**
     * Custom [ExtendedFloatingActionButton] used to edit the item where needed
     */
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

    /**
     * Custom section used to display the items with a row layout
     */
    @Composable
    @NonRestartableComposable
    override fun ColumnScope.RowItems() {
        TeamsSection()
    }

    /**
     * The section where are displayed the teams where the collection is shared
     */
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

    /**
     * Custom card used to display with a properly card the information of a link attached to the [item]
     *
     * @param link The link to display
     */
    @Composable
    @NonRestartableComposable
    override fun ItemRelatedLinkCard(
        link: RefyLinkImpl
    ) {
        CollectionLinkCard(
            viewModel = viewModel,
            collection = item.value!!,
            showOwnerData = isCollectionShared,
            link = link
        )
    }

    /**
     * The representative icon of the upsert action
     */
    override fun upsertIcon(): ImageVector {
        return FolderManaged
    }

    /**
     * The representative text of the upsert action
     */
    override fun upsertText(): StringResource {
        return Res.string.edit
    }

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate("$UPSERT_COLLECTION_SCREEN/${item.value!!.id}/${item.value!!.color}")
    }

    /**
     * Custom trailing content to display in the [TopBar] component
     */
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

    /**
     * Content to allow the user to attach items to the current [item]
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
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

    /**
     * Content displayed when the user request to delete the [item]
     *
     * @param delete The state used to manage the visibility of this component
     */
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

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        color = viewModel.color.collectAsState(
            initial = collectionColor.toColor()
        )
        awaitNullItemLoaded(
            itemToWait = item.value
        ) { collection ->
            isCollectionShared = collection.isShared()
        }
    }

}