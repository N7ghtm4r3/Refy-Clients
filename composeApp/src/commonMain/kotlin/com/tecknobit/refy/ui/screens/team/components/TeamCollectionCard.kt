package com.tecknobit.refy.ui.screens.team.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.refy.COLLECTION_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.ItemTitle
import com.tecknobit.refy.ui.components.RemoveItemButton
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collection
import refy.composeapp.generated.resources.links_contained

/**
 * Custom card used to display the information of a collection shared with the team
 *
 * @param viewModel The support viewmodel for the screen
 * @param iAmAnAdmin Whether the [com.tecknobit.refy.localUser] is an [com.tecknobit.refycore.enums.TeamRole.ADMIN] of the team
 * @param collection The collection shared with the team
 */
@Composable
fun TeamCollectionCard(
    viewModel: TeamScreenViewModel,
    iAmAnAdmin: Boolean,
    collection: LinksCollection
) {
    val collectionColor = collection.color.toColor()
    val contrasting = getContrastColor(
        backgroundColor = collectionColor
    )
    Card(
        modifier = Modifier
            .width(200.dp),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = {
            navigator.navigate(
                route = "$COLLECTION_SCREEN/${collection.id}/${collection.title}/${collection.color}"
            )
        }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = collectionColor,
                overlineColor = contrasting,
                headlineColor = contrasting,
                supportingColor = contrasting
            ),
            overlineContent = {
                Text(
                    text = stringResource(Res.string.collection)
                )
            },
            headlineContent = {
                ItemTitle(
                    item = collection
                )
            },
            supportingContent = {
                val links = collection.links.size
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.links_contained,
                        quantity = links,
                        links
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = if (collection.iAmTheOwner() || iAmAnAdmin) {
                {
                    RemoveItemButton(
                        removeAction = {
                            viewModel.removeCollection(
                                collection = collection
                            )
                        },
                        color = contrasting
                    )
                }
            } else
                null
        )
    }
}