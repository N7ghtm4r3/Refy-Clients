@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.collections.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.refy.COLLECTION_SCREEN
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
import refy.composeapp.generated.resources.created_on

@Composable
@NonRestartableComposable
fun CollectionCard(
    modifier: Modifier = Modifier,
    viewModel: CollectionsScreenViewModel,
    collection: LinksCollection
) {
    val expanded = remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .colorOneSideBorder(
                borderToColor = BorderToColor.TOP,
                width = 45.dp,
                color = collection.color.toColor(),
                shape = CardDefaults.shape
            ),
        onClick = {
            navigator.navigate(
                route = "$COLLECTION_SCREEN/${collection.id}/${collection.title}/${collection.color}"
            )
        }
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
                info = Res.string.created_on
            )
            CollectionBottomBar(
                expanded = expanded,
                viewModel = viewModel,
                collection = collection
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun CollectionBottomBar(
    expanded: MutableState<Boolean>,
    viewModel: CollectionsScreenViewModel,
    collection: LinksCollection
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpandCardButton(
            expanded = expanded
        )
        AttachItemButton(
            attachItemContent = { state, scope ->
                AttachCollection(
                    state = state,
                    scope = scope,
                    viewModel = viewModel,
                    collection = collection
                )
            }
        )
        DeleteItemButton(
            item = collection,
            deleteContent = { delete ->
                DeleteCollection(
                    show = delete,
                    viewModel = viewModel,
                    collection = collection
                )
            }
        )
    }
}