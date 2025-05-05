package com.tecknobit.refy.ui.screens.collection.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.TEAM_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.ItemTitle
import com.tecknobit.refy.ui.components.RemoveItemButton
import com.tecknobit.refy.ui.components.TeamLogo
import com.tecknobit.refy.ui.screens.collection.presentation.CollectionScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.members
import refy.composeapp.generated.resources.team

/**
 * Custom card used to display the information of a team where the collection is shared
 *
 * @param viewModel The support viewmodel for the screen
 * @param team The team where the collection is shared
 */
@Composable
fun CollectionTeamCard(
    viewModel: CollectionScreenViewModel,
    team: Team
) {
    Card(
        modifier = Modifier
            .width(230.dp),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = { navigator.navigate("$TEAM_SCREEN/${team.id}/${team.title}") }
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            overlineContent = {
                Text(
                    text = stringResource(Res.string.team)
                )
            },
            leadingContent = {
                TeamLogo(
                    team = team,
                    size = 50.dp
                )
            },
            headlineContent = {
                ItemTitle(
                    item = team
                )
            },
            supportingContent = {
                val members = team.members.size
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.members,
                        quantity = members,
                        members
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = if (team.iAmTheOwner()) {
                {
                    RemoveItemButton(
                        removeAction = {
                            viewModel.removeTeam(
                                team = team
                            )
                        }
                    )
                }
            } else
                null
        )
    }
}