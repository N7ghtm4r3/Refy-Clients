@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens.collections.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.refy.COLLECTION_SCREEN
import com.tecknobit.refy.UPSERT_COLLECTION_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachCollection
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.DeleteCollection
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.ExpandCardButton
import com.tecknobit.refy.ui.components.ItemCardDetails
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.collections.presentation.CollectionsScreenViewModel
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collection_created_on

/**
 * Custom card used to display the information of a [LinksCollection]
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param collection The collection to display
 */
@Composable
fun CollectionCard(
    modifier: Modifier = Modifier,
    viewModel: CollectionsScreenViewModel,
    collection: LinksCollection
) {
    val expanded = remember { mutableStateOf(false) }
    val descriptionLines = remember { mutableIntStateOf(0) }
    Card(
        modifier = modifier
            .colorOneSideBorder(
                borderToColor = BorderToColor.TOP,
                width = 45.dp,
                color = collection.color.toColor(),
                shape = CardDefaults.shape
            )
            .combinedClickable(
                onClick = {
                    navigator.navigate(
                        route = "$COLLECTION_SCREEN/${collection.id}/${collection.title}/${collection.color}"
                    )
                },
                onLongClick = if (collection.iAmTheOwner()) {
                    {
                        navigator.navigate("$UPSERT_COLLECTION_SCREEN/${collection.id}/${collection.color}")
                    }
                } else null
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp
                )
                .padding(
                    top = 25.dp,
                    bottom = 10.dp
                )
        ) {
            ItemCardDetails(
                modifier = Modifier
                    .padding(
                        bottom = 5.dp
                    ),
                expanded = expanded,
                item = collection,
                info = Res.string.collection_created_on,
                descriptionLines = descriptionLines
            )
            CollectionBottomBar(
                expanded = expanded,
                viewModel = viewModel,
                collection = collection,
                descriptionLines = descriptionLines
            )
        }
    }
}

/**
 * The bottom bar of the [CollectionCard] component
 *
 * @param expanded Whether the card is expanded
 * @param viewModel The support viewmodel for the screen
 * @param collection The collection to display
 * @param descriptionLines The number of the lines occupied by the link description
 */
@Composable
@NonRestartableComposable
private fun CollectionBottomBar(
    expanded: MutableState<Boolean>,
    viewModel: CollectionsScreenViewModel,
    collection: LinksCollection,
    descriptionLines: MutableState<Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                min = 35.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpandCardButton(
            descriptionLines = descriptionLines,
            expanded = expanded
        )
        if (collection.iAmTheOwner()) {
            AttachItemButton(
                attachItemContent = { state, scope ->
                    AttachCollection(
                        state = state,
                        scope = scope,
                        collectionsManager = viewModel,
                        collection = collection
                    )
                }
            )
        }
        DeleteItemButton(
            modifier = Modifier
                .weight(1f),
            item = collection,
            deleteContent = { delete ->
                DeleteCollection(
                    show = delete,
                    collectionsManager = viewModel,
                    collection = collection,
                    onDelete = {
                        delete.value = false
                        viewModel.refreshAfterLinksAttached()
                    }
                )
            }
        )
    }
}